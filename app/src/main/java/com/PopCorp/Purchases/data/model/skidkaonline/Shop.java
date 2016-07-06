package com.PopCorp.Purchases.data.model.skidkaonline;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Александр on 06.07.2016.
 */
public class Shop {

    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;
    @SerializedName("image")
    private String image;
    @SerializedName("category")
    private Category category;
    @SerializedName("cityUrl")
    private String cityUrl;
    @SerializedName("cityId")
    private int cityId;

    private boolean favorite = false;

    public Shop(String name, String url, String image, Category category, String cityUrl, int cityId, boolean favorite) {
        this.name = name;
        this.url = url;
        this.image = image;
        this.category = category;
        this.cityUrl = cityUrl;
        this.cityId = cityId;
        this.favorite = favorite;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
