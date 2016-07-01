package com.PopCorp.Purchases.domain.repository;

import com.PopCorp.Purchases.data.model.Region;

import java.util.List;

import rx.Observable;

public interface RegionRepository {

    Observable<List<Region>> getData();
}
