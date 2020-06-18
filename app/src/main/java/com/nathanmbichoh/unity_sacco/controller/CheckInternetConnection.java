package com.nathanmbichoh.unity_sacco.controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nathanmbichoh.unity_sacco.R;

public class CheckInternetConnection {
    /** CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT */
    public static void checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connMgr != null;
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet
            if (!activeNetworkInfo.isConnected()) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                builder.setBackground(context.getResources().getDrawable(R.drawable.error_card, null));
                builder.setMessage("Please check your internet connection.");
                builder.show();
            }
        } else {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
            builder.setBackground(context.getResources().getDrawable(R.drawable.error_card, null));
            builder.setMessage("Please check your internet connection.");
            builder.show();
        }
    }
}

