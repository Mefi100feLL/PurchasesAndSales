package com.PopCorp.Purchases.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Set;

public class Category implements Parcelable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private int type;

    @SerializedName("image")
    private String imageUrl;

    private boolean favorite;

    private static Set<Category> categories = new HashSet<>();

    public static Category create(int id, String name, int type, String imageUrl, boolean favorite) {
        for (Category category : categories) {
            if (category.getId() == id && category.getType() == type) {
                return category;
            }
        }
        Category category = new Category(id, name, type, imageUrl, favorite);
        categories.add(category);
        return category;
    }

    private Category(int id, String name, int type, String imageUrl, boolean favorite) {
        setId(id);
        setName(name);
        setType(type);
        setImageUrl(imageUrl);
        setFavorite(favorite);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Category)) return false;
        Category category = (Category) object;
        return (id == category.getId() && type == category.getType());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(type);
        dest.writeString(imageUrl);
        dest.writeString(String.valueOf(favorite));
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    private Category(Parcel parcel) {
        setId(parcel.readInt());
        setName(parcel.readString());
        setType(parcel.readInt());
        setImageUrl(parcel.readString());
        setFavorite(Boolean.valueOf(parcel.readString()));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
