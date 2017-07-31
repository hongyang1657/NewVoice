package fitme.ai.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.VoiceWakeuper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

import fitme.ai.view.impl.IGetVoiceToWord;

/**
 * 语音听写，语音转文字
 *
 */
public class VoiceToWords {
    private static String TAG = "VoiceToWords类";
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    //提示toast
    //private Toast mToast;
    //context
    private Context context;
    // 函数调用返回值,表示返回结果，失败或成功
    int ret = 0;
    //设置回调接口
    private IGetVoiceToWord mIGetVoiceToWord;

    private EditText txtOfUser;



    /**
     * 构造方法
     */
    public VoiceToWords(Context context, IGetVoiceToWord mIGetVoiceToWord) {
        this.context = context;
        this.mIGetVoiceToWord = mIGetVoiceToWord;
        this.txtOfUser = txtOfUser;
        //mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
        //设置参数
        setParams();
    }

    /**
     * 初始化监听器
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    /**
     * 展示提示框
     */
    private void showTip(final String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 开始语音听写
     */
    public void startRecognizer() {

        //清空听写结果
        mIatResults.clear();
        ret = mIat.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            showTip("听写失败,错误码：" + ret);
        } else {
            showTip("请开始说话");
        }
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
            L.i("讯飞开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            L.i("讯飞onError："+error.toString());
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
           // showTip(error.getPlainDescription(true));
            if("您好像没有说话哦.(错误码:10118)".equals(error.getPlainDescription(true))){
                String lowVoiceTip="";
                int i=new Random().nextInt(3);
                switch (i){
                    case 0:
                        lowVoiceTip="声音太小了，听不清呢";
                        showTip(lowVoiceTip);
                        break;
                    case 1:
                        lowVoiceTip="有在说话吗，没听到啊";
                        showTip(lowVoiceTip);
                        break;
                    case 2:
                        lowVoiceTip="您好像没有说话呢";
                        showTip(lowVoiceTip);
                        break;
                }
                mIGetVoiceToWord.showLowVoice(lowVoiceTip);
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
            L.i("讯飞结束说话");
        }


//        结果回调处
        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            String text = printResult(results);
            L.i("isLast:"+isLast);
            if(isLast&&!"".equals(text)){
                mIGetVoiceToWord.appendResult(text);
                mIGetVoiceToWord.getResult(text);
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            //showTip("当前正在说话，音量大小：" + volume);
            //Log.d(TAG, "返回音频数据：" + data.length);
            //L.i("返回音频数据"+ data.length);
        }


        //打印结果
        private String printResult(RecognizerResult results) {
            String text = JsonParser.parseIatResult(results.getResultString());

            String sn = null;
            // 读取json结果中的sn字段
            try {
                JSONObject resultJson = new JSONObject(results.getResultString());
                sn = resultJson.optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIatResults.put(sn, text);

            StringBuffer resultBuffer = new StringBuffer();
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
            }
            return resultBuffer.toString();
        }


        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    /**
     * 为听写对象设置参数
     */
    private void setParams() {
        //默认不设置的语音参数
        //SpeechConstant.NET_TIMEOUT:网络连接超时时间 默认20秒
        //SpeechConstant.KEY_SPEECH_TIMEOUT:语音输入超时时间  默认60秒
        //mIat.setParameter(SpeechConstant.SAMPLE_RATE, "16000") 默认的识别采样率支持16000Hz和8000Hz
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        //设置语音输入超时时间
        mIat.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT,"10000");
        mIat.setParameter(SpeechConstant.SAMPLE_RATE, "16000");
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        //获得当前系统的语言
        String language = context.getResources().getConfiguration().locale.getCountry();
        // 设置语言
        if (language.equals("US")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        }
        //应用领域
         //mIat.setParameter(SpeechConstant.DOMAIN, "iat");

        // 传入音频源。
        //录音机的录音方式，默认为MIC(MediaRecorder.AudioSource.MIC)
        // 设置音频来源为外部文件
        //mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
        // 也可以像以下这样直接设置音频文件路径识别（要求设置文件在sdcard上的全路径）：
        // mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-2");
        // mIat.setParameter(SpeechConstant.ASR_SOURCE_PATH, "sdcard/XXX/XXX.pcm");

        mIat.setParameter(SpeechConstant.VAD_ENABLE,"1");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "7000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/abcd/iat.wav");
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
    }

    public boolean isIatListening() {
        return mIat.isListening();
    }


    //停止录音，开始解析
    public void mTtsStop() {
        // 退出时释放连接
        if (mIat != null&&mIat.isListening()) {
            mIat.stopListening();
        }
    }

    public void mIatDestroy() {

        // 退出时释放连接
        if(mIat != null) {
            if(mIat.isListening()){
                mIat.cancel();
            }
            mIat.destroy();
        }
    }

}
