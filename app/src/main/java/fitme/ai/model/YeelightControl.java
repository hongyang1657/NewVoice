package fitme.ai.model;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fitme.ai.MyApplication;
import fitme.ai.utils.L;
import fitme.ai.view.impl.IGetYeelight;

/**
 * Created by hongy on 2017/8/23.
 */

public class YeelightControl {

    private volatile static YeelightControl instance;

    private MyApplication application;
    private WifiManager.MulticastLock multicastLock;

    private String yeelightResponse = null;

    private IGetYeelight iGetYeelight;

    private YeelightControl(MyApplication application, IGetYeelight iGetYeelight) {
        this.iGetYeelight = iGetYeelight;
        this.application = application;
        WifiManager wm = (WifiManager)application.getSystemService(Context.WIFI_SERVICE);
        multicastLock = wm.createMulticastLock("test");
        multicastLock.acquire();
    }

    public static YeelightControl getInstance(MyApplication application, IGetYeelight iGetYeelight){
        if(instance==null) {
            synchronized (YeelightControl.class) {       //锁
                if(instance ==null) {
                    instance =new YeelightControl(application,iGetYeelight);
                }
            }
        }
        return instance;
    }

    private static final String TAG = "APITEST";
    private static final int MSG_SHOWLOG = 0;
    private static final int MSG_FOUND_DEVICE = 1;
    private static final int MSG_DISCOVER_FINISH = 2;
    private static final int MSG_STOP_SEARCH = 3;
    private static final int MSG_RESPONSE = 4;

    private static final String UDP_HOST = "239.255.255.250";
    private static final int UDP_PORT = 1982;
    private static final String message = "M-SEARCH * HTTP/1.1\r\n" +
            "HOST:239.255.255.250:1982\r\n" +
            "MAN:\"ssdp:discover\"\r\n" +
            "ST:wifi_bulb\r\n"; //用于发送的字符串
    private DatagramSocket mDSocket;
    private boolean mSeraching = true;
    List<HashMap<String, String>> mDeviceList = new ArrayList<HashMap<String, String>>();

    private static final int MSG_CONNECT_SUCCESS_SWITCH = 5;
    private static final int MSG_CONNECT_SUCCESS_BRIGHT = 6;
    private static final int MSG_CONNECT_SUCCESS_COLOR = 7;
    private static final int MSG_CONNECT_SUCCESS_CT = 8;

