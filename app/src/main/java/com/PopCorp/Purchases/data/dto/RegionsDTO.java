package com.PopCorp.Purchases.data.dto;

import com.PopCorp.Purchases.data.model.Region;

import java.util.List;

public class RegionsDTO extends UniversalDTO<List<Region>> {

    public RegionsDTO(boolean error, String message, List<Region> result) {
        super(error, message);
    }
}
