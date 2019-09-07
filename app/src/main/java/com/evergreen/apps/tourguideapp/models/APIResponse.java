package com.evergreen.apps.tourguideapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIResponse implements Parcelable {

    @SerializedName("response")
    @Expose
    public VenueResponse response;

    public APIResponse() {
    }

    public VenueResponse getResponse() {
        return response;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.response, flags);
    }


    protected APIResponse(Parcel in) {
        this.response = in.readParcelable(VenueResponse.class.getClassLoader());
    }

    public static final Parcelable.Creator<APIResponse> CREATOR = new Parcelable.Creator<APIResponse>() {
        @Override
        public APIResponse createFromParcel(Parcel source) {
            return new APIResponse(source);
        }

        @Override
        public APIResponse[] newArray(int size) {
            return new APIResponse[size];
        }
    };
}