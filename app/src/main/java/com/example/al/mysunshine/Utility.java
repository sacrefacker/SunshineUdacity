package com.example.al.mysunshine;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.util.Calendar;

public class Utility
{

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

    public static String formatTemperature(Context context, double temperature, boolean isMetric) {
        double temp;
        if (!isMetric) {
            temp = 9 * temperature / 5 + 32;
        }
        else {
            temp = temperature;
        }
        String result = context.getString(R.string.format_temperature, temp);
        if (temp > 0) {
            result = "+".concat(result);
        }
        return result;
    }

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

    public static String getFormattedWind(Context context, float windSpeed, float degrees) {
        int windFormat = R.string.format_wind;

        // From wind direction in degrees, determine compass direction as a string (e.g NW)
        // You know what's fun, writing really long if/else statements with tons of possible
        // conditions.  Seriously, try it!
        String direction = "";
        if (degrees >= 337.5 || degrees < 22.5) {
            direction = "N";
        }
        else if (degrees >= 22.5 && degrees < 67.5) {
            direction = "NE";
        }
        else if (degrees >= 67.5 && degrees < 112.5) {
            direction = "E";
        }
        else if (degrees >= 112.5 && degrees < 157.5) {
            direction = "SE";
        }
        else if (degrees >= 157.5 && degrees < 202.5) {
            direction = "S";
        }
        else if (degrees >= 202.5 && degrees < 247.5) {
            direction = "SW";
        }
        else if (degrees >= 247.5 && degrees < 292.5) {
            direction = "W";
        }
        else if (degrees >= 292.5 && degrees < 337.5) {
            direction = "NW";
        }
        return String.format(context.getString(windFormat), windSpeed, direction);
    }
}