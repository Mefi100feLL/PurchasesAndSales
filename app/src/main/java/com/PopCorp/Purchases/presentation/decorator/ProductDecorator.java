package com.PopCorp.Purchases.presentation.decorator;

import com.PopCorp.Purchases.data.model.ContentSame;
import com.PopCorp.Purchases.data.model.ListItemCategory;
import com.PopCorp.Purchases.data.model.Product;

import java.util.Arrays;

public class ProductDecorator implements ContentSame<ProductDecorator> {

    private String name;
    private boolean header;
    private Product item;
    private ListItemCategory category;

    public ProductDecorator(String name, boolean header, Product item, ListItemCategory category) {
        this.name = name;
        this.header = header;
        this.item = item;
        this.category = category;
    }

    @Override
    public boolean equalsContent(ProductDecorator decorator) {
        boolean result = Arrays.equals(getFields(), decorator.getFields());
        if ((!header && !decorator.isHeader()) && result){
            result = Arrays.equals(getItemFields(), decorator.getItemFields());
        }
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProductDecorator)) return false;
        ProductDecorator decorator = (ProductDecorator) object;
        boolean result = false;
        if (header && decorator.isHeader()){
            if (category != null && decorator.getCategory() != null) {
                result = category.equals(decorator.getCategory());
            } else if (category == null && decorator.getCategory() == null){
                result = name.equals(decorator.getName());
            }
        } else if (!header && !decorator.isHeader()){
            result = item.equals(decorator.getItem());
        }
        return result;
    }

    public String[] getFields(){
        String categoryId = "";
        if (category != null){
            categoryId = String.valueOf(category.getId());
        }
        return new String[] {
                name,
                String.valueOf(header),
                categoryId
        };
    }

    public String[] getItemFields(){
        return new String[] {
                item.getName(),
                item.getCountString(),
                String.valueOf(item.getCategory().getId())
        };
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public Product getItem() {
        return item;
    }

    public void setItem(Product item) {
        this.item = item;
    }

    public ListItemCategory getCategory() {
        return category;
    }

    public void setCategory(ListItemCategory category) {
        this.category = category;
    }
}
