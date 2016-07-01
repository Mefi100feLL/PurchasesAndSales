package com.PopCorp.Purchases.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ListItemCategory implements Parcelable {

    private long id;
    private String name;
    private int color;

    public ListItemCategory(long id, String name, int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ListItemCategory)) return false;
        ListItemCategory category = (ListItemCategory) object;
        return (id == category.getId() && name.equals(category.getName()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeInt(color);
    }

    public static final Creator<ListItemCategory> CREATOR = new Creator<ListItemCategory>() {
        public ListItemCategory createFromParcel(Parcel in) {
            return new ListItemCategory(in);
        }

        public ListItemCategory[] newArray(int size) {
            return new ListItemCategory[size];
        }
    };

    private ListItemCategory(Parcel parcel) {
        this.id = parcel.readLong();
        this.name = parcel.readString();
        this.color = parcel.readInt();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
