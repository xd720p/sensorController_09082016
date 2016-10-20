package com.example.xd720p.sensorcontroller_09082016.services;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.example.xd720p.sensorcontroller_09082016.models.ObservationPoints;
import com.example.xd720p.sensorcontroller_09082016.models.Sensors;
import com.example.xd720p.sensorcontroller_09082016.models.Temperature;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class SmsSenderService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SmsSenderService(String name) {
        super(name);
    }

    public SmsSenderService() {
        super ("smsService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        List<ObservationPoints> observationPointsList = ObservationPoints.getAllActiveTPoints();

        for (ObservationPoints item : observationPointsList) {
           sendSms(item.getPHONE_T());
        }


        stopSelf();
    }

    private void sendSms(String phoneNumber) {
        String smsBody = "*";
        String SMS_SENT = "SMS_SENT";
        String SMS_DELIVERED = "SMS_DELIVERED";

        Calendar cal = Calendar.getInstance();

        String text = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);

        appendLog(text);

        SmsManager smsManager = android.telephony.SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, smsBody, null, null);

    }

    public void appendLog(String text)
    {
        File logFile = new File("sdcard/Download/log.file");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
