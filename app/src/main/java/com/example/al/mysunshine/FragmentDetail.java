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
import android.widget.TextView;

import com.example.al.mysunshine.data.WeatherContract;

public class FragmentDetail extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = FragmentDetail.class.getSimpleName();
    public static final String HASHTAG = " #MySunshine";
    private static final int DETAIL_LOADER = 1;

    private ShareActionProvider mShareActionProvider;
    private String mForecast;

    private static final String[] DETAIL_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
    };

    static final int COLUMN_WEATHER_ID = 0;
    static final int COLUMN_DATE = 1;
    static final int COLUMN_SHORT_DESC = 2;
    static final int COLUMN_MAX_TEMP = 3;
    static final int COLUMN_MIN_TEMP = 4;
    static final int COLUMN_HUMIDITY = 5;
    static final int COLUMN_WIND_SPEED = 5;
    static final int COLUMN_PRESSURE = 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

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

    private Intent createShareIntent() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        //cool stuff:
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast.concat(HASHTAG));
        return shareIntent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

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
        String wind = Utility.formatWind(getContext(), cursor
                .getLong(COLUMN_WIND_SPEED));
        String pressure = Utility.formatPressure(getContext(), cursor
                .getLong(COLUMN_PRESSURE));

        //for sharing
        mForecast = String.format("%s - %s : %s/%s", date, weather, maxTemp, minTemp);

        TextView dateView = (TextView) getView().findViewById(R.id.detail_date_textview);
        dateView.setText(date);
        TextView weatherView = (TextView) getView().findViewById(R.id.detail_forecast_textview);
        weatherView.setText(weather);
        TextView maxView = (TextView) getView().findViewById(R.id.detail_high_textview);
        maxView.setText(maxTemp);
        TextView minView = (TextView) getView().findViewById(R.id.detail_low_textview);
        minView.setText(minTemp);
        TextView humidityView = (TextView) getView().findViewById(R.id.detail_humidity_textview);
        humidityView.setText(humidity);
        TextView windView = (TextView) getView().findViewById(R.id.detail_wind_textview);
        windView.setText(wind);
        TextView pressureView = (TextView) getView().findViewById(R.id.detail_pressure_textview);
        pressureView.setText(pressure);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}
