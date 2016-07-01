package com.PopCorp.Purchases.data.comparator;

import com.PopCorp.Purchases.data.model.Region;

import java.util.Comparator;

public class RegionComparator implements Comparator<Region> {

    @Override
    public int compare(Region lhs, Region rhs) {
        return lhs.getName().compareToIgnoreCase(rhs.getName());
    }
}
