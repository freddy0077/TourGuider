package com.evergreen.apps.tourguideapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class VenueResponse implements Parcelable {

    private List<Place> venues;

    private List<Category> categories;

    private Photos photos;

    private Tip tips;

    public VenueResponse() {
    }

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public Tip getTips() {
        return tips;
    }

    public void setTips(Tip tips) {
        this.tips = tips;
    }

    public List<Place> getVenues() {
        return venues;
    }

    public List<Category> getCategories(){
        return categories;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.venues);
        dest.writeTypedList(this.categories);
//        dest.writeParcelable(this.photos, flags);
        dest.writeParcelable(this.tips, flags);
    }


    protected VenueResponse(Parcel in) {
        this.venues = in.createTypedArrayList(Place.CREATOR);
        this.categories = in.createTypedArrayList(Category.CREATOR);
        this.photos = in.readParcelable(Photos.class.getClassLoader());
        this.tips = in.readParcelable(Tip.class.getClassLoader());
    }

    public static final Creator<VenueResponse> CREATOR = new Creator<VenueResponse>() {
        @Override
        public VenueResponse createFromParcel(Parcel source) {
            return new VenueResponse(source);
        }

        @Override
        public VenueResponse[] newArray(int size) {
            return new VenueResponse[size];
        }
    };
}