package com.evergreen.apps.tourguideapp.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;

public class LocationFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> mLocationsFetchTask;

    /**
     * The entry point to your Job. Implementations should offload work to another thread of
     * execution as soon as possible.
     *
     * @param job
     * @return whether there is more work remaining.
     */
    @Override
    public boolean onStartJob(final JobParameters job) {
        mLocationsFetchTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                LocationsSyncTask.syncLocations(context);
                jobFinished(job, false);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(job,false);
            }
        };

        mLocationsFetchTask.execute();

        Log.d("FirebaseJobService", "onStartJob: started");
        return true;
    }

    /**
     * Called when the scheduling engine has decided to interrupt the execution of a running job,
     * most likely because the runtime constraints associated with the job are no longer satisfied.
     *
     * @param job
     * @return whether the job should be retried
     * @see com.firebase.jobdispatcher.Job.Builder#setRetryStrategy(RetryStrategy)
     * @see RetryStrategy
     */
    @Override
    public boolean onStopJob(JobParameters job) {
        if (mLocationsFetchTask != null){
            mLocationsFetchTask.cancel(true);
        }

        Log.d("FirebaseJobService", "onStopJob: stopped");

        return true;
    }
}
