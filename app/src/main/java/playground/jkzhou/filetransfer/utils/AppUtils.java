package playground.jkzhou.filetransfer.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by JK.Zhou on 2016/12/3.
 */

public class AppUtils {

    private static final String TAG = "AppUtils";
    private static String CACHE_DIR = null;


    public static String[] acquireHostInfo() {
        /*
        * NOTE: DO NOT invoke this method from a MAIN thread, otherwise you would get an
        * android.os.NetworkOnMainThreadException, because performing network operations
        * on a main thread is discouraged by Android, its better to do it on other thread
        * or use AsyncTask.
        */
        String[] result = new String[2];
        InetAddress net;
        try {
            net = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            Log.e(TAG, "acquireHostInfo: Failed to access local host", e);
            return result;
        }
        result[0] = net.getHostName();
        result[1] = net.getHostAddress();
        Log.d(TAG, "acquireHostInfo: Host Name:" + result[0] + ",\tHost Address:" + result[1]);
        return result;
    }

    public static String formatIPv4(int ip) {
        return String.format(Locale.getDefault(), "%d.%d.%d.%d",
                ip & 0xff, ip >> 8 & 0xff, ip >> 16 & 0xff, ip >> 24 & 0xff);
    }

    public static String getDeviceWifiIP(Context app) {
        WifiManager wifiMgr = (WifiManager) app.getSystemService(WIFI_SERVICE);
        return formatIPv4(wifiMgr.getConnectionInfo().getIpAddress());
    }

    public static String getCacheDir(Context context) {
        File cache = context.getExternalCacheDir();
        if (cache == null) cache = context.getCacheDir();
        return cache.getPath();
    }

    public static void listAssets(Context context, String path) {
        listAssets(context.getAssets(), path);
    }

    private static void listAssets(AssetManager asMgr, String path) {
        String[] paths;
        try {
            paths = asMgr.list(path);
        } catch (IOException e) {
            Log.e(TAG, "listAssets: cannot access path " + path, e);
            return;
        }
        for (String p : paths) {
            Log.d(TAG, "listAssets: " + path + File.separator + p);
            listAssets(asMgr, path + File.separator + p);
        }
    }

    public static boolean copyAssets(Context context, String src, String dest) {
        CACHE_DIR = getCacheDir(context);
        return copyAssets(context.getAssets(), src, dest);
    }

    private static boolean copyAssets(AssetManager asMgr, String src, String dest) {

        boolean success = true;
        String[] paths;

        try {
            paths = asMgr.list(src);
        } catch (IOException e) {
            Log.e(TAG, "copyAssets: Failed to access path " + src, e);
            return false;
        }

        if (paths.length == 0) {
            /* current path is a file */
            try {
                doCopyFile(asMgr, src, CACHE_DIR + File.separator + dest);
            } catch (IOException e) {
                Log.e(TAG, "copyAssets: Failed to copy " + src + " to " + dest, e);
                success = false;
            }
        } else {
            /* current path is a dir */
            for (String path : paths) {
                success &= copyAssets(asMgr, src + File.separator + path, dest + File.separator + path);
            }
        }

        return success;
    }

    private static void doCopyFile(AssetManager asMgr, String from, String to) throws IOException {
        Log.v(TAG, "doCopyFile: copying " + from + " to " + to);
        FileUtils.copyInputStreamToFile(asMgr.open(from), new File(to));
    }

}
