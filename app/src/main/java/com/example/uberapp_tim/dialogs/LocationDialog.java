package com.example.uberapp_tim.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;


public class LocationDialog extends AlertDialog.Builder {
    public LocationDialog(Context context) {
        super(context);
        setUpDialog();
    }

    private void setUpDialog() {
        setTitle("oops");
        setMessage("Your Locations seems to be disabled, do you want to enable it?");
        setCancelable(false);

        setPositiveButton("Sure", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getContext().startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//				getContext().startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

                dialog.dismiss();
            }
        });

        setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
    }

    public AlertDialog prepareDialog() {
        AlertDialog dialog = create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }


}
