package com.travelunion.image_selector.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class GalleryImage implements Parcelable {
    private  String name;
    private  String dateTaken;
    private int size = 0;
    private String path;

    public GalleryImage(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(dateTaken);
        dest.writeInt(size);
        dest.writeString(path);
    }

    public static final Parcelable.Creator<GalleryImage> CREATOR = new Parcelable.Creator<GalleryImage>() {
        public GalleryImage createFromParcel(Parcel in) {
            return new GalleryImage(in);
        }

        public GalleryImage[] newArray(int size) {
            return new GalleryImage[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private GalleryImage(Parcel in) {
        name = in.readString();
        dateTaken = in.readString();
        size = in.readInt();
        path = in.readString();
    }
}
