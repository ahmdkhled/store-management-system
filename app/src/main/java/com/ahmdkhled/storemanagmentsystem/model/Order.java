package com.ahmdkhled.storemanagmentsystem.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable{
    private int id;
    private String date;

    public Order(int id, String date) {
        this.id = id;
        this.date = date;
    }

    protected Order(Parcel in) {
        id = in.readInt();
        date = in.readString();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(date);
    }

    public Order() {
    }
}
