package com.example.xd720p.sensorcontroller_09082016.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.example.xd720p.sensorcontroller_09082016.MainActivity;
import com.example.xd720p.sensorcontroller_09082016.models.ObservationPoints;
import com.example.xd720p.sensorcontroller_09082016.models.Sensors;
import com.example.xd720p.sensorcontroller_09082016.models.Temperature;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                    sender = msgs[i].getOriginatingAddress().toString();
                    str += sender + " : ";
                    str += msgs[i].getMessageBody().toString();
                }
            }

            saveToDB(parseSms(str), sender);



        }
    }

    private Map<String, Double> parseSms(String sms) {
        String pattern = "(T.*\\r*\\n)";

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(sms);

        List<String> str = new ArrayList<>();

        while(m.find()) {
            str.add(m.group());
        }

        String[] tmp;

        Map<String, Double> res = new LinkedHashMap<>();

        for (String s: str) {
            tmp = s.split("[=,-]", 2);

            if (tmp[1].startsWith("нет связи")) {
                tmp[1] = "-127";
            }

            res.put(tmp[0].substring(2, tmp[0].length()-1), Double.parseDouble(tmp[1]));
        }

        return res;
    }

    private void saveToDB(Map<String, Double> input, String phoneNumber) {
        Sensors sensors;
        Sensors existingSensor = null;
        Temperature temp;

        ObservationPoints op = ObservationPoints.findByNumber(phoneNumber);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy-HH-mm-ss");
        String currentDate = df.format(c.getTime());

        long time = System.currentTimeMillis();

        for (Map.Entry<String, Double> entry : input.entrySet()) {
            existingSensor = Sensors.findBySmsNameAndPoint(entry.getKey(), op.getId());

            if (existingSensor != null) {
                temp = new Temperature(op.getId(), existingSensor.getId(), entry.getValue(), time);
                temp.save();
                existingSensor = null;
            } else {
                sensors = new Sensors(op.getId(), 0, 1, "", "", entry.getKey(), currentDate, currentDate);
                sensors.save();
                temp = new Temperature(op.getId(), sensors.getId(), entry.getValue(), time);
                temp.save();
            }
        }
    }
}
