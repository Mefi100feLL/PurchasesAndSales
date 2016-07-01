package com.PopCorp.Purchases.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Set;

public class Region {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    private static Set<Region> regions = new HashSet<>();

    public static Region create(int id, String name){
        for (Region region : regions){
            if (region.getId() == id){
                return region;
            }
        }
        Region region = new Region(id, name);
        regions.add(region);
        return region;
    }

    private Region(int id, String name){
        setId(id);
        setName(name);
    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof Region)) return false;
        Region region = (Region) object;
        return getId() == region.getId();
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
}
