package com.evergreen.apps.tourguideapp.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;

import com.evergreen.apps.tourguideapp.providers.LocationProvider;
import com.evergreen.apps.tourguideapp.utils.Constants;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class LocationsSyncUtils {

    private static final int SYNC_INTERVAL_HOURS = 1;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    private static boolean sInitialized;

    private static final String LOCATIONS_SYNC_TAG = "locations-sync";

    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job syncLocationsJob = dispatcher.newJobBuilder()
                .setService(LocationFirebaseJobService.class)
                .setTag(LOCATIONS_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SYNC_INTERVAL_HOURS, SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
//                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(syncLocationsJob);
    }

    synchronized public static void initialize(@NonNull final Context context){

        if (sInitialized) return;

        sInitialized = true;

        scheduleFirebaseJobDispatcherSync(context);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri locationQueryUri = LocationProvider.URI_LOCATION;

                String selection = null;
                Cursor cursor = context.getContentResolver()
                        .query(locationQueryUri,
                                Constants.MAIN_LOCATION_PROJECTION,
                                selection,
                                Constants.MAIN_LOCATION_PROJECTION,
                                null);



                if (null == cursor || cursor.getCount() == 0){
                    startImmediateSync(context);
                }

                if (cursor != null) {
                    cursor.close();
                }
            }
        }).start();
    }

    public static void startImmediateSync(@NonNull final Context context){
        Intent intentToSyncImmediately = new Intent(context, LocationSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
