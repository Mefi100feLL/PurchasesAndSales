package com.PopCorp.Purchases.data.repository.net.skidkaonline;

import com.PopCorp.Purchases.data.model.skidkaonline.City;
import com.PopCorp.Purchases.data.net.API;
import com.PopCorp.Purchases.data.net.APIFactory;
import com.PopCorp.Purchases.domain.repository.skidkaonline.CityRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by Александр on 06.07.2016.
 */
public class CityNetRepository implements CityRepository {

    private API api = APIFactory.getAPI();

    @Override
    public Observable<List<City>> getData() {
        return api.getSkidkaonlineCities();
    }
}
