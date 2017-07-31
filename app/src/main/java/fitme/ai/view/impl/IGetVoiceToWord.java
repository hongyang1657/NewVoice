package fitme.ai.view.impl;

/**
 * Created by hongy on 2017/5/22.
 */

public interface IGetVoiceToWord {

    void getResult(String result);
    //声音太小10118错误
    void showLowVoice(String result);

    void appendResult(CharSequence sequence);

}
