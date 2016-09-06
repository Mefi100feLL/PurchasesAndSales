package com.PopCorp.Purchases.data.dto;

import com.PopCorp.Purchases.data.model.Category;

import java.util.List;

public class CategoriesDTO extends UniversalDTO<List<Category>> {

    public CategoriesDTO(boolean error, String message, List<Category> result) {
        super(error, message);
    }
}
