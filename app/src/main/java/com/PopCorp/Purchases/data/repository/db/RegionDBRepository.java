package com.PopCorp.Purchases.data.repository.db;

import com.PopCorp.Purchases.data.dao.RegionDAO;
import com.PopCorp.Purchases.data.model.Region;
import com.PopCorp.Purchases.domain.repository.RegionRepository;

import java.util.List;

import rx.Observable;

public class RegionDBRepository implements RegionRepository {

    private RegionDAO dao = new RegionDAO();

    @Override
    public Observable<List<Region>> getData() {
        return Observable.just(dao.getAllRegions());
    }

    public void addAllRegions(List<Region> regions) {
        dao.addAllRegions(regions);
    }
}
