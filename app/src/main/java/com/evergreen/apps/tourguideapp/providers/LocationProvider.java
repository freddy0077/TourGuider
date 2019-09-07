package com.evergreen.apps.tourguideapp.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.evergreen.apps.tourguideapp.data.CategoryDaoAccess;
import com.evergreen.apps.tourguideapp.data.DaoAccess;
import com.evergreen.apps.tourguideapp.data.LocationDatabase;
import com.evergreen.apps.tourguideapp.models.Category;
import com.evergreen.apps.tourguideapp.models.Location;

public class LocationProvider extends ContentProvider {

    private static final String TAG = LocationProvider.class.getSimpleName();

    public static final String AUTHORITY = "com.evergreen.apps.tourguideapp.provider";

    public static final Uri URI_LOCATION = Uri.parse(
            "content://" + AUTHORITY + "/" + Location.TABLE_NAME
    );

    public static final Uri URI_CATEGORY = Uri.parse(
            "content://" + AUTHORITY + "/" + Category.TABLE_NAME
    );

    private static final int CODE_LOCATION_DIR = 1;

    private static final int CODE_LOCATION_ITEM = 2;

    private static final int CODE_CATEGORY_DIR = 3;

    private static final int CODE_CATEGORY_ITEM = 4;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, Location.TABLE_NAME, CODE_LOCATION_DIR);
        MATCHER.addURI(AUTHORITY, Location.TABLE_NAME + "/*", CODE_LOCATION_ITEM);

        MATCHER.addURI(AUTHORITY, Category.TABLE_NAME, CODE_CATEGORY_DIR);
        MATCHER.addURI(AUTHORITY, Category.TABLE_NAME + "/*", CODE_CATEGORY_ITEM);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final int code = MATCHER.match(uri);
        final Cursor cursor;
        final Context context = getContext();
        LocationDatabase locationDatabase = LocationDatabase.getInstance(context);

        DaoAccess location = locationDatabase.daoAccess();
        CategoryDaoAccess categoryDaoAccess = locationDatabase.categoryDaoAccess();

        switch(code){
            case CODE_LOCATION_DIR:
                cursor = location.fetchLocations();
                break;

            case CODE_LOCATION_ITEM:

                if (ContentUris.parseId(uri) == 0) {
                    cursor = location.fetchRandomLocation();

                } else if(ContentUris.parseId(uri) == 1011){

                    cursor = location.fetchRandomLocations();

                }else {
                    cursor = location.fetchOneLocationById(ContentUris.parseId(uri));
                }
                break;

            case CODE_CATEGORY_DIR:
                cursor = categoryDaoAccess.fetchCategories();
                break;

            case CODE_CATEGORY_ITEM:
                cursor = categoryDaoAccess.fetchOneCategoryByIdCursor(ContentUris.parseId(uri));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(context.getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch(MATCHER.match(uri)){
            case CODE_LOCATION_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + Location.TABLE_NAME;

            case CODE_LOCATION_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + Location.TABLE_NAME;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri,  ContentValues contentValues) {
        switch(MATCHER.match(uri)){
            case CODE_LOCATION_DIR:
                final Context context = getContext();
                if (context == null){
                    return null;
                }

                final long id = LocationDatabase.getInstance(context)
                        .daoAccess()
                        .insert(Location.fromContentValues(contentValues));
                context.getContentResolver().notifyChange(uri, null);

                return ContentUris.withAppendedId(uri, id);

            case CODE_LOCATION_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int code = MATCHER.match(uri);
        Context context = getContext();

        LocationDatabase locationDatabase = LocationDatabase.getInstance(context);

        int rowsInserted = 0;

        switch(code){
            case CODE_LOCATION_DIR:
                rowsInserted = bulkInsertion(uri, values, locationDatabase, rowsInserted,"location");
                break;

            case CODE_CATEGORY_DIR:
                rowsInserted = bulkInsertion(uri, values, locationDatabase, rowsInserted, "category");
                break;

            default:
                    return super.bulkInsert(uri, values);

        }

        return rowsInserted;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        Context context = getContext();

        LocationDatabase locationDatabase = LocationDatabase.getInstance(context);

        switch (MATCHER.match(uri)) {

            case CODE_LOCATION_DIR:

                if (context == null){
                    return 0;
                }

              locationDatabase.daoAccess().deleteAllLocations();

                return 1;

            case CODE_LOCATION_ITEM:

                if (context == null){
                    return 0;
                }

                final int count = locationDatabase.daoAccess()
                        .deleteLocationById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;

            case CODE_CATEGORY_DIR:

                if (context == null){
                    return 0;
                }

                locationDatabase.categoryDaoAccess().deleteAllCategories();

                return 1;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        switch(MATCHER.match(uri)){
            case CODE_CATEGORY_ITEM:
                final Context context = getContext();
                if (context == null){
                    return 0;
                }

                final int id = LocationDatabase.getInstance(context).categoryDaoAccess()
                        .setCategoryDownloadedById(Location.fromContentValues(contentValues).getId(),
                                Category.fromContentValues(contentValues).isCategory_downloaded());
                context.getContentResolver().notifyChange(uri, null);
                return id;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    private int bulkInsertion(@NonNull Uri uri, @NonNull ContentValues[] values, LocationDatabase locationDatabase, int rowsInserted, String type) {
        try {
            for (ContentValues value : values) {
                long _id = 0;

                if (type.equals("location")){

                    _id = locationDatabase.daoAccess().insert(Location.fromContentValues(value));

                }else if (type.equals("category")){

                    _id = locationDatabase.categoryDaoAccess().insert(Category.fromContentValues(value));
                }

                if (_id != -1) {

                    rowsInserted++;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Location bulkInsertion: " + e.getMessage());
            Log.d(TAG, "Location bulkInsertion: " + e.getStackTrace()[0].getLineNumber());
        }

        if (rowsInserted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsInserted;
    }
}
