package fitme.ai.model;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.com.broadlink.econtrol.dataparse.BLNetWorkDataParser;
import cn.com.broadlink.econtrol.dataparse.data.A1Info;
import cn.com.broadlink.econtrol.dataparse.data.S1AlarmState;
import cn.com.broadlink.econtrol.dataparse.data.S1SensorAlarmState;
import cn.com.broadlink.econtrol.dataparse.data.S1SensorInfo;
import cn.com.broadlink.sdk.BLLet;
import cn.com.broadlink.sdk.constants.controller.BLControllerErrCode;
import cn.com.broadlink.sdk.data.controller.BLDNADevice;
import cn.com.broadlink.sdk.data.controller.BLStdData;
import cn.com.broadlink.sdk.param.controller.BLStdControlParam;
import cn.com.broadlink.sdk.result.controller.BLPassthroughResult;
import cn.com.broadlink.sdk.result.controller.BLStdControlResult;
import fitme.ai.utils.L;
import fitme.ai.view.MainActivity;

/**
 * Created by hongy on 2017/6/23.
 */

public class BLControl {

    private Map blDNADevicesMap = null;
    private BLDNADevice bldnaDevice;

    public BLControl(Map blDNADevicesMap) {
        this.blDNADevicesMap = blDNADevicesMap;
    }

    //控制sp设备(wifi开关)
    /*public void SPControl(int pwr){
        bldnaDevice = (BLDNADevice) blDNADevicesMap.get("sp");
        //控制设备参数
        BLStdData.Value value = new BLStdData.Value();
        value.setVal(pwr);

        ArrayList<BLStdData.Value> pwrVals = new ArrayList<>();
        pwrVals.add(value);

        BLStdControlParam ctrlParam = new BLStdControlParam();
        ctrlParam.setAct("set");
        ctrlParam.getParams().add("pwr");
        ctrlParam.getVals().add(pwrVals);

        if (bldnaDevice.getDid()!=null){
            final BLStdControlResult blStdControlResult = BLLet.Controller.dnaControl(bldnaDevice.getDid(), null, ctrlParam);
            L.i("blStdControlResult:"+blStdControlResult.getMsg()+"控制错误码："+blStdControlResult.getStatus());
        }else {
            //Toast.makeText(this, "没有找到设备", Toast.LENGTH_SHORT).show();
        }
    }*/

    //控制杜亚窗帘
    /*public void curtainControl(int pwr){
        bldnaDevice = (BLDNADevice) blDNADevicesMap.get("curtain");
        //控制设备参数
        BLStdData.Value value = new BLStdData.Value();
        value.setVal(pwr);

        ArrayList<BLStdData.Value> Vals = new ArrayList<>();
        Vals.add(value);

        BLStdControlParam ctrlParam = new BLStdControlParam();
        ctrlParam.setAct("set");
        ctrlParam.getParams().add("curtain_work");
        ctrlParam.getVals().add(Vals);

        if (bldnaDevice!=null){
            L.i("sssssssssssssssss"+bldnaDevice.getName()+"----"+bldnaDevice.getDid());
            final BLStdControlResult blStdControlResult = BLLet.Controller.dnaControl(bldnaDevice.getDid(), null, ctrlParam);
            L.i("blStdControlResult:"+blStdControlResult.getMsg()+"控制错误码："+blStdControlResult.getStatus());
        }else {
            L.i("没有找到该设备");
        }
    }*/

    //deviceName 设备名称，val 设定的值，param 键名
    public void dnaControlSet(String deviceName,String val,String param){

        bldnaDevice = (BLDNADevice) blDNADevicesMap.get(deviceName);
        new DnaControlSetTask(bldnaDevice).execute(param, val);
    }

    //通用DNA控制
    public class DnaControlSetTask extends AsyncTask<String, Void, BLStdControlResult> {

        private BLDNADevice bldnaDevice;

        public DnaControlSetTask(BLDNADevice bldnaDevice) {
            this.bldnaDevice = bldnaDevice;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected BLStdControlResult doInBackground(String... params) {
            String param = params[0];

            BLStdData.Value value = new BLStdData.Value();
            value.setVal(params[1]);

            ArrayList<BLStdData.Value> dnaVals = new ArrayList<>();
            dnaVals.add(value);

            BLStdControlParam stdControlParam = new BLStdControlParam();
            stdControlParam.setAct("set");
            stdControlParam.getParams().add(param);
            stdControlParam.getVals().add(dnaVals);
            return BLLet.Controller.dnaControl(bldnaDevice.getDid(), null, stdControlParam);
        }

        @Override
        protected void onPostExecute(BLStdControlResult result) {
            super.onPostExecute(result);
            if(result != null && result.getStatus() == BLControllerErrCode.SUCCESS){
                BLStdData stdData = result.getData();
                L.i("发送dna通用指令成功");
            }
        }
    }


