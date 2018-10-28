package com.qc.language.ui.gre.databean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by beckett on 2018/9/19.
 */
public class AppData implements Parcelable {

    private String name;

    private String imageurl;

    private String type;

    private String bundlename;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBundlename() {
        return bundlename;
    }

    public void setBundlename(String bundlename) {
        this.bundlename = bundlename;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.imageurl);
        dest.writeString(this.type);
        dest.writeString(this.bundlename);
    }

    public AppData() {
    }

    protected AppData(Parcel in) {
        this.name = in.readString();
        this.imageurl = in.readString();
        this.type = in.readString();
        this.bundlename = in.readString();
    }

    public static final Creator<AppData> CREATOR = new Creator<AppData>() {
        @Override
        public AppData createFromParcel(Parcel source) {
            return new AppData(source);
        }

        @Override
        public AppData[] newArray(int size) {
            return new AppData[size];
        }
    };
}
