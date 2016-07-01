package com.PopCorp.Purchases.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Set;

public class Shop implements Parcelable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String imageUrl;

    @SerializedName("cityId")
    private int regionId;

    @SerializedName("countSales")
    private int countSales;

    private boolean favorite = false;


    private static Set<Shop> shops = new HashSet<>();

    public static Shop create(int id, String name, String imageUrl, int region, boolean favorite, int countSales){
        for (Shop shop : shops){
            if (shop.getId() == id){
                return shop;
            }
        }
        Shop shop = new Shop(id, name, imageUrl, region, favorite, countSales);
        shops.add(shop);
        return shop;
    }

    private Shop(int id, String name, String imageUrl, int regionId, boolean favorite, int countSales){
        setId(id);
        setName(name);
        setImageUrl(imageUrl);
        setRegionId(regionId);
        setFavorite(favorite);
        setCountSales(countSales);
    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof Shop)) return false;
        Shop shop = (Shop) object;
        return getId() == shop.getId() && regionId == shop.getRegionId();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeInt(regionId);
        dest.writeString(String.valueOf(favorite));
        dest.writeInt(countSales);
    }

    public static final Creator<Shop> CREATOR = new Creator<Shop>() {
        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

    private Shop(Parcel parcel) {
        setId(parcel.readInt());
        setName(parcel.readString());
        setImageUrl(parcel.readString());
        setRegionId(parcel.readInt());
        setFavorite(Boolean.valueOf(parcel.readString()));
        setCountSales(parcel.readInt());
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getCountSales() {
        return countSales;
    }

    public void setCountSales(int countSales) {
        this.countSales = countSales;
    }
}
