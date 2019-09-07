package com.evergreen.apps.tourguideapp.utils;

import com.evergreen.apps.tourguideapp.models.APIResponse;
import com.evergreen.apps.tourguideapp.models.Location;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class Constants {


    public static final String[] MAIN_LOCATION_PROJECTION = {
            Location.COLUMN_NAME,
            Location.COLUMN_LAT,
            Location.COLUMN_LNG,
            Location.COLUMN_ADDRESS,
            Location.COLUMN_CITY,
            Location.COLUMN_CATEGORY_NAME,
            Location.COLUMN_CATEGORY_ID,
            Location.COLUMN_CATEGORY_ICON,
    };

    public static final String[] MAIN_CATEGORY_PROJECTION = {
            Location.COLUMN_NAME
    };

    public static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationaddress";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";
    public static final String WIDGET_RESULT = "wigdet_lists";

    private static String BASE_URL = "https://api.foursquare.com/v2/";
    public static final String DETAILS_CONTENT    = "DETAILS_CONTENT";
    public static final String GOOGLE_MAP_PACKAGE = "com.google.android.apps.maps";
    public static final String DEFAULT_LOCATION   = "";
    public static final String VENUE_PHOTOS       = "photos";
    public static final String VENUE_GROUP        = "venue";
    public static final String VENUE_SORT        = "popular";
    public static final int    LIMIT_10           =  10;
    public static final int    LIMIT_100           =  100;

    public static final String SHARED_TRANSITION_NAME = "profile";

    public static final String SHARED_PREFERENCES = "Preferences";

    public static final String SECTION = "section";

    private static Retrofit retrofit = null;


    public static Retrofit getRetrofit() {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public static String getImage(String locationId, ApiInterface apiInterface){

        Call<APIResponse> call = apiInterface.getVenuePhoto(locationId, Config.VERSION, Config.CLIENT_ID, Config.CLIENT_SECRET, Constants.VENUE_GROUP, Constants.LIMIT_10);

        APIResponse apiResponse = null;

        try {
            apiResponse = call.execute().body();

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return apiResponse != null ? apiResponse.getResponse().getPhotos().getItems().get(0).getPhotoUrl() : null;
    }

}
