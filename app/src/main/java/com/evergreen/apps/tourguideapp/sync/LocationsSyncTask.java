package com.evergreen.apps.tourguideapp.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.evergreen.apps.tourguideapp.models.APIResponse;
import com.evergreen.apps.tourguideapp.models.Category;
import com.evergreen.apps.tourguideapp.models.Place;
import com.evergreen.apps.tourguideapp.providers.LocationProvider;
import com.evergreen.apps.tourguideapp.utils.ApiInterface;
import com.evergreen.apps.tourguideapp.utils.Config;
import com.evergreen.apps.tourguideapp.utils.Constants;
import com.evergreen.apps.tourguideapp.utils.FoursquareJsonUtils;
import com.evergreen.apps.tourguideapp.utils.LocationPreferences;
import com.evergreen.apps.tourguideapp.utils.NotificationUtil;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

public class LocationsSyncTask {

    private static final String TAG = LocationsSyncTask.class.getSimpleName();
    private static ApiInterface apiInterface;


    synchronized public static void syncLocations(Context context){

        apiInterface = Constants.getRetrofit().create(ApiInterface.class);

        try{

            ContentResolver locationsContentResolver = context.getContentResolver();

            //Delete old data;
            locationsContentResolver.delete(LocationProvider.URI_LOCATION, null, null);

            locationsContentResolver.delete(LocationProvider.URI_CATEGORY, null, null);

            insertLocations(context, locationsContentResolver, "");

            insertCategories(locationsContentResolver);

            if (LocationPreferences.locationCategorySectionSet(context)){

                syncCategoryLocations(context);

                LocationPreferences.resetLocationCategorySection(context);
            }

            NotificationUtil.notifyUserOfNewUpdates(context);

        }catch (Exception e){
            Log.e(TAG,"Sync Task Url: " + e.getMessage());
        }
    }


    synchronized public static void syncCategoryLocations(Context context){

        apiInterface = Constants.getRetrofit().create(ApiInterface.class);

        try{

            ContentResolver locationsContentResolver = context.getContentResolver();

            //Delete old data;
//            locationsContentResolver.delete(LocationProvider.URI_LOCATION, null, null);

            String section = LocationPreferences.getCategorySection(context);

            insertLocations(context, locationsContentResolver, section);

//            locationsContentResolver.update(LocationProvider.URI_CATEGORY,  values, null, null);

        }catch (Exception e){
            Log.d(TAG, "syncLocations: " + e.getMessage());
        }
    }

    private static void insertLocations(Context context, ContentResolver locationsContentResolver, String s) {

        try {

            String coordinates = "";
            String location = "";

            if (LocationPreferences.isLocationLatLonAvailable(context)){

                double latitude = LocationPreferences.getLocationCoordinates(context)[0];
                double longitude = LocationPreferences.getLocationCoordinates(context)[1];

                coordinates = latitude + ","+ longitude;
            }

            Call<APIResponse> call = apiInterface
                    .getResponsesCall(Config.VERSION, Config.CLIENT_ID, Config.CLIENT_SECRET, s, location, coordinates);

            APIResponse apiResponse = null;

            try {
                apiResponse = call.execute().body();

            } catch (IOException exception) {
                exception.printStackTrace();

                Log.i(TAG, "SyncTask insertLocations: "+ exception.getMessage());
            }

            if (apiResponse != null) {

                List<Place> placeList = apiResponse.getResponse().getVenues();

                ContentValues[] locationContentValues = FoursquareJsonUtils.getFourSquareContentValuesFromJson(placeList, context);

                locationsContentResolver.bulkInsert(LocationProvider.URI_LOCATION, locationContentValues);

                NotificationUtil.notifyUserOfNewUpdates(context);
            }
        }catch(Exception e){

            Log.e(TAG,  e.getMessage());
        }
    }

    private static void insertCategories( ContentResolver locationsContentResolver) {
        Call<APIResponse> call = apiInterface
                .getCategoriesCall(Config.VERSION, Config.CLIENT_ID, Config.CLIENT_SECRET);

        APIResponse apiResponse = null;

        try {
            apiResponse = call.execute().body();

        } catch (IOException exception) {
            exception.printStackTrace();
            Log.e(TAG, exception.getMessage());
        }

        if (apiResponse != null) {

            List<Category> categoryList = apiResponse.getResponse().getCategories();

            ContentValues[] categoryContentValues = FoursquareJsonUtils.getFourSquareCategoriesFromJson(categoryList);

            locationsContentResolver.bulkInsert(LocationProvider.URI_CATEGORY, categoryContentValues);
        }
    }
}
