package com.PopCorp.Purchases.data.dto;

import com.PopCorp.Purchases.data.model.Shop;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import rx.Observable;

public class ShopsDTO extends UniversalDTO<List<Shop>> {

    public ShopsDTO(boolean error, String message) {
        super(error, message);
    }

}
