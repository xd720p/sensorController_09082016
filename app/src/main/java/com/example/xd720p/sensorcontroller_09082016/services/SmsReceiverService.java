package com.example.xd720p.sensorcontroller_09082016.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xd720p on 18.09.16.
 */
public class SmsReceiverService extends BroadcastReceiver {
    private String TAG = SmsReceiverService.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;

        String str = "";
        List<String> inputMessages = new LinkedList<String>();

        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            String sender = "";

            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                if (sender.equals(msgs[i].getOriginatingAddress().toString())) {
                    str += msgs[i].getMessageBody().toString();
                } else {
                    if (!str.equals("")) {
                        inputMessages.add(str);
                        str = "";
                    }
                    sender = msgs[i].getOriginatingAddress().toString();
                    str += sender + " : ";
                    str += msgs[i].getMessageBody().toString();
                }
                if (i == msgs.length-1) {
                    inputMessages.add(str);
                    str = "";
                }
            }

        }
    }
}
