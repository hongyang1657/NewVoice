package fitme.ai.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import fitme.ai.R;
import fitme.ai.utils.ApMgr;
import fitme.ai.utils.AppContext;
import fitme.ai.utils.BaseTransfer;
import fitme.ai.utils.Consts;
import fitme.ai.utils.HotSpotBroadcaseReceiver;
import fitme.ai.utils.L;
import fitme.ai.utils.NetUtils;
import fitme.ai.utils.NetworkStateUtil;
import fitme.ai.utils.WifiAutoConnectManager;
import fitme.ai.utils.WifiMgr;

/**
 * Created by hongy on 2017/6/15.
 */

public class LaunchActivity extends Activity{

    private Button btNext,btStartAP;
    /**
     * 便携热点状态接收器
     */
    private HotSpotBroadcaseReceiver mHotSpotBroadcaseReceiver;

    private WifiManager wifiManager;

    private String ssid;
    private String password;

    private boolean isWifiClose = true;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //判断网络状态
            if (!NetworkStateUtil.isNetworkAvailable(LaunchActivity.this)){
                L.i("当前没网络");
                final WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
                if(wifiManager != null && wifiManager.getWifiState()==wifiManager.WIFI_STATE_DISABLED){
                    L.i("开启wifi");
                    wifiManager.setWifiEnabled(true);
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            while (isWifiClose){
                                try {
                                    sleep(1000);
                                    if (wifiManager.getWifiState()==wifiManager.WIFI_STATE_ENABLED){
                                        isWifiClose = false;
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            L.i("连接指定的网络");
                            WifiAutoConnectManager wifiAutoConnectManager = new WifiAutoConnectManager((WifiManager) getSystemService(Context.WIFI_SERVICE));
                            wifiAutoConnectManager.connect(ssid,password, WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WPA);
                        }
                    }.start();
                }else if (wifiManager != null && wifiManager.getWifiState()==wifiManager.WIFI_STATE_ENABLED){
                    L.i("连接指定的网络");
                    WifiAutoConnectManager wifiAutoConnectManager = new WifiAutoConnectManager((WifiManager) getSystemService(Context.WIFI_SERVICE));
                    wifiAutoConnectManager.connect(ssid,password, WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WPA);
                }
            }else {
                //有网络,初始化其他组件

            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_layout);
        initView();
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    }

    private void initView(){
        btNext = (Button) findViewById(R.id.bt_to_next);
        btStartAP = (Button) findViewById(R.id.bt_start_ap);
    }

    public void launchActivityClick(View v){
        switch (v.getId()){
            case R.id.bt_to_next:
                //跳转下一页
                startActivity(new Intent(LaunchActivity.this,MainActivity.class));
                finish();
                break;
            case R.id.bt_start_ap:
                //开启ap热点
                boolean isWifiApOpen = setWifiAPModel();
                if (isWifiApOpen){
                    //开启UDP服务，接收手机传来的udp数据
                    registerHotSpotReceiver();
                }
                break;

        }
    }

    //开启AP热点
    private boolean setWifiAPModel(){
        //关闭wifi
        wifiManager.setWifiEnabled(false);
        //配置热点
        Method method1 = null;
        try {
            //通过反射机制打开热点
            method1 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            WifiConfiguration netConfig = new WifiConfiguration();

            netConfig.SSID = "FitmeVoiceBox";
            netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            netConfig.wepKeys[0] = "";
            netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.wepTxKeyIndex = 0;
            return (boolean) method1.invoke(wifiManager, netConfig, true);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        } catch (SecurityException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 等待接收端发送初始化完成指令线程
     * @return
     */
    private Runnable receiveInitSuccessOrderRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    //开始接收接收端发来的指令
                    L.i("开始接收接收端发来的指令");
                    receiveInitSuccessOrder(Consts.DEFAULT_SERVER_UDP_PORT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
    private void registerHotSpotReceiver() {
        mHotSpotBroadcaseReceiver = new HotSpotBroadcaseReceiver() {
            @Override
            public void onHotSpotEnabled() {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //等待接收端连接
                        L.i("等待接收端连接");
                        Runnable mUdpServerRunnable = receiveInitSuccessOrderRunnable();
                        AppContext.MAIN_EXECUTOR.execute(mUdpServerRunnable);
                    }
                }, 2000);
            }
        };
        L.i("IntentFilter");
        IntentFilter filter = new IntentFilter(HotSpotBroadcaseReceiver.ACTION_HOTSPOT_STATE_CHANGED);
        registerReceiver(mHotSpotBroadcaseReceiver, filter);
    }

    private DatagramSocket mDatagramSocket;
    private void receiveInitSuccessOrder(int serverPort) throws Exception {
        //确保WiFi连接后获取正确IP地址
        int tryCount = 0;
        String localIpAddress = ApMgr.getHotspotLocalIpAddress(this);
        Log.i("hy_debug_message", "receiveInitSuccessOrder确保WiFi连接后获取正确IP地址: "+localIpAddress);
        while (localIpAddress.equals(Consts.DEFAULT_UNKNOW_IP) && tryCount < Consts.DEFAULT_TRY_COUNT) {
            Thread.sleep(1000);
            localIpAddress = ApMgr.getHotspotLocalIpAddress(this);
            L.i("---------tryCount------"+tryCount);
            tryCount ++;
        }

        /** 这里使用UDP发送和接收指令 */
        mDatagramSocket = new DatagramSocket(serverPort);
        while (true) {
            L.i("发送udp---------------");
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            mDatagramSocket.receive(receivePacket);
            String response = new String(receivePacket.getData()).trim();
            if(!TextUtils.isEmpty(response) && !response.equals("null")) {
                L.i("接收到的udpppp消息 -------->>>" + response);
                //拿到wifi账号密码后，尝试连接wifi
                JSONObject object = new JSONObject(response);
                JSONObject data = object.getJSONObject("data");
                ssid = data.getString("ssid");
                password = data.getString("password");
                connectWifi();
            }
        }
    }

    /**
     * 热点开关是否打开
     * @return
     */
    public boolean isWifiApEnabled() {
        try {
            Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifiManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关闭WiFi热点
     */
    public void closeWifiAp() {
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        if (isWifiApEnabled()) {
            try {
                Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);
                WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);
                Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method2.invoke(wifiManager, config, false);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    //关闭热点，打开wifi，连接
    private void connectWifi(){
        closeWifiAp();
        mHandler.sendEmptyMessageDelayed(1,3000);
    }

}
