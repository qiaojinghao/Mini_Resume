package com.jiuzhang.guojing.awesomeresume.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.UUID;

/**
 * Created by Jinghao Qiao on 2018/2/7.
 */

public class Skill implements Parcelable{
    public String id;

    public String type;


    public List<String> names;

    public Skill() {
        id = UUID.randomUUID().toString();
    }

    protected Skill(Parcel in) {
        id = in.readString();
        type = in.readString();
        names = in.createStringArrayList();
    }

    public static final Creator<Skill> CREATOR = new Creator<Skill>() {
        @Override
        public Skill createFromParcel(Parcel in) {
            return new Skill(in);
        }

        @Override
        public Skill[] newArray(int size) {
            return new Skill[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(type);
        parcel.writeStringList(names);
    }
}
