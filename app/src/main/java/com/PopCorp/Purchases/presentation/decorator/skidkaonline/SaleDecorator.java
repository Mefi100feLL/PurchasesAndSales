package com.PopCorp.Purchases.presentation.decorator.skidkaonline;

import com.PopCorp.Purchases.data.model.skidkaonline.Sale;

public class SaleDecorator {

    private String name;
    private boolean header;
    private Sale sale;
    private String catalog;

    public SaleDecorator(String name, boolean header, Sale sale, String catalog) {
        this.name = name;
        this.header = header;
        this.sale = sale;
        this.catalog = catalog;
    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof SaleDecorator)) return false;

        SaleDecorator another = (SaleDecorator) object;
        if (isHeader() && another.isHeader()){
            return (getCatalog().equals(another.getCatalog()));
        }
        if (!(isHeader() || another.isHeader())){
            return getSale().equals(another.getSale());
        }
        return false;
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

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }
}
