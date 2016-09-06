package com.PopCorp.Purchases.data.repository.db.skidkaonline;

import com.PopCorp.Purchases.data.dao.skidkaonline.CityDAO;
import com.PopCorp.Purchases.data.model.skidkaonline.City;
import com.PopCorp.Purchases.domain.repository.skidkaonline.CityRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by Александр on 06.07.2016.
 */
public class CityDBRepository implements CityRepository {

    private CityDAO dao = new CityDAO();

    @Override
    public Observable<List<City>> getData() {
        return Observable.just(dao.getAllCities());
    }

    public void addAllCities(List<City> cities) {
        dao.addAllCities(cities);
    }
}
