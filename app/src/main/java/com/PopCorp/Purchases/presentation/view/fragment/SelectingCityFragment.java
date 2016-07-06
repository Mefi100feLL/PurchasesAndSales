package com.PopCorp.Purchases.presentation.view.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.model.skidkaonline.City;
import com.PopCorp.Purchases.data.utils.EmptyView;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.presenter.SelectingCityPresenter;
import com.PopCorp.Purchases.presentation.view.adapter.CategoriesAdapter;
import com.PopCorp.Purchases.presentation.view.adapter.skidkaonline.CityAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.SelectingCityView;
import com.arellomobile.mvp.presenter.InjectPresenter;

public class SelectingCityFragment extends MvpAppCompatFragment implements SelectingCityView {

    @InjectPresenter
    SelectingCityPresenter presenter;


    private Toolbar toolBar;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private View progressBar;
    private EmptyView emptyView;

    private CityAdapter adapter;

    private FloatingActionButton fab;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_selecting_city, container, false);
        setHasOptionsMenu(true);

        toolBar = (Toolbar) rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        activity.setSupportActionBar(toolBar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setHomeButtonEnabled(true);
            toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        }

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        emptyView = new EmptyView(rootView);
        progressBar = rootView.findViewById(R.id.progress);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);

        swipeRefresh.setColorSchemeResources(R.color.swipe_refresh_color_one, R.color.swipe_refresh_color_two, R.color.swipe_refresh_color_three);
        swipeRefresh.setOnRefreshListener(presenter::onRefresh);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        adapter = new CityAdapter(presenter, presenter.getObjects());
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> {});

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        toolBar.setTitle(R.string.title_selecting_city);
    }

    @Override
    public void showCitiesEmpty() {
        showError(R.string.empty_no_cities, R.drawable.ic_menu_gallery, R.string.button_update, view -> {
            presenter.update();
        });
    }

    @Override
    public void filter(String filter) {
        adapter.getFilter().filter(filter);
    }

    @Override
    public void setSelectedCity(City selectedCity) {
        adapter.setSelectedCity(selectedCity);
    }

    @Override
    public void showSnackBar(int errorRes) {
        Snackbar.make(fab, errorRes, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        emptyView.hide();
    }

    @Override
    public void showData() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.hide();
    }

    @Override
    public void showError(String text, int drawableRes, int textButtonRes, View.OnClickListener listener) {
        emptyView.showEmpty(text, drawableRes, textButtonRes, listener);
    }

    @Override
    public void showError(int textRes, int drawableRes, int textButtonRes, View.OnClickListener listener) {
        emptyView.showEmpty(textRes, drawableRes, textButtonRes, listener);
    }

    @Override
    public void refreshing(boolean refresh) {
        swipeRefresh.setRefreshing(refresh);
        swipeRefresh.setEnabled(!refresh);
    }

}
