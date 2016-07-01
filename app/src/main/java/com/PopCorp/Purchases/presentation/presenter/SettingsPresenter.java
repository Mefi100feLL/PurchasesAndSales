package com.PopCorp.Purchases.presentation.presenter;

import com.PopCorp.Purchases.data.dao.ListItemCategoryDAO;
import com.PopCorp.Purchases.data.model.ListItemCategory;
import com.PopCorp.Purchases.data.model.Region;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.domain.interactor.RegionInteractor;
import com.PopCorp.Purchases.presentation.view.moxy.SettingsView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

@InjectViewState
public class SettingsPresenter extends MvpPresenter<SettingsView> {

    private ListItemCategoryDAO categoryDAO = new ListItemCategoryDAO();
    private RegionInteractor regionsInteractor = new RegionInteractor();

    public boolean saveCategory(ListItemCategory category, String name, int color) {
        ListItemCategory categ = categoryDAO.getWithName(name);
        if (categ != null){
            if ((category != null && category.getId() != categ.getId()) || category == null){
                return false;
            }
        }
        if (category == null){
            category = new ListItemCategory(-1, name, color);
        } else{
            category.setName(name);
            category.setColor(color);
        }
        categoryDAO.updateOrAddToDB(category);
        return true;
    }

    public void showCategories() {
        List<ListItemCategory> categories = categoryDAO.getAllCategories();
        getViewState().showDialogWithCategories(categories);
    }

    public void removeCategory(ListItemCategory category) {
        categoryDAO.remove(category);
    }

    public void showCurrencies() {
        ArrayList<String> currencies = new ArrayList<>(PreferencesManager.getInstance().getCurrencies());
        getViewState().showDialogWithCurrencies(currencies);
    }

    public boolean addNewCurrency(String newCurrency) {
        ArrayList<String> currencies = new ArrayList<>(PreferencesManager.getInstance().getCurrencies());
        if (!currencies.contains(newCurrency)){
            currencies.add(newCurrency);
            PreferencesManager.getInstance().putCurrencies(currencies);
            return true;
        } else{
            return false;
        }
    }

    public void showUnits() {
        ArrayList<String> units = new ArrayList<>(PreferencesManager.getInstance().getEdizms());
        getViewState().showDialogWithUnits(units);
    }

    public boolean addNewUnit(String newUnit) {
        ArrayList<String> units = new ArrayList<>(PreferencesManager.getInstance().getEdizms());
        if (!units.contains(newUnit)){
            units.add(newUnit);
            PreferencesManager.getInstance().putEdizms(units);
            return true;
        } else{
            return false;
        }
    }

    public void loadRegions() {
        getViewState().showProgress();
        regionsInteractor.getData()
                .subscribe(new Observer<List<Region>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().hideProgress();
                        getViewState().showSnackBar(ErrorManager.getErrorResource(e));
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Region> regions) {
                        getViewState().hideProgress();
                        if (regions.size() != 0) {
                            getViewState().showSelectingRegions(regions);
                        } else{
                            getViewState().showRegionsEmpty();
                        }
                    }
                });
    }
}