    //透传控制,获取空气检测仪数据
    private byte[] passthroughControl;
    public String dnaPassthrough(){
        bldnaDevice = (BLDNADevice) blDNADevicesMap.get("a1");
        String result;
        passthroughControl = BLNetWorkDataParser.getInstace().a1RefreshBytes();
        if (bldnaDevice!=null){
            BLPassthroughResult blPassthroughResult = BLLet.Controller.dnaPassthrough(bldnaDevice.getDid(),null,passthroughControl);
            L.i("msg"+blPassthroughResult.getMsg()+"data::"+blPassthroughResult.getData());
            A1Info a1Info = BLNetWorkDataParser.getInstace().a1RefreshBytesParse(blPassthroughResult.getData());
            L.i("humidity:"+a1Info.humidity+"---light:"+a1Info.light+"---temperature:"+a1Info.temperature+"---voice:"+a1Info.voice+"---air_condition:"+a1Info.air_condition);
            int air = a1Info.air_condition;
            int voice = a1Info.voice;
            int light = a1Info.light;
            String strAir = "";
            String strVoice = "";
            String strLight = "";
            switch (air){
                case 0:
                    strAir = "优";
                    break;
                case 1:
                    strAir = "良";
                    break;
                case 2:
                    strAir = "正常";
                    break;
                case 3:
                    strAir = "差";
                    break;
            }
            switch (voice){
                case 0:
                    strVoice = "寂静";
                    break;
                case 1:
                    strVoice = "正常";
                    break;
                case 2:
                    strVoice = "吵闹";
                    break;
            }
            switch (light){
                case 0:
                    strLight = "暗";
                    break;
                case 1:
                    strLight = "昏暗";
                    break;
                case 2:
                    strLight = "正常";
                    break;
                case 3:
                    strLight = "亮";
                    break;
            }

            result = "当前"
                    +"湿度：百分之"+a1Info.humidity
                    +"，温度："+a1Info.temperature+"摄氏度"
                    +"，空气质量："+strAir
                    +"，环境声音："+strVoice
                    +"，光线："+strLight;
        }else {
            L.i("没有找到该设备");
            result = "没有找到该设备";
        }

        return result;
    }

    //获取安防S1的数据
    List<S1SensorInfo> s1Info = new ArrayList<>();
    public void getSecurityData(){
        bldnaDevice = (BLDNADevice) blDNADevicesMap.get("s1");
        if (bldnaDevice!=null){
            byte[] s1SensorPritect = BLNetWorkDataParser.getInstace().s1QuerySensorPritectMap();
            BLPassthroughResult blPassthroughs1SensorPritect = BLLet.Controller.dnaPassthrough(bldnaDevice.getDid(),null,s1SensorPritect);
            L.i("msg！！！！！！："+blPassthroughs1SensorPritect.getMsg()+"---"+blPassthroughs1SensorPritect.getData());



            byte[] s1bytes = BLNetWorkDataParser.getInstace().s1GetSensorAlarmState();
            BLPassthroughResult blPassthroughResult = BLLet.Controller.dnaPassthrough(bldnaDevice.getDid(),null,s1bytes);
            L.i("msg！！！！！！："+blPassthroughResult.getMsg()+"---"+blPassthroughResult.getData());

            S1AlarmState s1Alarm = BLNetWorkDataParser.getInstace().s1ParseGetSensorAlarmState(blPassthroughResult.getData());
            S1SensorAlarmState s1 = null;
            for (int i=0;i<s1Alarm.getCount();i++){
                s1 = s1Alarm.getStatusList().get(i);
                L.i(i+" :"+s1.getStatus());
            }
        }else {
            L.i("没有找到该设备");
        }

    }

    //通用控制红外，射频设备
    public void commandRedCodeDevice(String strRedCode,String deviceDid){
        new NewSendIrTask(deviceDid).execute(strRedCode);
    }

    //新的发送红外
    public class NewSendIrTask extends AsyncTask<String, Void, BLStdControlResult> {
        private String deviceDid;

        public NewSendIrTask(String deviceDid) {
            this.deviceDid = deviceDid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected BLStdControlResult doInBackground(String... params) {

            //设置要发送的红外指令
            BLStdData.Value value = new BLStdData.Value();
            value.setVal(params[0]);

            ArrayList<BLStdData.Value> irVals = new ArrayList<>();
            irVals.add(value);

            /**发送学习到的命令**/
            BLStdControlParam intoStudyParam = new BLStdControlParam();
            intoStudyParam.setAct("set");
            intoStudyParam.getParams().add("irda");
            intoStudyParam.getVals().add(irVals);
            return BLLet.Controller.dnaControl(deviceDid, null, intoStudyParam);
        }

        @Override
        protected void onPostExecute(BLStdControlResult stdControlResult) {
            super.onPostExecute(stdControlResult);
            if(stdControlResult != null && stdControlResult.getStatus() == BLControllerErrCode.SUCCESS){
                L.i("发送红外码或射频成功！");
            }
        }
    }
}
