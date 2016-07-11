package com.PopCorp.Purchases.data.model.skidkaonline;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Александр on 06.07.2016.
 */
public class City {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("region")
    private String region;
    @SerializedName("url")
    private String url;

    public City(int id, String name, String region, String url) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.url = url;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof City)) return false;
        City city = (City) object;
        return id == city.getId();
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
