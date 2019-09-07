package com.evergreen.apps.tourguideapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

@Entity(tableName = Location.TABLE_NAME)
public class Location implements Parcelable {

    public static final String TABLE_NAME = "locations";

//    public static final String COLUMN_ID   = BaseColumns._ID;
    public static final String COLUMN_ID   = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LOCATION_ID = "location_id";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_CROSS_STREET = "cross_street";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LNG = "lng";
    public static final String COLUMN_CC = "cc";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_CATEGORY_NAME = "category_name";
    public static final String COLUMN_CATEGORY_ICON = "category_icon";

    public static final String COLUMN_PHOTO_URL      = "photo_url";

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    private long id;

    @ColumnInfo(name= COLUMN_LOCATION_ID)
    private String locationId;

    @ColumnInfo( name = COLUMN_NAME)
    private String name;

    @ColumnInfo( name = COLUMN_ADDRESS)
    private String address;

    @ColumnInfo(name = COLUMN_CROSS_STREET)
    private String crossStreet;

    @ColumnInfo( name = COLUMN_LAT)
    private String lat;

    @ColumnInfo( name = COLUMN_LNG)
    private String lng;

    @Ignore
    private List labeledLatLngs;

    @ColumnInfo( name = COLUMN_CC)
    private String cc;

    @ColumnInfo( name = COLUMN_CITY)
    private String city;

    @ColumnInfo( name = COLUMN_STATE)
    private String state;

    @ColumnInfo( name = COLUMN_COUNTRY)
    private String country;

    private String formatted_address;

    @Ignore
    private List formattedAddress;

    @ColumnInfo(name = COLUMN_CATEGORY_ID)
    private String categoryId;

    @ColumnInfo( name = COLUMN_CATEGORY_NAME)
    private String categoryName;

    @ColumnInfo( name = COLUMN_CATEGORY_ICON)
    private String categoryIcon;

    @ColumnInfo( name = COLUMN_PHOTO_URL)
    private String photoUrl;

    public Location(){

    }

    public static Location fromContentValues(ContentValues values){

        final Location location = new Location();
//        if (values.containsKey(COLUMN_ID)){
//            location.id = values.getAsLong(COLUMN_ID);
//        }

        if (values.containsKey(COLUMN_NAME)){
            location.name = values.getAsString(COLUMN_NAME);
        }

        if (values.containsKey(COLUMN_ADDRESS)){
            location.address = values.getAsString(COLUMN_ADDRESS);
        }

        if (values.containsKey(COLUMN_LAT)){
            location.lat = values.getAsString(COLUMN_LAT);
        }

        if (values.containsKey(COLUMN_LNG)){
            location.lng = values.getAsString(COLUMN_LNG);
        }

        if (values.containsKey(COLUMN_LOCATION_ID)){
            location.locationId = values.getAsString(COLUMN_LOCATION_ID);
        }

        if (values.containsKey(COLUMN_CC)){
            location.cc = values.getAsString(COLUMN_CC);
        }

        if (values.containsKey(COLUMN_CITY)){
            location.city = values.getAsString(COLUMN_CITY);
        }

        if (values.containsKey(COLUMN_COUNTRY)){
            location.country = values.getAsString(COLUMN_COUNTRY);
        }

        if (values.containsKey(COLUMN_CATEGORY_ID)){
            location.categoryId = values.getAsString(COLUMN_CATEGORY_ID);
        }

        if (values.containsKey(COLUMN_CATEGORY_NAME)){
            location.categoryName = values.getAsString(COLUMN_CATEGORY_NAME);
        }

        if (values.containsKey(COLUMN_CATEGORY_ICON)){
            location.categoryIcon = values.getAsString(COLUMN_CATEGORY_ICON);
        }

        if (values.containsKey(COLUMN_PHOTO_URL)){
            location.photoUrl = values.getAsString(COLUMN_PHOTO_URL);
        }

        return location;
    }

    @NonNull
    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCrossStreet() {
        return crossStreet;
    }

    public void setCrossStreet(String crossStreet) {
        this.crossStreet = crossStreet;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public List getLabeledLatLngs() {
        return labeledLatLngs;
    }

    public void setLabeledLatLngs(List labeledLatLngs) {
        this.labeledLatLngs = labeledLatLngs;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List getFormattedAddress() {
        return formattedAddress;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }


    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setFormattedAddress(List formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    protected Location(Parcel in) {
        address = in.readString();
        crossStreet = in.readString();
        lat = in.readString();
        lng = in.readString();
        cc = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(crossStreet);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(cc);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(country);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };


}
