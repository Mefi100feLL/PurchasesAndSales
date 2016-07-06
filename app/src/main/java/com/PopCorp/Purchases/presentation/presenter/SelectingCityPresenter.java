package com.PopCorp.Purchases.presentation.presenter;

import android.view.View;

import com.PopCorp.Purchases.data.callback.RecyclerCallback;
import com.PopCorp.Purchases.data.model.skidkaonline.City;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.domain.interactor.skidkaonline.CityInteractor;
import com.PopCorp.Purchases.presentation.view.moxy.SelectingCityView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

@InjectViewState
public class SelectingCityPresenter extends MvpPresenter<SelectingCityView> implements RecyclerCallback<City> {

    private CityInteractor interactor = new CityInteractor();

    private ArrayList<City> objects = new ArrayList<>();

    private City selectedCity;

    private String currentFilter = "";

    public SelectingCityPresenter(){
        getViewState().showProgress();
        loadData();
    }

    private void loadData(){
        interactor.getData()
                .subscribe(new Observer<List<City>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().refreshing(false);
                        e.printStackTrace();
                        getViewState().showSnackBar(ErrorManager.getErrorResource(e));
                    }

                    @Override
                    public void onNext(List<City> cities) {
                        getViewState().refreshing(false);
                        if (cities.size() == 0) {
                            getViewState().showCitiesEmpty();
                        } else {
                            if (!PreferencesManager.getInstance().getCity().isEmpty()){
                                for (City city : cities){
                                    if (city.getId() == Integer.valueOf(PreferencesManager.getInstance().getCity())){
                                        selectedCity = city;
                                        getViewState().setSelectedCity(selectedCity);
                                        break;
                                    }
                                }
                            }
                            objects.clear();
                            objects.addAll(cities);
                            getViewState().showData();
                            getViewState().filter(currentFilter);
                        }
                    }
                });
    }

    @Override
    public void onItemClicked(View view, City item) {
        selectedCity = item;
        getViewState().setSelectedCity(selectedCity);
        getViewState().filter(currentFilter);
    }

    @Override
    public void onItemLongClicked(View view, City item) {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onEmpty(int stringRes, int drawableRes, int buttonRes, View.OnClickListener listener) {

    }

    @Override
    public void onEmpty(String string, int drawableRes, int buttonRes, View.OnClickListener listener) {

    }

    public void onRefresh() {
        getViewState().refreshing(true);
        loadData();
    }

    public ArrayList<City> getObjects() {
        return objects;
    }

    public void update() {
        getViewState().showProgress();
        loadData();
    }
}