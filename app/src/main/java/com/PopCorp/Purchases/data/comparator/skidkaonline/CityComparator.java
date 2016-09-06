package com.PopCorp.Purchases.data.comparator.skidkaonline;

import com.PopCorp.Purchases.data.model.skidkaonline.City;

import java.util.Comparator;

/**
 * Created by Александр on 06.07.2016.
 */
public class CityComparator implements Comparator<City> {

    @Override
    public int compare(City lhs, City rhs) {
        return lhs.getName().compareToIgnoreCase(rhs.getName());
    }
}
