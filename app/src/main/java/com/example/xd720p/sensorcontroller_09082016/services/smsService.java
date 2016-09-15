package com.example.xd720p.sensorcontroller_09082016.services;

import android.app.IntentService;
import android.content.Intent;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class smsService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public smsService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }




    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
