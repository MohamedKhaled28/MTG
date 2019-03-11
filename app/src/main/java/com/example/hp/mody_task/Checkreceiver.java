package com.example.hp.mody_task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import Model.CheckConnectivity;

/**
 * Created by HP on 6/12/2017.
 */

public class Checkreceiver extends BroadcastReceiver {
   /* AlertDialog.Builder alertdialog;
    AlertDialog alert;*/
    @Override
    public void onReceive(Context context, Intent intent) {
       if(CheckConnectivity.isNetworkAvaliable(context)){

       }else{
           Toast.makeText(context,R.string.valid_connection,Toast.LENGTH_LONG).show();

       }
    }
}
