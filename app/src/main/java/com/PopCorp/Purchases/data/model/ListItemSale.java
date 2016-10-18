package com.PopCorp.Purchases.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ListItemSale implements Parcelable {

    private long id;
    private String image;
    private int saleId;
    private String periodStart;
    private String periodEnd;

    public ListItemSale(long id, String image, int saleId, String periodStart, String periodEnd) {
        this.id = id;
        this.image = image;
        this.saleId = saleId;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ListItemSale)) return false;
        ListItemSale listItemSale = (ListItemSale) object;
        return id == listItemSale.getId();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(image);
        dest.writeInt(saleId);
        dest.writeString(periodStart);
        dest.writeString(periodEnd);
    }

    public static final Creator<ListItemSale> CREATOR = new Creator<ListItemSale>() {
        public ListItemSale createFromParcel(Parcel in) {
            return new ListItemSale(in);
        }

        public ListItemSale[] newArray(int size) {
            return new ListItemSale[size];
        }
    };

    private ListItemSale(Parcel parcel) {
        this.id = parcel.readLong();
        this.image = parcel.readString();
        this.saleId = parcel.readInt();
        this.periodStart = parcel.readString();
        this.periodEnd = parcel.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(String periodStart) {
        this.periodStart = periodStart;
    }

    public String getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(String periodEnd) {
        this.periodEnd = periodEnd;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }
}
