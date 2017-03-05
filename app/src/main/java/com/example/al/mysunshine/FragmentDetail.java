package com.example.al.mysunshine;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.al.mysunshine.data.WeatherContract;

public class FragmentDetail extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
// LIFECYCLE

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mIconView = (ImageView) rootView.findViewById(R.id.detail_icon);
        mDateView = (TextView) rootView.findViewById(R.id.detail_date_textview);
        mWeatherView = (TextView) rootView.findViewById(R.id.detail_forecast_textview);
        mMaxView = (TextView) rootView.findViewById(R.id.detail_high_textview);
        mMinView = (TextView) rootView.findViewById(R.id.detail_low_textview);
        mHumidityView = (TextView) rootView.findViewById(R.id.detail_humidity_textview);
        mWindView = (TextView) rootView.findViewById(R.id.detail_wind_textview);
        mPressureView = (TextView) rootView.findViewById(R.id.detail_pressure_textview);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

// MENU

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail_fragment, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);
        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        if (mForecast != null) {
            mShareActionProvider.setShareIntent(createShareIntent());
        }
    }

// LOADER

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Intent intent = getActivity().getIntent();
        if(intent == null) {
            return null;
        }
        return new CursorLoader(
                getActivity(),
                intent.getData(),
                DETAIL_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return;
        }

        String date = Utility.formatDate(cursor
                .getLong(COLUMN_DATE), getContext());
        String weather = cursor
                .getString(COLUMN_SHORT_DESC);
        String maxTemp = Utility.formatTemperature(getContext(), cursor
                .getLong(COLUMN_MAX_TEMP), true);
        String minTemp = Utility.formatTemperature(getContext(), cursor
                .getLong(COLUMN_MIN_TEMP), true);
        String humidity = Utility.formatHumidity(getContext(), cursor
                .getLong(COLUMN_HUMIDITY));
        String wind = Utility.getFormattedWind(getContext(), cursor
                .getLong(COLUMN_WIND_SPEED), cursor.getLong(COLUMN_WIND_DEGREES));
        String pressure = Utility.formatPressure(getContext(), cursor
                .getLong(COLUMN_PRESSURE));

        //for sharing
        mForecast = String.format("%s - %s : %s/%s", date, weather, maxTemp, minTemp);

        mIconView.setImageResource(android.R.drawable.ic_dialog_info);
        mDateView.setText(date);
        mWeatherView.setText(weather);
        mMaxView.setText(maxTemp);
        mMinView.setText(minTemp);
        mHumidityView.setText(humidity);
        mWindView.setText(wind);
        mPressureView.setText(pressure);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

// PRIVATE

    private Intent createShareIntent() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        //cool stuff:
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast.concat(HASHTAG));
        return shareIntent;
    }

// CONSTANTS

    private static final String TAG = FragmentDetail.class.getSimpleName();

    private static final String HASHTAG = " #MySunshine";
    private static final int DETAIL_LOADER = 1;

    // Adapter

    private static final String[] DETAIL_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            // This works because the WeatherProvider returns location data joined with
            // weather data, even though they're stored in two different tables.
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING
    };

    static final int COLUMN_WEATHER_ID = 0;
    static final int COLUMN_DATE = 1;
    static final int COLUMN_SHORT_DESC = 2;
    static final int COLUMN_MAX_TEMP = 3;
    static final int COLUMN_MIN_TEMP = 4;
    static final int COLUMN_HUMIDITY = 5;
    static final int COLUMN_PRESSURE = 6;
    static final int COLUMN_WIND_SPEED = 7;
    static final int COLUMN_WIND_DEGREES = 8;
    static final int COLUMN_WEATHER_ADD_ID = 9;

// VARIABLES

    private ShareActionProvider mShareActionProvider;
    private String mForecast;

    // Views

    private ImageView mIconView;
    private TextView mDateView;
    private TextView mWeatherView;
    private TextView mMaxView;
    private TextView mMinView;
    private TextView mHumidityView;
    private TextView mWindView;
    private TextView mPressureView;
}
