package com.priyo.focus;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CountdownService extends Service {
    public CountdownService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}