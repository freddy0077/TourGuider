package com.evergreen.apps.tourguideapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Place implements Parcelable {

    private String id;
    private String name;
    private Location location;
    private List<Category> categories;

    public Place(String id, String name, Location location, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.categories = categories;
    }

    public Place(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation(){
        return location;
    }

    public void setLocation( Location location){
        this.location = location;
    }

    public List<Category> getCategories(){
        return categories;
    }

    public void setCategories(List<Category> categories){
        this.categories = categories;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.location, flags);
        dest.writeList(this.categories);
    }

    protected Place(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.categories = new ArrayList<Category>();
        in.readList(this.categories, Category.class.getClassLoader());
    }

    public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel source) {
            return new Place(source);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
}
