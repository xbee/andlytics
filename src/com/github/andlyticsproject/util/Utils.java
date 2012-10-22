
package com.github.andlyticsproject.util;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;

import com.github.andlyticsproject.io.MediaScannerWrapper;

/**
 * Utility class for simple helper methods.
 */
public final class Utils {
	// TODO Replace this with a user configurable date formatter
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEEEE, d MMM yyyy");

	/** Private constructor. */
	private Utils() {
	}
	

	public static String formatDate(Date date){
		return dateFormat.format(date);
	}

	/**
	 * get the code of the actual version.
	 *
	 * @param context
	 *            the context
	 * @return the code of the actual version
	 */
	public static int getActualVersionCode(final Context context) {
		// Get the versionCode of the Package, which must be different
		// (incremented) in each release on the market in the
		// AndroidManifest.xml
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES).versionCode;
		} catch (NameNotFoundException e) {
			return 0;
		}
	}

	/**
	 * get the name of the actual version.
	 *
	 * @param context
	 *            the context
	 * @return the name of the actual version
	 */
	public static String getActualVersionName(final Context context) {
		// Get the versionCode of the Package, which must be different
		// (incremented) in each release on the market in the
		// AndroidManifest.xml
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES).versionName;
		} catch (NameNotFoundException e) {
			return null;
		}
	}

	public static <P, T extends AsyncTask<P, ?, ?>> void execute(T task) {
		execute(task, (P[]) null);
	}

	@SuppressLint("NewApi")
	public static <P, T extends AsyncTask<P, ?, ?>> void execute(T task, P... params) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
		} else {
			task.execute(params);
		}
	}

	public static boolean isFroyo() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static void scanFile(Context ctx, String filename) {
		if (isFroyo()) {
			MediaScannerWrapper.scanFile(ctx, filename);
		}
	}

	public static void closeSilently(Closeable c) {
		if (c != null) {
			try {
				c.close();
			} catch (Exception e) {
			}
		}
	}

	public static String readFileAsString(String filename) {
		FileInputStream in = null;
		try {
			in = new FileInputStream(filename);
			byte[] data = new byte[in.available()];
			in.read(data);

			return new String(data, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (in != null) {
				Utils.closeSilently(in);
			}
		}
	}

}
