package com.PopCorp.Purchases.presentation.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.model.skidkaonline.City;
import com.PopCorp.Purchases.data.utils.EmptyView;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.presenter.SelectingCityPresenter;
import com.PopCorp.Purchases.presentation.view.adapter.skidkaonline.CityAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.SelectingCityView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

public class SelectingCityFragment extends MvpAppCompatFragment implements SelectingCityView {

    @InjectPresenter
    SelectingCityPresenter presenter;

    private Toolbar toolBar;
    private FastScrollRecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private View progressBar;
    private EmptyView emptyView;

    private FloatingActionButton fab;
    private SearchView searchView;

    private CityAdapter adapter;


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
        ThemeManager.getInstance().putPrimaryColor(toolBar);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        emptyView = new EmptyView(rootView);
        progressBar = rootView.findViewById(R.id.progress);
        recyclerView = (FastScrollRecyclerView) rootView.findViewById(R.id.recycler);
        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);

        swipeRefresh.setColorSchemeResources(R.color.swipe_refresh_color_one, R.color.swipe_refresh_color_two, R.color.swipe_refresh_color_three);
        swipeRefresh.setOnRefreshListener(presenter::onRefresh);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        adapter = new CityAdapter(presenter, presenter.getObjects());
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> presenter.onFabClicked());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        toolBar.setTitle(R.string.title_selecting_city);
        toolBar.setKeepScreenOn(PreferencesManager.getInstance().isDisplayNoOff());
    }

    @Override
    public void showCitiesEmpty() {
        showError(R.string.empty_no_cities, R.drawable.ic_skyscraper, R.string.button_try_again, view -> {
            presenter.tryAgainLoad();
        });
    }

    @Override
    public void showEmptyForSearch(String searchText) {
        showError(R.string.empty_search_city, R.drawable.ic_compas, R.string.button_all_cities, view -> {
            presenter.search("");
            if (searchView != null) {
                searchView.onActionViewCollapsed();
            }
        });
    }

    @Override
    public void filter(String filter) {
        adapter.getFilter().filter(filter);
    }

    @Override
    public void setSelectedCity(City selectedCity) {
        adapter.setSelectedCity(selectedCity);
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptySelectedCity() {
        Snackbar.make(fab, R.string.notification_city_not_selected, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setResultAndExit() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void showFastScroll() {
        recyclerView.setAutoHideDelay(1500);
    }

    @Override
    public void hideFastScroll() {
        recyclerView.setAutoHideDelay(0);
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void showSnackBar(Throwable e) {
        Snackbar.make(fab, ErrorManager.getErrorText(e, getActivity()), Snackbar.LENGTH_SHORT).show();
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
    public void showError(Throwable e) {
        showError(ErrorManager.getErrorExpandedText(e, getActivity()), ErrorManager.getErrorImage(e), R.string.button_try_again, view -> {
            presenter.tryAgainLoad();
        });
    }

    @Override
    public void refreshing(boolean refresh) {
        swipeRefresh.setRefreshing(refresh);
        swipeRefresh.setEnabled(!refresh);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cities, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.search(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
