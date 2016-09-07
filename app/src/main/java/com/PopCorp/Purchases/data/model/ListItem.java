package com.PopCorp.Purchases.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.PopCorp.Purchases.presentation.utils.DecoratorBigDecimal;

import java.math.BigDecimal;

public class ListItem implements Parcelable, ContentSame<ListItem> {

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
        this.count = new BigDecimal(count != null && !count.isEmpty() ? count : "0");
        this.edizm = edizm;
        this.coast = new BigDecimal(coast != null && !coast.isEmpty() ? coast : "0");
        this.category = category;
        if (shop == null){
            shop = "";
        }
        this.shop = shop;
        if (comment == null){
            comment = "";
        }
        this.comment = comment;
        this.buyed = buyed;
        this.important = important;
        this.sale = sale;
    }

    @Override
    public String toString() {
        String result = "";
        /*result += "id=" + id + ", ";
        result += "listId=" + listId + ", ";*/
        result += "name=" + name + ", ";
        /*result += "count=" + count + ", ";
        result += "edizm=" + edizm + ", ";
        result += "coast=" + coast + ", ";*/
        result += "category=" + category + ", ";
        /*result += "shop=" + shop + ", ";
        result += "comment=" + comment + ", ";*/
        result += "buyed=" + buyed + ", ";
        /*result += "important=" + important;*/
        return result;
    }

    @Override
    public boolean equalsContent(ListItem object) {
        if (equals(object)) {
            if (name.equals(object.getName()) &&
                    count.equals(object.getCount()) &&
                    edizm.equals(object.getEdizm()) &&
                    coast.equals(object.getCoast()) &&
                    comment.equals(object.getComment()) &&
                    buyed == object.isBuyed() &&
                    important == object.isImportant()) {
                if ((category != null && object.getCategory() != null && category.equals(object.getCategory())) || (category == null && object.getCategory() == null)) {
                    if (shop != null && object.getShop() != null && shop.equals(object.getShop()) || (shop == null && object.getShop() == null)) {
                        if (sale != null && object.getSale() != null) {
                            if (sale.equals(object.getSale())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ListItem)) return false;
        ListItem item = (ListItem) object;
        return name.equals(item.getName());
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
        dest.writeString(count.toString());
        dest.writeString(edizm);
        dest.writeString(coast.toString());
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
        return DecoratorBigDecimal.decor(count);
    }

    public String getCoastString() {
        return DecoratorBigDecimal.decor(coast);
    }

    public void setCount(String count) {
        this.count = new BigDecimal(count);
    }

    public void setCoast(String coast) {
        this.coast = new BigDecimal(coast);
    }

}
