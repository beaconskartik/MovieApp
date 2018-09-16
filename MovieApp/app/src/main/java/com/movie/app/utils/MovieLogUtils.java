/**
 * Created by Kartik on 9/16/18.
 */

package com.movie.app.utils;

import android.util.Log;
import com.movie.app.BuildConfig;

public final class MovieLogUtils {
    public static void setLogTag(String logTag) {
        sMyLogTag = logTag;
    }

    public static void logFlow(String logText) {
        log(sMyLogTag + LOG_FLOW, logText);
    }

    public static void logError(String logText) {
        log(sMyLogTag + LOG_ERROR, logText);
    }

    public static void logException(String logText, Exception e) {
        log(sMyLogTag + LOG_EXCEPTION, logText + ": " + e.toString());
    }

    public static void logError(String logText, Error er) {
        log(sMyLogTag + LOG_ERROR, logText + ": " + er.toString());
    }

    public static void logWithTag(String tag, String logText) {
        log(tag, logText);
    }

    private static void log(String tag, String logText) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, logText);
        }
    }

    // Members variable
    private static String sMyLogTag = "movie_app";

    private static final String LOG_ERROR = "Error";
    private static final String LOG_FLOW = "Flow";
    private static final String LOG_EXCEPTION = "Exception";
}
