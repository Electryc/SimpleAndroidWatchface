package com.catinean.simpleandroidwatchface;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class SimpleWatchFaceConfigurationActivity extends Activity implements ColourChooserDialog.Listener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String PATH = "/simple_watch_face_config";
    private static final String TAG = "SimpleWatchface";
    private static final String KEY_BACKGROUND_COLOUR = "KEY_BACKGROUND_COLOUR";
    private static final String KEY_DATE_TIME_COLOUR = "KEY_DATE_TIME_COLOUR";
    private static final String TAG_BACKGROUND_COLOUR_CHOOSER = "background_chooser";
    private static final String TAG_DATE_AND_TIME_COLOUR_CHOOSER = "date_time_chooser";

    private GoogleApiClient googleApiClient;
    private View backgroundColourImagePreview;
    private View dateAndTimeColourImagePreview;
    private WatchConfigurationPreferences watchConfigurationPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        findViewById(R.id.configuration_background_colour).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColourChooserDialog.newInstance(getString(R.string.pick_background_colour))
                        .show(getFragmentManager(), TAG_BACKGROUND_COLOUR_CHOOSER);
            }
        });

        findViewById(R.id.configuration_time_colour).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColourChooserDialog.newInstance(getString(R.string.pick_date_time_colour))
                        .show(getFragmentManager(), TAG_DATE_AND_TIME_COLOUR_CHOOSER);
            }
        });

        backgroundColourImagePreview = findViewById(R.id.configuration_background_colour_preview);
        dateAndTimeColourImagePreview = findViewById(R.id.configuration_date_and_time_colour_preview);

        watchConfigurationPreferences = WatchConfigurationPreferences.newInstance(this);

        backgroundColourImagePreview.setBackgroundColor(watchConfigurationPreferences.getBackgroundColour());
        dateAndTimeColourImagePreview.setBackgroundColor(watchConfigurationPreferences.getDateAndTimeColour());
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onColourSelected(String colour, String tag) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create(PATH);

        if (TAG_BACKGROUND_COLOUR_CHOOSER.equals(tag)) {
            backgroundColourImagePreview.setBackgroundColor(Color.parseColor(colour));
            watchConfigurationPreferences.setBackgroundColour(Color.parseColor(colour));
            putDataMapReq.getDataMap().putString(KEY_BACKGROUND_COLOUR, colour);
        } else {
            dateAndTimeColourImagePreview.setBackgroundColor(Color.parseColor(colour));
            watchConfigurationPreferences.setDateAndTimeColour(Color.parseColor(colour));
            putDataMapReq.getDataMap().putString(KEY_DATE_TIME_COLOUR, colour);
        }

        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        Wearable.DataApi.putDataItem(googleApiClient, putDataReq);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed");
    }

    @Override
    protected void onStop() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }
}
