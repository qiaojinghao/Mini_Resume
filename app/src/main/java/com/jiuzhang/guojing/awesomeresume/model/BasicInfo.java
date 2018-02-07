package com.jiuzhang.guojing.awesomeresume.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.jiuzhang.guojing.awesomeresume.util.BirthUtils;

import java.util.Date;

public class BasicInfo implements Parcelable {

    public String name;

    public Date birth;

    public String phoneNum;

    public String email;

    public Uri imageUri;

    public BasicInfo() {

    }

    protected BasicInfo(Parcel in) {
        name = in.readString();
        birth = BirthUtils.stringToBirth(in.readString());
        phoneNum = in.readString();
        email = in.readString();
        imageUri = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        if(birth!=null)
        dest.writeString(BirthUtils.birthToString(birth));
        dest.writeString(phoneNum);
        dest.writeString(email);
        dest.writeParcelable(imageUri, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BasicInfo> CREATOR = new Creator<BasicInfo>() {
        @Override
        public BasicInfo createFromParcel(Parcel in) {
            return new BasicInfo(in);
        }

        @Override
        public BasicInfo[] newArray(int size) {
            return new BasicInfo[size];
        }
    };
}
