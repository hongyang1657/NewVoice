package fitme.ai.utils;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hongy on 2017/5/31.
 */

public class WifiAPManager {
    private static final String tag = "WifiApManager";

    private static final String METHOD_GET_WIFI_AP_STATE = "getWifiApState";
    private static final String METHOD_SET_WIFI_AP_ENABLED = "setWifiApEnabled";
    private static final String METHOD_GET_WIFI_AP_CONFIG = "getWifiApConfiguration";
    private static final String METHOD_IS_WIFI_AP_ENABLED = "isWifiApEnabled";

    private static final Map<String, Method> methodMap = new HashMap<String, Method>();
    private static Boolean mIsSupport;
    private static boolean mIsHtc;

    public synchronized static final boolean isSupport() {
        if (mIsSupport != null) {
            return mIsSupport;
        }

        boolean result = Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO;
        if (result) {
            try {
                Field field = WifiConfiguration.class.getDeclaredField("mWifiApProfile");
                mIsHtc = field != null;
            } catch (Exception e) {
            }
        }

        if (result) {
            try {
                String name = METHOD_GET_WIFI_AP_STATE;
                Method method = WifiManager.class.getMethod(name);
                methodMap.put(name, method);
                result = method != null;
            } catch (SecurityException e) {
                L.i(tag+"SecurityException"+e);
            } catch (NoSuchMethodException e) {
                L.i(tag+ "NoSuchMethodException"+ e);
            }
        }

        if (result) {
            try {
                String name = METHOD_SET_WIFI_AP_ENABLED;
                Method method = WifiManager.class.getMethod(name, WifiConfiguration.class, boolean.class);
                methodMap.put(name, method);
                result = method != null;
            } catch (SecurityException e) {
                L.i(tag+"SecurityException"+e);
            } catch (NoSuchMethodException e) {
                L.i(tag+"NoSuchMethodException"+e);
            }
        }

        if (result) {
            try {
                String name = METHOD_GET_WIFI_AP_CONFIG;
                Method method = WifiManager.class.getMethod(name);
                methodMap.put(name, method);
                result = method != null;
            } catch (SecurityException e) {
                L.i(tag+ "SecurityException"+ e);
            } catch (NoSuchMethodException e) {
                L.i(tag+ "NoSuchMethodException"+e);
            }
        }

        if (result) {
            try {
                String name = getSetWifiApConfigName();
                Method method = WifiManager.class.getMethod(name, WifiConfiguration.class);
                methodMap.put(name, method);
                result = method != null;
            } catch (SecurityException e) {
                L.i(tag+"SecurityException"+e);
            } catch (NoSuchMethodException e) {
                L.i(tag+"NoSuchMethodException"+e);
            }
        }

        if (result) {
            try {
                String name = METHOD_IS_WIFI_AP_ENABLED;
                Method method = WifiManager.class.getMethod(name);
                methodMap.put(name, method);
                result = method != null;
            } catch (SecurityException e) {
                L.i(tag+"SecurityException"+e);
            } catch (NoSuchMethodException e) {
                L.i(tag+"NoSuchMethodException"+e);
            }
        }

        mIsSupport = result;
        return isSupport();
    }

    private final WifiManager mWifiManager;
    public WifiAPManager(WifiManager manager) {
        if (!isSupport()) {
            throw new RuntimeException("Unsupport Ap!");
        }
        L.i(tag+ "Build.BRAND -----------> " + Build.BRAND);

        mWifiManager = manager;
    }
    public WifiManager getWifiManager() {
        return mWifiManager;
    }

    /*public int getWifiApState() {
        try {
            Method method = methodMap.get(METHOD_GET_WIFI_AP_STATE);
            return (Integer) method.invoke(mWifiManager);
        } catch (Exception e) {
            L.i(tag+ e.getMessage());
        }
        return WifiApState.WIFI_AP_STATE_UNKWON;
    }*/

    /*private WifiConfiguration getHtcWifiApConfiguration(WifiConfiguration standard){
        WifiConfiguration htcWifiConfig = standard;
        try {
            Object mWifiApProfileValue = BeanUtils.getFieldValue(standard, "mWifiApProfile");

            if (mWifiApProfileValue != null) {
                htcWifiConfig.SSID = (String)BeanUtils.getFieldValue(mWifiApProfileValue, "SSID");
            }
        } catch (Exception e) {
            Logger.e(tag, "" + e.getMessage(), e);
        }
        return htcWifiConfig;
    }*/

    /*public WifiConfiguration getWifiApConfiguration() {
        WifiConfiguration configuration = null;
        try {
            Method method = methodMap.get(METHOD_GET_WIFI_AP_CONFIG);
            configuration = (WifiConfiguration) method.invoke(mWifiManager);
            if(isHtc()){
                configuration = getHtcWifiApConfiguration(configuration);
            }
        } catch (Exception e) {
            L.i(tag+e.getMessage());
        }
        return configuration;
    }*/

    /*public boolean setWifiApConfiguration(WifiConfiguration netConfig) {
        boolean result = false;
        try {
            if (isHtc()) {
                setupHtcWifiConfiguration(netConfig);
            }

            Method method = methodMap.get(getSetWifiApConfigName());
            Class<?>[] params = method.getParameterTypes();
            for (Class<?> clazz : params) {
                L.i(tag+ "param -> " + clazz.getSimpleName());
            }

            if (isHtc()) {
                int rValue = (Integer) method.invoke(mWifiManager, netConfig);
                L.i(tag+ "rValue -> " + rValue);
                result = rValue > 0;
            } else {
                result = (Boolean) method.invoke(mWifiManager, netConfig);
            }
        } catch (Exception e) {
            L.i(tag+""+ e);
        }
        return result;
    }*/

    public boolean setWifiApEnabled(WifiConfiguration configuration, boolean enabled) {
        boolean result = false;
        try {
            Method method = methodMap.get(METHOD_SET_WIFI_AP_ENABLED);
            result = (Boolean)method.invoke(mWifiManager, configuration, enabled);
        } catch (Exception e) {
            L.i(tag+ e.getMessage()+ e);
        }
        return result;
    }

    public boolean isWifiApEnabled() {
        boolean result = false;
        try {
            Method method = methodMap.get(METHOD_IS_WIFI_AP_ENABLED);
            result = (Boolean)method.invoke(mWifiManager);
        } catch (Exception e) {
            L.i(tag+ e.getMessage()+ e);
        }
        return result;
    }

    /*private void setupHtcWifiConfiguration(WifiConfiguration config) {
        try {
            L.i(tag+ "config=  " + config);
            Object mWifiApProfileValue = BeanUtils.getFieldValue(config, "mWifiApProfile");

            if (mWifiApProfileValue != null) {
                BeanUtils.setFieldValue(mWifiApProfileValue, "SSID", config.SSID);
                BeanUtils.setFieldValue(mWifiApProfileValue, "BSSID", config.BSSID);
                BeanUtils.setFieldValue(mWifiApProfileValue, "secureType", "open");
                BeanUtils.setFieldValue(mWifiApProfileValue, "dhcpEnable", 1);
            }
        } catch (Exception e) {
            Logger.e(tag, "" + e.getMessage(), e);
        }
    }*/

    public static boolean isHtc() {
        return mIsHtc;
    }

    private static String getSetWifiApConfigName() {
        return mIsHtc? "setWifiApConfig": "setWifiApConfiguration";
    }
}