    private static final int MSG_CONNECT_FAILURE = 1;
    private static final String CMD_TOGGLE = "{\"id\":%id,\"method\":\"toggle\",\"params\":[]}\r\n" ;
    private static final String CMD_ON = "{\"id\":%id,\"method\":\"set_power\",\"params\":[\"on\",\"smooth\",500]}\r\n" ;
    private static final String CMD_OFF = "{\"id\":%id,\"method\":\"set_power\",\"params\":[\"off\",\"smooth\",500]}\r\n" ;
    private static final String CMD_CT = "{\"id\":%id,\"method\":\"set_ct_abx\",\"params\":[%value, \"smooth\", 500]}\r\n";
    private static final String CMD_HSV = "{\"id\":%id,\"method\":\"set_hsv\",\"params\":[%value, 100, \"smooth\", 200]}\r\n";
    private static final String CMD_BRIGHTNESS = "{\"id\":%id,\"method\":\"set_bright\",\"params\":[%value, \"smooth\", 200]}\r\n";
    private static final String CMD_BRIGHTNESS_SCENE = "{\"id\":%id,\"method\":\"set_bright\",\"params\":[%value, \"smooth\", 500]}\r\n";
    private static final String CMD_COLOR_SCENE = "{\"id\":%id,\"method\":\"set_scene\",\"params\":[\"cf\",1,0,\"100,1,%color,1\"]}\r\n";
    private static final String CMD_GET = "{\"id\":%id,\"method\":\"get_prop\",\"params\":[\"power\"]}\r\n" ;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_FOUND_DEVICE:
                    //L.i("yeelight_MSG_FOUND_DEVICE："+msg.obj.toString());
                    break;
                case MSG_SHOWLOG:
                    //L.i("yeelight_MSG_SHOWLOG："+msg.obj.toString());
                    break;
                case MSG_STOP_SEARCH:
                    mSearchThread.interrupt();
                    multicastLock.release();
                    L.i("yeelight停止搜索");
                    mSeraching = false;
                    iGetYeelight.getDevices(mDeviceList);
                    break;
                case MSG_DISCOVER_FINISH:
                    L.i("yeelight:"+"msg_discover_finish");
                    break;
                case MSG_RESPONSE:
                    L.i("yeelight收到硬件的回复："+msg.obj.toString());
                    yeelightResponse = msg.obj.toString();
                    break;
                case MSG_CONNECT_SUCCESS_SWITCH:
                    write(parseSwitch((Boolean) msg.obj));
                    break;
                case MSG_CONNECT_SUCCESS_BRIGHT:
                    write(parseBrightnessCmd((Integer) msg.obj));
                    break;
                case MSG_CONNECT_SUCCESS_COLOR:
                    write(parseColorCmd((Integer) msg.obj));
                    break;
                case MSG_CONNECT_SUCCESS_CT:
                    write(parseCTCmd((Integer) msg.obj));
                    break;
            }
        }
    };

    private Thread mSearchThread = null;
    public void searchDevice() {
        mDeviceList.clear();
        mSeraching = true;
        mSearchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mDSocket = new DatagramSocket();
                    DatagramPacket dpSend = new DatagramPacket(message.getBytes(),
                            message.getBytes().length, InetAddress.getByName(UDP_HOST),
                            UDP_PORT);
                    mDSocket.send(dpSend);
                    mHandler.sendEmptyMessageDelayed(MSG_STOP_SEARCH,2000);
                    while (mSeraching) {
                        byte[] buf = new byte[1024];
                        DatagramPacket dpRecv = new DatagramPacket(buf, buf.length);
                        mDSocket.receive(dpRecv);
                        byte[] bytes = dpRecv.getData();
                        StringBuffer buffer = new StringBuffer();
                        for (int i = 0; i < dpRecv.getLength(); i++) {
                            // parse /r
                            if (bytes[i] == 13) {
                                continue;
                            }
                            buffer.append((char) bytes[i]);
                        }
                        Log.d("socket", "got message:" + buffer.toString());

                        if (!buffer.toString().contains("yeelight")) {
                            mHandler.obtainMessage(MSG_SHOWLOG, "收到一条消息,不是Yeelight灯泡").sendToTarget();
                            return;
                        }else {
                            mHandler.obtainMessage(MSG_FOUND_DEVICE, buffer.toString()).sendToTarget();
                        }
                        String[] infos = buffer.toString().split("\n");
                        HashMap<String, String> bulbInfo = new HashMap<String, String>();
                        for (String str : infos) {
                            int index = str.indexOf(":");
                            if (index == -1) {
                                continue;
                            }
                            String title = str.substring(0, index);
                            String value = str.substring(index + 1);
                            bulbInfo.put(title, value);
                        }
                        if (!hasAdd(bulbInfo)){
                            mDeviceList.add(bulbInfo);
                        }

                    }
                    mHandler.sendEmptyMessage(MSG_DISCOVER_FINISH);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mSearchThread.start();

    }

    private boolean hasAdd(HashMap<String,String> bulbinfo){
        for (HashMap<String,String> info : mDeviceList){
            Log.d(TAG, "location params = " + bulbinfo.get("Location"));
            if (info.get("Location").equals(bulbinfo.get("Location"))){
                return true;
            }
        }
        return false;
    }

    /*
    *       控制部分
    */

    private boolean cmd_run = true;
    private int mCmdId;
    private Socket mSocket;
    private BufferedOutputStream mBos;
    private BufferedReader mReader;

    public void connect(final String mBulbIP, final int mBulbPort){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cmd_run = true;
                    mSocket = new Socket(mBulbIP, mBulbPort);
                    mSocket.setKeepAlive(true);
                    mBos= new BufferedOutputStream(mSocket.getOutputStream());
                    //mHandler.sendEmptyMessage(MSG_CONNECT_SUCCESS);
                    mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                    while (cmd_run){
                        try {
                            String value = mReader.readLine();
                            //Log.d("hy_debug_message", "接收到的value = "+value);
                            iGetYeelight.getResponse(value);
                            mHandler.obtainMessage(MSG_RESPONSE, value).sendToTarget();
                        }catch (Exception e){

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(MSG_CONNECT_FAILURE);
                }
            }
        }).start();
    }

    public String getState(){
        String cmd;
        cmd = CMD_GET.replace("%id", String.valueOf(++mCmdId));
        return cmd;
    }

    public String parseSwitch(boolean on){
        String cmd;
        if (on){
            cmd = CMD_ON.replace("%id", String.valueOf(++mCmdId));
        }else {
            cmd = CMD_OFF.replace("%id", String.valueOf(++mCmdId));
        }
        return cmd;
    }
    public String parseCTCmd(int ct){
        return CMD_CT.replace("%id", String.valueOf(++mCmdId)).replace("%value", String.valueOf(ct+3400));
    }
    public String parseColorCmd(int color){
        return CMD_HSV.replace("%id", String.valueOf(++mCmdId)).replace("%value", String.valueOf(color));
    }
    public String parseBrightnessCmd(int brightness){
        return CMD_BRIGHTNESS.replace("%id", String.valueOf(++mCmdId)).replace("%value", String.valueOf(brightness));
    }
    public void write(final String cmd){
        Log.i("hy_debug_message","发送的命令："+cmd);

        new Thread(){
            @Override
            public void run() {
                super.run();
                //L.i("mBos:"+mBos+"mSocket:"+mSocket.isConnected());
                if (mBos != null && mSocket.isConnected()){
                    try {
                        L.i("发送命令成功！！！！！！！！");
                        mBos.write(cmd.getBytes());
                        mBos.flush();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG,"mBos = null or mSocket is closed");
                }
            }
        }.start();

    }

    //释放组播锁
    public void release(){
        multicastLock.release();
    }

    //获取yeelight硬件的回复
    public String getYeelightResponse(){
        return yeelightResponse;
    }

    //连接设备，并发送控制指令
    public void connectAndControl(final String mBulbIP, final int mBulbPort, final boolean on, final int bright, final int color, final int CT){
        cmd_run = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cmd_run = true;
                    mSocket = new Socket(mBulbIP, mBulbPort);
                    mSocket.setKeepAlive(true);
                    mBos= new BufferedOutputStream(mSocket.getOutputStream());
                    if (bright>-1){
                        mHandler.obtainMessage(MSG_CONNECT_SUCCESS_BRIGHT, bright).sendToTarget();
                    }else if (color>-1){
                        mHandler.obtainMessage(MSG_CONNECT_SUCCESS_COLOR, color).sendToTarget();
                    }else if (CT>-1){
                        mHandler.obtainMessage(MSG_CONNECT_SUCCESS_CT, color).sendToTarget();
                    }else {
                        mHandler.obtainMessage(MSG_CONNECT_SUCCESS_SWITCH, on).sendToTarget();
                    }
                    mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                    while (cmd_run){
                        try {
                            String value = mReader.readLine();
                            iGetYeelight.getResponse(value);
                            mHandler.obtainMessage(MSG_RESPONSE, value).sendToTarget();
                        }catch (Exception e){

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(MSG_CONNECT_FAILURE);
                }
            }
        }).start();
    }
}
