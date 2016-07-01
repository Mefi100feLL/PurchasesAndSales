package com.PopCorp.Purchases.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class ListItem implements Parcelable {

    private long id;
    private long listId;
    private String name;
    private BigDecimal count;
    private String edizm;
    private BigDecimal coast;
    private ListItemCategory category;
    private String shop;
    private String comment;
    private boolean buyed;
    private boolean important;
    private ListItemSale sale;

    public ListItem(long id, long listId, String name, String count, String edizm, String coast, ListItemCategory category, String shop, String comment, boolean buyed, boolean important, ListItemSale sale) {
        this.id = id;
        this.listId = listId;
        this.name = name;
        this.count = new BigDecimal(count);
        this.edizm = edizm;
        this.coast = new BigDecimal(coast);
        this.category = category;
        this.shop = shop;
        this.comment = comment;
        this.buyed = buyed;
        this.important = important;
        this.sale = sale;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ListItem)) return false;
        ListItem item = (ListItem) object;
        return id == item.getId();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(listId);
        dest.writeString(name);
        dest.writeString(getCountString());
        dest.writeString(edizm);
        dest.writeString(getCoastString());
        dest.writeParcelable(category, flags);
        dest.writeString(shop);
        dest.writeString(comment);
        dest.writeString(String.valueOf(buyed));
        dest.writeString(String.valueOf(important));
        dest.writeParcelable(sale, flags);
    }

    public static final Creator<ListItem> CREATOR = new Creator<ListItem>() {
        public ListItem createFromParcel(Parcel in) {
            return new ListItem(in);
        }

        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };

    private ListItem(Parcel parcel) {
        this.id = parcel.readLong();
        this.listId = parcel.readLong();
        this.name = parcel.readString();
        this.count = new BigDecimal(parcel.readString());
        this.edizm = parcel.readString();
        this.coast = new BigDecimal(parcel.readString());
        this.category = parcel.readParcelable(ListItemCategory.class.getClassLoader());
        this.shop = parcel.readString();
        this.comment = parcel.readString();
        this.buyed = Boolean.parseBoolean(parcel.readString());
        this.important = Boolean.parseBoolean(parcel.readString());
        this.sale = parcel.readParcelable(ListItemSale.class.getClassLoader());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public String getEdizm() {
        return edizm;
    }

    public void setEdizm(String edizm) {
        this.edizm = edizm;
    }

    public BigDecimal getCoast() {
        return coast;
    }

    public void setCoast(BigDecimal coast) {
        this.coast = coast;
    }

    public ListItemCategory getCategory() {
        return category;
    }

    public void setCategory(ListItemCategory category) {
        this.category = category;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isBuyed() {
        return buyed;
    }

    public void setBuyed(boolean buyed) {
        this.buyed = buyed;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public ListItemSale getSale() {
        return sale;
    }

    public void setSale(ListItemSale sale) {
        this.sale = sale;
    }

    public String getCountString() {
        return count.toString();
    }

    public String getCoastString() {
        return coast.toString();
    }

    public void setCount(String count) {
        this.count = new BigDecimal(count);
    }

    public void setCoast(String coast) {
        this.coast = new BigDecimal(coast);
    }
}
