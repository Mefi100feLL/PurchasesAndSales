package com.PopCorp.Purchases.data.model.skidkaonline;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Александр on 06.07.2016.
 */
public class Category {

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
