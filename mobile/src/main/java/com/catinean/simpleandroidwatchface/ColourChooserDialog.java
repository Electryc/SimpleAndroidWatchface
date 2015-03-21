package com.catinean.simpleandroidwatchface;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ColourChooserDialog extends DialogFragment {

    private Listener colourSelectedListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        colourSelectedListener = (Listener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pick_background_colour)
                .setItems(R.array.colors_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String[] colours = getResources().getStringArray(R.array.colors_array);
                        colourSelectedListener.onColourSelected(colours[which], getTag());
                    }
                });
        return builder.create();
    }

    interface Listener {
        void onColourSelected(String colour, String tag);
    }
}
