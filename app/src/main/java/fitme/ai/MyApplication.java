package fitme.ai;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.util.ArrayList;
import java.util.List;

import cn.com.broadlink.sdk.BLLet;
import cn.com.broadlink.sdk.data.controller.BLDNADevice;
import fitme.ai.bean.YeelightDeviceBean;
import fitme.ai.utils.BLUserInfoUnits;
import fitme.ai.utils.L;
import fitme.ai.utils.Location;
import fitme.ai.utils.UnCeHandler;

/**
 * 语音技术由科大讯飞提供
 *
 */

public class MyApplication extends Application {

    //保存经度信息
    private int longitude;
    //保存纬度信息
    private int latitude;

    //定位类
    private Location location;
    //主界面是否是第一次启动,用于启动一次定位请求
    private boolean isMainFirstStart;

    //博联
    public static ArrayList<BLDNADevice> mDevList = new ArrayList<BLDNADevice>();
    public static BLUserInfoUnits mBLUserInfoUnits;



    @Override
    public void onCreate() {
        super.onCreate();
        L.i("初始化讯飞");
        //gqgz的账号  59ae515e
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=59ae515e");
        //fitme APP的账号
        //SpeechUtility.createUtility(this, SpeechConstant.APPID +"=57edd2db");
        //初始化博联sdk
        mBLUserInfoUnits = new BLUserInfoUnits(this);
        BLLet.init(this);
        BLLet.DebugLog.on();

        //设置该CrashHandler为程序的默认处理器
        /*UnCeHandler catchExcep = new UnCeHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(catchExcep);*/

    }

    private List<YeelightDeviceBean> yeelightDeviceBeanList;

    public void initYeelightDeviceList(){
        yeelightDeviceBeanList = new ArrayList<>();
    }

    public void setYeelightDeviceBean(YeelightDeviceBean yeelightDeviceBean){
        yeelightDeviceBeanList.add(yeelightDeviceBean);
    }

    public List<YeelightDeviceBean> getYeelightDeviceBeanList(){
        return yeelightDeviceBeanList;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public void appFinish(){
        BLLet.finish();
    }

    /**
     * 关闭Activity列表中的所有Activity*/
    public void finishActivity(){

        //杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
