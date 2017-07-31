package fitme.ai.utils;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.Map;

import fitme.ai.MyApplication;


/**
 * Created by blw on 2016/9/11.
 */
public class Location {
    //定位客户端
    private AMapLocationClient locationClient = null;
    //定位客户端的具体设置参数
    private AMapLocationClientOption locationOption =null;
    //app类
    private MyApplication app;


    /**
     * 构造方法，进行初始化定位
     */
    public Location(Context context,MyApplication app) {
        //初始化client
        locationClient = new AMapLocationClient(context);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        //初始化参数
        locationOption=new AMapLocationClientOption();
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        //app
        this.app = app;
    }
    /**
     * 开始定位
     */
    public void startLocation(){
        //启动定位
        locationClient.startLocation();
    }
    /**
     * 设置默认定位参数
     */
    private AMapLocationClientOption getDefaultOption() {
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        locationOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        locationOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        locationOption.setInterval(20000);//可选，设置定位间隔。默认为10秒
        locationOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是ture
        locationOption.setWifiActiveScan(false);//设置是否强制刷新WIFI，默认为true，强制刷新。
        locationOption.setMockEnable(false);        //设置是否允许模拟位置,默认为false，不允许模拟位置
        locationOption.setLocationCacheEnable(true);//可选，设置是否开启缓存
        locationOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        locationOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        return locationOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            getLocationStr(location);
        }
    };

    /**
     * 获取定位结果
     */
    private synchronized void getLocationStr(AMapLocation location){
        if (null != location&&location.getErrorCode()==0) {
            //经度
            double longitude = location.getLongitude();
            app.setLongitude( (int)(longitude*Math.pow(10,7)));
            //纬度
            double latitude = location.getLatitude();
            app.setLatitude((int)(latitude*Math.pow(10,7)));

            L.i("定位结果"+"经度:"+longitude+",纬度:"+latitude+",转换后经度:"+(int)(longitude*Math.pow(10,7))+",转换后纬度:"+(int)(latitude*Math.pow(10,7)));

        } else {
            Log.i("定位失败","错误码:"+location.getErrorCode()+"\n错误信息:"+location.getErrorInfo()+"\n错误描述:"+ location.getLocationDetail());
        }
    }

    /**
     * 停止定位
     */
    public void stopLocation(){
        // 停止定位
        if(locationClient!=null){
            locationClient.stopLocation();
        }
    }

    /**
     * 销毁定位
     */
    public void destroyLocation(){
        if (null != locationClient) {
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }
}
