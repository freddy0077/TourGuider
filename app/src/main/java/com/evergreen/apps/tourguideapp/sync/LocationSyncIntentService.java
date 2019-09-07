package com.evergreen.apps.tourguideapp.sync;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;

public class LocationSyncIntentService extends IntentService {
    public LocationSyncIntentService() {

        super("LocationSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        LocationsSyncTask.syncLocations(this);
    }
}
