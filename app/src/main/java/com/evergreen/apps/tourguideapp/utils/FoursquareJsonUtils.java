package com.evergreen.apps.tourguideapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.evergreen.apps.tourguideapp.models.Category;
import com.evergreen.apps.tourguideapp.models.Location;
import com.evergreen.apps.tourguideapp.models.Place;

import java.util.List;

public final class FoursquareJsonUtils {

    private static final String TAG = FoursquareJsonUtils.class.getSimpleName();


    public static ContentValues[] getFourSquareContentValuesFromJson(List<Place> placeList, Context context) {

        ApiInterface apiInterface = Constants.getRetrofit().create(ApiInterface.class);

        ContentValues[] locationContentValues = new ContentValues[placeList.size()];

        for (int i = 0; i < placeList.size(); i++) {

            try {

                ContentValues locationValues = new ContentValues();
                locationValues.put(Location.COLUMN_NAME, placeList.get(i).getName());
                locationValues.put(Location.COLUMN_LOCATION_ID, placeList.get(i).getId());
                locationValues.put(Location.COLUMN_LAT, placeList.get(i).getLocation().getLat());
                locationValues.put(Location.COLUMN_LNG, placeList.get(i).getLocation().getLng());
                locationValues.put(Location.COLUMN_ADDRESS, placeList.get(i).getLocation().getAddress());
                locationValues.put(Location.COLUMN_CITY, placeList.get(i).getLocation().getCity());
                locationValues.put(Location.COLUMN_CC, placeList.get(i).getLocation().getCc());
                locationValues.put(Location.COLUMN_STATE, placeList.get(i).getLocation().getState());
                locationValues.put(Location.COLUMN_COUNTRY, placeList.get(i).getLocation().getCountry());
                locationValues.put(Location.COLUMN_CATEGORY_ID, placeList.get(i).getCategories().get(0).getId());
                locationValues.put(Location.COLUMN_CATEGORY_NAME, placeList.get(i).getCategories().get(0).getName());
                locationValues.put(Location.COLUMN_CATEGORY_ICON, placeList.get(i).getCategories().get(0).getIcon().getFullIconUrl().toString());
                // locationValues.put(Location.COLUMN_PHOTO_URL, image_url_string);

                locationContentValues[i] = locationValues;

            } catch (IndexOutOfBoundsException ex) {
                Log.e(TAG, "Location Index Out of Bounds Exceptions " + ex.getMessage());
            }
        }

        return locationContentValues;
    }
    public static ContentValues[] getFourSquareCategoriesFromJson(List<Category> categoryList) {

        ContentValues[] categoryContentValues = new ContentValues[categoryList.size()];

        for (int i = 0; i < categoryList.size(); i++) {

            try {

                ContentValues categoryValues = new ContentValues();
                categoryValues.put(Category.COLUMN_CATEGORY_ID,    categoryList.get(i).getId());
                categoryValues.put(Category.COLUMN_NAME,  categoryList.get(i).getName());
                categoryValues.put(Category.COLUMN_PLURAL_NAME, categoryList.get(i).getPluralName());
                categoryValues.put(Category.COLUMN_SHORT_NAME, categoryList.get(i).getShortName());
                categoryValues.put(Category.COLUMN_ICON_URL, categoryList.get(i).getCategories().get(0).getIcon().getFullIconUrl().toString());

                categoryContentValues[i] = categoryValues;

            } catch (Exception e) {
                Log.d(TAG, "Category Index Out of Bounds Exceptions " + e.getMessage());
            }
        }

        return categoryContentValues;
    }
}
