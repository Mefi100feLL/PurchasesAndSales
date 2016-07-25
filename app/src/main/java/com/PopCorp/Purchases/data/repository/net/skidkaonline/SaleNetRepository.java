package com.PopCorp.Purchases.data.repository.net.skidkaonline;

import com.PopCorp.Purchases.data.dto.UniversalDTO;
import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.data.net.API;
import com.PopCorp.Purchases.data.net.APIFactory;
import com.PopCorp.Purchases.domain.repository.skidkaonline.SaleRepository;

import java.util.List;

import rx.Observable;

public class SaleNetRepository implements SaleRepository {

    private API api = APIFactory.getAPI();

    @Override
    public Observable<List<Sale>> getData(int cityId, String shopUrl) {
        return api.getSkidkaonlineSales(cityId, shopUrl)
                .flatMap(UniversalDTO::getData);
    }

    @Override
    public Observable<Sale> getSale(int cityId, int saleId) {
        return api.getSkidkaonlineSale(cityId, saleId)
                .flatMap(UniversalDTO::getData);
    }
}
