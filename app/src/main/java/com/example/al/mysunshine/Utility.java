package com.example.al.mysunshine;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utility {
    public static String getPreferredLocation(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_location_key),
                context.getString(R.string.pref_default_location));
    }

    public static boolean isMetric(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_units_key),
                context.getString(R.string.pref_units_metric))
                .equals(context.getString(R.string.pref_units_metric));
    }

    static String formatTemperature(Context context, double temperature, boolean isMetric) {
        double temp;
        if ( !isMetric ) {
            temp = 9*temperature/5+32;
        } else {
            temp = temperature;
        }
        String result = String.format("%.0f", temp);
        if (temp > 0) {
            result = "+".concat(result);
        }
        return context.getString(R.string.format_temperature, temp);
    }

    static String formatWind(Context context, double wind) {
        return context.getString(R.string.format_wind, wind);
    }

    static String formatPressure(Context context, double pressure) {
        return context.getString(R.string.format_pressure, pressure);
    }

    static String formatHumidity(Context context, double humidity) {
        return context.getString(R.string.format_humidity, humidity);
    }

    static String formatDate(long dateInMillis, Context context) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(dateInMillis);

        Calendar calendar = Calendar.getInstance();
        if (date.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)) {
            return context.getString(R.string.today);
        }
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        if (date.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)) {
            return context.getString(R.string.tomorrow);
        }

        return DateFormat.getDateInstance().format(date.getTime());
    }
}