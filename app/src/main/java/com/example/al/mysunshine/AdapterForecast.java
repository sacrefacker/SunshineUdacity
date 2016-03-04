package com.example.al.mysunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.al.mysunshine.data.WeatherContract;

/**
 * {@link AdapterForecast} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class AdapterForecast extends CursorAdapter {

    public static final int VIEW_TYPE_TODAY = 0;
    public static final int VIEW_TYPE_OTHER = 1;

    public static final int COLUMN_DATE = 0;
    public static final int COLUMN_SHORT_DESC = 1;
    public static final int COLUMN_MAX_TEMP = 2;
    public static final int COLUMN_MIN_TEMP = 3;

    public AdapterForecast(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_TODAY : VIEW_TYPE_OTHER;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId  = (viewType == 0) ? R.layout.list_item_today : R.layout.list_item_forecast;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.
        String[] data = convertCursorRowToUXFormat(cursor);
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.iconView.setImageResource(R.drawable.ic_sync_black_24dp);

//        TextView forecast = (TextView) view.findViewById(R.id.list_item_forecast_textview);
//        forecast.setText(data[COLUMN_SHORT_DESC]);
        viewHolder.forecast.setText(data[COLUMN_SHORT_DESC]);
//        TextView date = (TextView) view.findViewById(R.id.list_item_date_textview);
//        date.setText(data[COLUMN_DATE]);
        viewHolder.date.setText(data[COLUMN_DATE]);
//        TextView max = (TextView) view.findViewById(R.id.list_item_high_textview);
//        max.setText(data[COLUMN_MAX_TEMP]);
        viewHolder.max.setText(data[COLUMN_MAX_TEMP]);
//        TextView min = (TextView) view.findViewById(R.id.list_item_low_textview);
//        min.setText(data[COLUMN_MIN_TEMP]);
        viewHolder.min.setText(data[COLUMN_MIN_TEMP]);

    }

    /*
        This is ported from TaskFetchWeather --- but now we go straight from the cursor to the
        string.
     */
    private String[] convertCursorRowToUXFormat(Cursor cursor) {
        // get row indices for our cursor
//        int idx_date = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE);
//        int idx_short_desc = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC);
//        int idx_max_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP);
//        int idx_min_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP);

        String[] data = new String[4];

        data[COLUMN_DATE] = Utility.formatDate(cursor.getLong(FragmentForecast
                .COL_WEATHER_DATE), mContext);
        data[COLUMN_SHORT_DESC] = cursor.getString(FragmentForecast
                .COL_WEATHER_DESC);
        data[COLUMN_MAX_TEMP] = Utility.formatTemperature(mContext, cursor
                .getDouble(FragmentForecast.COL_WEATHER_MAX_TEMP), Utility.isMetric(mContext));
        data[COLUMN_MIN_TEMP] = Utility.formatTemperature(mContext, cursor
                .getDouble(FragmentForecast.COL_WEATHER_MIN_TEMP), Utility.isMetric(mContext));

        return data;
    }

    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView forecast;
        public final TextView date;
        public final TextView max;
        public final TextView min;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            forecast = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            date = (TextView) view.findViewById(R.id.list_item_date_textview);
            max = (TextView) view.findViewById(R.id.list_item_high_textview);
            min = (TextView) view.findViewById(R.id.list_item_low_textview);
        }
    }
}