package com.evergreen.apps.tourguideapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public final class LocationPreferences {

    public static final String PREF_COORD_LAT = "coord_lat";
    public static final String PREF_COORD_LNG = "coord_long";

    public static final String PREF_SECTION = "section";


    public static void setLocationDetails(Context context, double lat, double lon){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        Editor editor = sharedPreferences.edit();

        editor.putLong(PREF_COORD_LAT, Double.doubleToRawLongBits(lat));
        editor.putLong(PREF_COORD_LNG, Double.doubleToRawLongBits(lon));
        editor.apply();
    }


    public static void resetLocationCoordinates(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();


        editor.remove(PREF_COORD_LAT);
        editor.remove(PREF_COORD_LNG);
        editor.apply();
    }

    public static double[] getLocationCoordinates(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        double[] preferredCoordinates = new double[2];

        /*
         * This is a hack we have to resort to since you can't store doubles in SharedPreferences.
         *
         * Double.doubleToLongBits returns an integer corresponding to the bits of the given
         * IEEE 754 double precision value.
         *
         * Double.longBitsToDouble does the opposite, converting a long (that represents a double)
         * into the double itself.
         */
        preferredCoordinates[0] = Double
                .longBitsToDouble(sp.getLong(PREF_COORD_LAT, Double.doubleToRawLongBits(0.0)));
        preferredCoordinates[1] = Double
                .longBitsToDouble(sp.getLong(PREF_COORD_LNG, Double.doubleToRawLongBits(0.0)));

        return preferredCoordinates;
    }

    public static boolean isLocationLatLonAvailable(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        boolean spContainLatitude = sp.contains(PREF_COORD_LAT);
        boolean spContainLongitude = sp.contains(PREF_COORD_LNG);

        boolean spContainBothLatitudeAndLongitude = false;
        if (spContainLatitude && spContainLongitude) {
            spContainBothLatitudeAndLongitude = true;
        }

        return spContainBothLatitudeAndLongitude;
    }


    public static void setCategorySection(Context context, String section){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        Editor editor = sharedPreferences.edit();

        editor.putString(PREF_SECTION, section);
        editor.apply();
    }

    public static void resetLocationCategorySection(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();

        editor.remove(PREF_SECTION);
        editor.apply();
    }

    public static boolean locationCategorySectionSet(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean spContainsSection = false;

        if (sharedPreferences.contains(PREF_SECTION)){
            spContainsSection = true;
        }

        return spContainsSection;
    }

    public static String getCategorySection(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

       return sp.getString(PREF_SECTION, "");
    }

}
