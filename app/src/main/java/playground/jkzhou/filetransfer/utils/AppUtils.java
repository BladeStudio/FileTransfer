package playground.jkzhou.filetransfer.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by JK.Zhou on 2016/12/3.
 */

public class AppUtils {

    private static final String TAG = "AppUtils";
    public static String CACHE_DIR = null;


    public static String getCacheDir(Context context) {
        File cache = context.getExternalCacheDir();
        if (cache == null) cache = context.getCacheDir();
        return cache.getPath();
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
