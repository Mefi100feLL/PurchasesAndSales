package com.PopCorp.Purchases.presentation.view.moxy;

import com.PopCorp.Purchases.data.model.skidkaonline.City;

public interface SelectingCityView extends SampleDataView {
    void showCitiesEmpty();

    void filter(String filter);

    void setSelectedCity(City selectedCity);
}
