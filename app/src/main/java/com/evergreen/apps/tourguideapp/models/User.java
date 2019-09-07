package com.evergreen.apps.tourguideapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private int id;
    private String firstName;
    private String gender;
    private Photos photo;
    private String visibility;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Photos getPhoto() {
        return photo;
    }

    public void setPhoto(Photos photo) {
        this.photo = photo;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setPhoto(String visibility) {
        this.visibility = visibility;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.gender);
//        dest.writeParcelable(this.photo, flags);
        dest.writeString(this.visibility);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readInt();
        this.firstName = in.readString();
        this.gender = in.readString();
        this.photo = in.readParcelable(Photos.class.getClassLoader());
        this.visibility = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
