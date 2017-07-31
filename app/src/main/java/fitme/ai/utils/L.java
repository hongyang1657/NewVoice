

package fitme.ai.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/3/26.
 */

public class L {

    private static boolean debug = true;
    private static final String TAG = "hy_debug_message";

    public static void i(String str){

        if (debug){
            Log.i(TAG, "Message: "+str);
        }
    }

    public static void logE(String content) {
        if (debug){
            int p = 2048;
            long length = content.length();
            if (length < p || length == p)
                Log.e(TAG, content);
            else {
                while (content.length() > p) {
                    String logContent = content.substring(0, p);
                    content = content.replace(logContent, "");
                    Log.e(TAG, logContent);
                }
                Log.e(TAG, content);
            }
        }
    }
}
