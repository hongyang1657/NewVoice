package fitme.ai.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import fitme.ai.view.MainActivity;

/**
 * Created by hongy on 2017/6/28.
 */

public class IflytekWakeUp {

    private Context mContext;
    //唤醒的阈值，就相当于门限值，当用户输入的语音的置信度大于这一个值的时候，才被认定为成功唤醒。
    private int curThresh = 10;
    //是否持续唤醒
    private String keep_alive = "1";
    /**
     * 闭环优化网络模式有三种：
     * 模式0：关闭闭环优化功能
     * <p>
     * 模式1：开启闭环优化功能，允许上传优化数据。需开发者自行管理优化资源。
     * sdk提供相应的查询和下载接口，请开发者参考API文档，具体使用请参考本示例
     * queryResource及downloadResource方法；
     * <p>
     * 模式2：开启闭环优化功能，允许上传优化数据及启动唤醒时进行资源查询下载；
     * 本示例为方便开发者使用仅展示模式0和模式2；
     */
    private String ivwNetMode = "0";
    // 语音唤醒对象
    private VoiceWakeuper mIvw;
    //存储唤醒词的ID
    private String wordID = "";
    private MainActivity.MyWakeuperListener listener;

    public IflytekWakeUp(Context context,MainActivity.MyWakeuperListener listener) {
        this.mContext = context;
        this.listener = listener;
        // 初始化唤醒对象
        mIvw = VoiceWakeuper.createWakeuper(mContext, null);
    }

    /**
     * 开启唤醒功能
     */
    public void startWakeuper() {
        //非空判断，防止因空指针使程序崩溃
        mIvw = VoiceWakeuper.getWakeuper();
        if (mIvw != null) {
            // 清空参数
            mIvw.setParameter(SpeechConstant.PARAMS, null);
            // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
            mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:" + curThresh);
            // 设置唤醒模式
            mIvw.setParameter(SpeechConstant.IVW_SST, "wakeup");
            // 设置持续进行唤醒
            mIvw.setParameter(SpeechConstant.KEEP_ALIVE, keep_alive);
            // 设置闭环优化网络模式
            mIvw.setParameter(SpeechConstant.IVW_NET_MODE, ivwNetMode);
            // 设置唤醒资源路径
            mIvw.setParameter(SpeechConstant.IVW_RES_PATH, getResource());
            // 设置唤醒录音保存路径，保存最近一分钟的音频
            mIvw.setParameter(SpeechConstant.IVW_AUDIO_PATH, Environment.getExternalStorageDirectory().getPath() + "/msc/ivw.wav");
            mIvw.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
            // 如有需要，设置 NOTIFY_RECORD_DATA 以实时通过 onEvent 返回录音音频流字节
            //mIvw.setParameter( SpeechConstant.NOTIFY_RECORD_DATA, "1" );

            // 启动唤醒
            mIvw.startListening(listener);
            L.i("初始化讯飞唤醒");
        }
    }

    public boolean isIvwListening(){
        if (mIvw!=null){
            return mIvw.isListening();
        }else {
            return false;
        }
    }


    /**
     * 获取唤醒词功能
     *
     * @return 返回文件位置
     */
    private String getResource() {
        final String resPath = ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, "ivw/" + "59ae515e" + ".jet");
        return resPath;
    }

    /**
     * 销毁唤醒功能
     */
    public void destroyWakeuper() {
        // 销毁合成对象
        mIvw = VoiceWakeuper.getWakeuper();
        if (mIvw != null) {
            mIvw.destroy();
        }
    }

    /**
     * 停止唤醒
     */
    public void stopWakeuper() {
        mIvw.stopListening();
        L.i("讯飞停止唤醒");
    }


}
