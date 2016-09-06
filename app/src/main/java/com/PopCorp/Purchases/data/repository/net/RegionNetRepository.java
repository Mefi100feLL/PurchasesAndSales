package com.PopCorp.Purchases.data.repository.net;

import com.PopCorp.Purchases.data.dto.UniversalDTO;
import com.PopCorp.Purchases.data.model.Region;
import com.PopCorp.Purchases.data.net.API;
import com.PopCorp.Purchases.data.net.APIFactory;
import com.PopCorp.Purchases.domain.repository.RegionRepository;

import java.util.List;

import rx.Observable;

public class RegionNetRepository implements RegionRepository {

    API api = APIFactory.getAPI();

    @Override
    public Observable<List<Region>> getData() {
        return api.getRegions()
                .flatMap(UniversalDTO::getData);
    }
}
