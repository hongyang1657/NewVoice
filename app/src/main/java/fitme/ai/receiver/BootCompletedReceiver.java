package fitme.ai.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import fitme.ai.utils.L;
import fitme.ai.view.MainActivity;

/**
 * Created by hongy on 2017/6/3.
 */

public class BootCompletedReceiver extends BroadcastReceiver{

    private static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        L.i("接收到广播"+intent.getAction());
        if (intent.getAction().equals(ACTION)) {
            Intent mainActivityIntent = new Intent(context, MainActivity.class);  // 要启动的Activity
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivityIntent);
            L.i("接收到自启广播："+intent.getAction());
        }
    }
}
