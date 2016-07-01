package com.PopCorp.Purchases.presentation.decorator;

import com.PopCorp.Purchases.data.model.Category;
import com.PopCorp.Purchases.data.model.Sale;

public class SaleCategoryDecorator extends SaleDecorator {

    private Category category;

    public SaleCategoryDecorator(Sale sale, boolean header, Category category){
        super(sale, header);
        setCategory(category);
    }

    @Override
    public String getName() {
        return category.getName();
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory(){
        return category;
    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof SaleCategoryDecorator)) return false;

        SaleCategoryDecorator another = (SaleCategoryDecorator) object;
        if (isHeader() && another.isHeader()){
            return (getCategory().equals(another.getCategory()));
        }
        if (!(isHeader() || another.isHeader())){
            return getSale().equals(another.getSale());
        }
        return false;
    }
}
