package com.PopCorp.Purchases.domain.repository.skidkaonline;

import com.PopCorp.Purchases.data.model.skidkaonline.City;

import java.util.List;

import rx.Observable;

/**
 * Created by Александр on 06.07.2016.
 */
public interface CityRepository {

    Observable<List<City>> getData();
}
