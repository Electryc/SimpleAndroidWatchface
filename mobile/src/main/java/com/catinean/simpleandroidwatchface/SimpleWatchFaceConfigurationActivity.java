package com.catinean.simpleandroidwatchface;

import android.app.Activity;
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

    private static final String PATH = "/watch_face_config/digital";
    private static final String TAG = "SimpleWatchface";
    private static final String KEY_BACKGROUND_COLOUR = "KEY_BACKGROUND_COLOUR";
    private static final String KEY_TIME_COLOUR = "KEY_TIME_COLOUR";
    private GoogleApiClient googleApiClient;

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
                new ColourChooserDialog().show(getFragmentManager(), "background_chooser");
            }
        });

        findViewById(R.id.configuration_time_colour).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColourChooserDialog().show(getFragmentManager(), "time_chooser");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onColourSelected(String colour, String tag) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create(PATH);
        putDataMapReq.getDataMap().putString("background_chooser".equals(tag) ? KEY_BACKGROUND_COLOUR : KEY_TIME_COLOUR, colour);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        Wearable.DataApi.putDataItem(googleApiClient, putDataReq);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e(TAG, "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed");
    }
}
