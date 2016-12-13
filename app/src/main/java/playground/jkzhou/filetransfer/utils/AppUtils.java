package playground.jkzhou.filetransfer.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by JK.Zhou on 2016/12/3.
 */

public class AppUtils {

	private static final String TAG = "AppUtils";
	private static String CACHE_DIR = null;

	public static boolean copyAssets(Context context, String src, String dest) {
		CACHE_DIR = getCacheDir(context);
		return copyAssets(context.getAssets(), src, dest);
	}

	public static void copyFile(Context context, String from, String to) {
		try {
			doCopyFile(context.getAssets(), from, to);
		} catch (IOException e) {
			Log.e(TAG, "copyFile: Copy " + from + " to " + to + " Failed", e);
		}
	}

	public static String formatIPv4(int ip) {
		return String.format(Locale.getDefault(), "%d.%d.%d.%d",
				ip & 0xff, ip >> 8 & 0xff, ip >> 16 & 0xff, ip >> 24 & 0xff);
	}

	public static String getCacheDir(Context context) {
		File cache = context.getExternalCacheDir();
		if (cache == null) cache = context.getCacheDir();
		return cache.getPath();
	}

	public static String getDeviceWifiIP(Context app) {
		WifiManager wifiMgr = (WifiManager) app.getSystemService(WIFI_SERVICE);
		return formatIPv4(wifiMgr.getConnectionInfo().getIpAddress());
	}

	public static List<String> scanAssets(Context context, String path) {
		List<String> list = new ArrayList<>();

		getAssetsPaths(context.getAssets(), path, list);

		return list;
	}

	public static void trimCache(Context context) {
		try {
			File dir = context.getExternalCacheDir();
			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
		} catch (Exception e) {
			Log.e(TAG, "trimCache: Error", e);
		}
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

	private static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] subPaths = dir.list();
			for (String subPath : subPaths) {
				boolean success = deleteDir(new File(dir, subPath));
				if (!success) {
					return false;
				}
			}
			// The directory is now empty so delete it
			return dir.delete();
		}

		return false;
	}

	private static void doCopyFile(AssetManager asMgr, String from, String to) throws IOException {
		Log.v(TAG, "doCopyFile: copying " + from + " to " + to);
		FileUtils.copyInputStreamToFile(asMgr.open(from), new File(to));
	}

	private static void getAssetsPaths(AssetManager asMgr, String path, List<String> list) {
		String[] paths;

		try {
			paths = asMgr.list(path);
		} catch (IOException e) {
			Log.e(TAG, "getAssetsList: Failed to access path " + path, e);
			return;
		}

		if (paths.length == 0) {
	        /* current path is a file */
			if (path != null && !path.isEmpty()) list.add(path);
		} else {
            /* current path is a dir */
			for (String p : paths) {
				getAssetsPaths(asMgr, path + File.separator + p, list);
			}
		}

	}

}
