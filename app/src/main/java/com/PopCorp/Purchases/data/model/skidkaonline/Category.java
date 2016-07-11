package com.PopCorp.Purchases.data.model.skidkaonline;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Set;

public class Category implements Parcelable {

    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;
    @SerializedName("cityUrl")
    private String cityUrl;
    @SerializedName("cityId")
    private int cityId;

    private static Set<Category> categories = new HashSet<>();

    public static Category create(String name, String url, String cityUrl, int cityId) {
        for (Category category : categories) {
            if (category.getUrl().equals(url) && category.getCityId() == cityId) {
                return category;
            }
        }
        Category category = new Category(name, url, cityUrl, cityId);
        categories.add(category);
        return category;
    }

    private Category(String name, String url, String cityUrl, int cityId) {
        this.name = name;
        this.url = url;
        this.cityUrl = cityUrl;
        this.cityId = cityId;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Category)) return false;
        Category category = (Category) object;
        return url.equals(category.getUrl()) && cityId == category.getCityId();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(cityUrl);
        dest.writeInt(cityId);
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
        this.name = parcel.readString();
        this.url = parcel.readString();
        this.cityUrl = parcel.readString();
        this.cityId = parcel.readInt();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCityUrl() {
        return cityUrl;
    }

    public void setCityUrl(String cityUrl) {
        this.cityUrl = cityUrl;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
