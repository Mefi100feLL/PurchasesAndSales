package com.PopCorp.Purchases.presentation.view.fragment.skidkaonline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.model.skidkaonline.Shop;
import com.PopCorp.Purchases.data.utils.EmptyView;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.presenter.skidkaonline.ShopsPresenter;
import com.PopCorp.Purchases.presentation.utils.TableSizes;
import com.PopCorp.Purchases.presentation.view.activity.MainActivity;
import com.PopCorp.Purchases.presentation.view.activity.SelectingCityActivity;
import com.PopCorp.Purchases.presentation.view.activity.skidkaonline.SalesActivity;
import com.PopCorp.Purchases.presentation.view.adapter.SpinnerAdapter;
import com.PopCorp.Purchases.presentation.view.adapter.skidkaonline.ShopAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.skidkaonline.ShopsView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

public class ShopsFragment extends MvpAppCompatFragment implements ShopsView {

    @InjectPresenter
    ShopsPresenter presenter;

    private Toolbar toolBar;
    private Spinner spinner;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private View progressBar;
    private EmptyView emptyView;

    private ShopAdapter adapter;

    private String[] arraySizesTable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shops, container, false);
        setHasOptionsMenu(true);

        toolBar = (Toolbar) rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        activity.setSupportActionBar(toolBar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setHomeButtonEnabled(true);
            toolBar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        }
        ThemeManager.getInstance().putPrimaryColor(toolBar);

        emptyView = new EmptyView(rootView);
        progressBar = rootView.findViewById(R.id.progress);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);

        spinner = (Spinner) rootView.findViewById(R.id.toolbar_spinner);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity(), new String[]{getString(R.string.spinner_all_shops), getString(R.string.spinner_favorite_shops)});
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.onFilter(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        swipeRefresh.setColorSchemeResources(R.color.swipe_refresh_color_one, R.color.swipe_refresh_color_two, R.color.swipe_refresh_color_three);
        swipeRefresh.setOnRefreshListener(presenter::onRefresh);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), TableSizes.getSkidkaonlineShopTableSize(getActivity()));

        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        adapter = new ShopAdapter(presenter, presenter.getObjects());
        adapter.setLayoutManager(layoutManager, TableSizes.getSkidkaonlineShopTableSize(getActivity()));
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void showSnackBar(Throwable e) {
        Snackbar.make(recyclerView, ErrorManager.getErrorText(e, getActivity()), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter.getObjects().size() == 0) {
            toolBar.setTitle(R.string.title_shops);
        }
        toolBar.setKeepScreenOn(PreferencesManager.getInstance().isDisplayNoOff());
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        emptyView.hide();
    }

    @Override
    public void showData() {
        toolBar.setTitle("");
        spinner.setVisibility(View.VISIBLE);
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
            presenter.tryAgainLoadShops();
        });
    }

    @Override
    public void refreshing(boolean refresh) {
        swipeRefresh.setRefreshing(refresh);
        swipeRefresh.setEnabled(!refresh);
    }

    @Override
    public void openShop(View v, Shop shop) {
        Intent intent = new Intent(getActivity(), SalesActivity.class);
        intent.putExtra(SalesActivity.CURRENT_SHOP, shop);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.shops, menu);
        super.onCreateOptionsMenu(menu, inflater);

        int groupId = 12;
        MenuItem item = menu.findItem(R.id.action_size_table);
        item.getSubMenu().clear();
        arraySizesTable = getResources().getStringArray(R.array.sizes_table_lists);
        for (String filterItem : arraySizesTable) {
            MenuItem addedItem = item.getSubMenu().add(groupId, filterItem.hashCode(), Menu.NONE, filterItem);
            if (filterItem.equals(String.valueOf(TableSizes.getSkidkaonlineShopTableSize(getActivity())))) {
                addedItem.setChecked(true);
            }
        }
        item.getSubMenu().setGroupCheckable(groupId, true, true);
        item.getSubMenu().setGroupEnabled(groupId, true);
        item.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        for (String filterItem : arraySizesTable) {
            if (item.getItemId() == filterItem.hashCode()) {
                TableSizes.putSkidkaonlineShopTableSize(getActivity(), Integer.parseInt(filterItem));
                item.setChecked(true);
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), Integer.parseInt(filterItem));
                adapter.setLayoutManager(layoutManager, Integer.parseInt(filterItem));
                recyclerView.setLayoutManager(layoutManager);
            }
        }
        return true;
    }

    @Override
    public void filter(String filter) {
        adapter.getFilter().filter(filter);
    }

    @Override
    public void showRegionEmpty() {
        showError(R.string.empty_shops_select_city, R.drawable.ic_skyscraper, R.string.button_select_city, v -> {
            openSelectingCityActivity();
        });
    }

    private void openSelectingCityActivity() {
        Intent intent = new Intent(getActivity(), SelectingCityActivity.class);
        startActivityForResult(intent, MainActivity.REQUEST_CODE_FOR_SELECTING_CITY_ACTIVITY);
    }

    @Override
    public void showFavoriteShopsEmpty() {
        showError(R.string.empty_no_favorite_shops, R.drawable.ic_folder_favorite, R.string.button_show_all_shops, v -> {
            presenter.selectSpinner(0);
        });
    }

    @Override
    public void showShopsEmpty() {
        showError(R.string.empty_no_shops, R.drawable.ic_shop, R.string.button_try_again, v -> {
            presenter.tryAgainLoadShops();
        });
    }

    @Override
    public void selectSpinner(int position) {
        spinner.setSelection(position);
    }

    @Override
    public void showSnackBarWithNewShops(int count, boolean isFilterFavorite) {
        Snackbar snackbar = Snackbar.make(recyclerView, getString(R.string.notification_new_shops).replace("count", String.valueOf(count)), Snackbar.LENGTH_LONG);
        if (isFilterFavorite) {
            snackbar.setAction(R.string.action_show_all, v -> {
                spinner.setSelection(0);
            });
        }
        snackbar.show();
    }

    @Override
    public void showSnackBarWithNewShop(Shop shop, boolean isFilterFavorite) {
        Snackbar snackbar = Snackbar.make(recyclerView, getString(R.string.notification_new_shop).replace("name", shop.getName()), Snackbar.LENGTH_LONG);
        if (isFilterFavorite) {
            snackbar.setAction(R.string.action_show_all, v -> {
                spinner.setSelection(0);
            });
        }
        snackbar.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MainActivity.REQUEST_CODE_FOR_SELECTING_CITY_ACTIVITY) {
                presenter.tryAgainLoadShops();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private TapTargetView.Listener tapTargetListener = new TapTargetView.Listener() {
        @Override
        public void onTargetClick(TapTargetView view) {
            super.onTargetClick(view);
            presenter.showTapTarget();
        }
    };

    @Override
    public void showTapTargetForFilter() {
        TapTargetView.showFor(getActivity(),
                TapTarget.forView(spinner, getString(R.string.tap_target_title_shops_filter), getString(R.string.tap_target_content_shops_filter))
                        .outerCircleColor(ThemeManager.getInstance().getPrimaryColorRes())
                        .targetCircleColor(R.color.md_white_1000)
                        .textColor(R.color.md_white_1000)
                        .dimColor(R.color.md_black_1000)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true), tapTargetListener);
    }

    @Override
    public void showTapTargetForShopFavorite() {
        View targetView = adapter.getFirstView();
        if (targetView != null) {
            TapTargetView.showFor(getActivity(),
                    TapTarget.forView(targetView, getString(R.string.tap_target_title_shop_favorite), getString(R.string.tap_target_content_shop_favorite))
                            .outerCircleColor(R.color.md_amber_500)
                            .targetCircleColor(R.color.md_white_1000)
                            .textColor(R.color.md_white_1000)
                            .dimColor(R.color.md_black_1000)
                            .drawShadow(true)
                            .cancelable(false)
                            .tintTarget(true), tapTargetListener);
        }
    }
}
