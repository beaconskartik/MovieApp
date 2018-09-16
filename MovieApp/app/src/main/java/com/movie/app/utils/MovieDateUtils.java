/**
 * Created by Kartik on 9/16/18.
 */

package com.movie.app.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class MovieDateUtils {
    private MovieDateUtils() {
    }

    public static String getDateAsString(Date date, String format, String timezone) {
        DateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        if (timezone == null) {
            formatter.setTimeZone(TimeZone.getDefault());
        } else {
            formatter.setTimeZone(TimeZone.getTimeZone(timezone));
        }
        return formatter.format(date);
    }

    public static Date getStringAsDate(String dateString, String format, String timezone) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        if (timezone == null) {
            formatter.setTimeZone(TimeZone.getDefault());
        } else {
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        Date date = new Date();
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            MovieLogUtils.logException("getStringAsDate exception :", e);
        }
        return date;
    }

    public static String truncateString(String str, int number) {
        if (str.length() > number) {
            str = str.substring(0, number) + "...";
        }
        return str;
    }
}
