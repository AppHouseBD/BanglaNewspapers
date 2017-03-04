package com.apphousebd.banglanewspapers.utilities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * Created by Asif Imtiaz Shaafi on February, 2017.
 * Email: a15shaafi.209@gmail.com
 */

public class NetworkUtility {



    /***********************************************************************************
     * check if the user has network connection on or off, if user is not connected to
     * internet,that is has no network connection,then prompt the user to activate network
     * connection
     ************************************************************************************/
    public static boolean hasNetworkConnection(final Context context) {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();

        boolean connected = info != null && info.isConnectedOrConnecting();

        if (connected) {
            return true;
        } else {
            //prompt user to activate connection

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(context)
                            .setMessage("Your Internet Connection is turned off!\n" +
                                    "You need to be connected to internet to use this service!")
                            .setPositiveButton("Turn On Internet", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                }
                            })
                            .setNegativeButton("Cancel", null);

            Dialog dialog = builder.create();
            dialog.show();

            return false;
        }
    }


}
