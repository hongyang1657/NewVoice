package fitme.ai.view.impl;


import fitme.ai.bean.MessageGet;
import fitme.ai.bean.Status;
import fitme.ai.bean.StatusWithUrl;

/**
 * Created by blw on 2016/9/11.
 */
public interface IWordInputActivityView {
    void showMessageCreate(StatusWithUrl status);
    void showMessageGet(MessageGet messageGet);
    void showMessageArrived(Status status);
}
