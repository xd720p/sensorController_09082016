package com.example.xd720p.sensorcontroller_09082016.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by xd720p on 16.09.16.
 */
public class SmsAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 322;


    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, SmsSenderService.class);
        context.startService(i);

    }
}
