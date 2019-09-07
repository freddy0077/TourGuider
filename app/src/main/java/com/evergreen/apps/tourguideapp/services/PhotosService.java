package com.evergreen.apps.tourguideapp.services;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.evergreen.apps.tourguideapp.models.APIResponse;
import com.evergreen.apps.tourguideapp.utils.ApiInterface;
import com.evergreen.apps.tourguideapp.utils.Config;
import com.evergreen.apps.tourguideapp.utils.Constants;

import java.io.IOException;

import retrofit2.Call;

public class PhotosService extends IntentService {

    private String TAG = PhotosService.class.getSimpleName();
    private String mVenueId;
    ApiInterface apiInterface;

    public PhotosService() {
        super("Photos Service");

//        mVenueId = venue_id;

        apiInterface = Constants.getRetrofit().create(ApiInterface.class);

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Call<APIResponse> call = apiInterface.getAllVenuePhotos(mVenueId, Config.VERSION, Config.CLIENT_ID,
                Config.CLIENT_SECRET, Constants.VENUE_GROUP,Constants.LIMIT_10);

        APIResponse apiResponse;

        try{

            apiResponse = call.execute().body();

        }catch (IOException e){
            e.printStackTrace();
            Log.i(TAG, "onHandleIntent: " + e.getMessage());
            return;
        }

        Intent photosIntent = new Intent("PHOTOS_SERVICE");
        photosIntent.putExtra("PHOTOS_SERVICE_PAYLOAD", apiResponse);

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(photosIntent);
    }
}
