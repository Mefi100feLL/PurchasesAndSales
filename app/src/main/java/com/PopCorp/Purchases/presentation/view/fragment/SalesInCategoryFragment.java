package com.PopCorp.Purchases.presentation.view.fragment;

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
import com.PopCorp.Purchases.data.callback.DialogFavoriteCallback;
import com.PopCorp.Purchases.data.comparator.SalesShopComparator;
import com.PopCorp.Purchases.data.model.Category;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.model.Shop;
import com.PopCorp.Purchases.data.utils.EmptyView;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.controller.DialogController;
import com.PopCorp.Purchases.presentation.presenter.SalesInCategoryPresenter;
import com.PopCorp.Purchases.presentation.utils.TableSizes;
import com.PopCorp.Purchases.presentation.view.activity.SaleActivity;
import com.PopCorp.Purchases.presentation.view.activity.SalesActivity;
import com.PopCorp.Purchases.presentation.view.adapter.SalesAdapter;
import com.PopCorp.Purchases.presentation.view.adapter.SalesInCategoryAdapter;
import com.PopCorp.Purchases.presentation.view.adapter.SpinnerAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.SalesInCategoryView;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.ArrayList;
import java.util.List;

public class SalesInCategoryFragment extends MvpAppCompatFragment implements SalesInCategoryView {

    private static final String CURRENT_CATEGORY = "current_category";

    @InjectPresenter
    SalesInCategoryPresenter presenter;

    private Toolbar toolBar;
    private Spinner spinner;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private View progressBar;
    private EmptyView emptyView;

    private SalesAdapter adapter;

    private String title = "";

    private String[] arraySizesTable;

    public static SalesInCategoryFragment create(Category category) {
        SalesInCategoryFragment result = new SalesInCategoryFragment();
        Bundle args = new Bundle();
        args.putParcelable(CURRENT_CATEGORY, category);
        result.setArguments(args);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Category category = getArguments().getParcelable(SalesActivity.CURRENT_CATEGORY);
        presenter.setCurrentCategory(category);
        if (category != null) {
            title = category.getName();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sales, container, false);
        setHasOptionsMenu(true);

        toolBar = (Toolbar) rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        activity.setSupportActionBar(toolBar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setHomeButtonEnabled(true);
            toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        }

        emptyView = new EmptyView(rootView);
        progressBar = rootView.findViewById(R.id.progress);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        spinner = (Spinner) rootView.findViewById(R.id.toolbar_spinner);

        swipeRefresh.setColorSchemeResources(R.color.swipe_refresh_color_one, R.color.swipe_refresh_color_two, R.color.swipe_refresh_color_three);
        swipeRefresh.setOnRefreshListener(presenter::onRefresh);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), TableSizes.getSaleTableSize(getActivity()));

        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        adapter = new SalesInCategoryAdapter(presenter, presenter.getObjects(), new SalesShopComparator());
        adapter.setLayoutManager(layoutManager, TableSizes.getSaleTableSize(getActivity()));
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        toolBar.setTitle(title);
        toolBar.setKeepScreenOn(PreferencesManager.getInstance().isDisplayNoOff());
    }

    @Override
    public void showShopsEmpty() {
        showError(R.string.empty_no_shops, R.drawable.ic_shop, R.string.button_try_again, v -> {
            presenter.loadShops();
        });
    }

    @Override
    public void showShopsForSelectingFavorites(ArrayList<Shop> shops) {
        DialogController.showDialogForFavoriteShops(getActivity(), shops, new DialogFavoriteCallback<Shop>() {
            @Override
            public void onSelected(List<Shop> result) {
                presenter.onFavoriteShopsSelected(result);
            }

            @Override
            public void onCancel() {
                showFavoriteShopsEmpty();
            }
        });
    }

    @Override
    public void showFavoriteShopsEmpty() {
        showError(R.string.empty_no_favorite_shops_in_category, R.drawable.ic_folder_favorite, R.string.button_select_shops, v -> {
            presenter.selectShops();
        });
    }

    @Override
    public void showSalesEmpty() {
        showError(R.string.empty_no_sales_in_category, R.drawable.ic_ghost_top, R.string.button_back_to_categories, v -> {
            getActivity().onBackPressed();
        });
    }

    @Override
    public void filter(String filter) {
        adapter.getFilter().filter(filter);
    }

    @Override
    public void showSpinner() {
        ArrayList<String> names = presenter.getFilterStrings();
        names.add(0, getString(R.string.spinner_all_shops));
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity(), names.toArray(new String[names.size()]));
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            private boolean first = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!first) {
                    presenter.onFilter(position);
                } else {
                    first = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSpinner() {
        spinner.setVisibility(View.GONE);
    }

    @Override
    public void selectSpinner(int filterPosition) {
        spinner.setSelection(filterPosition);
    }

    @Override
    public void showSales(View view, Sale item) {
        Intent intent = new Intent(getActivity(), SaleActivity.class);
        intent.putExtra(SaleActivity.CURRENT_SALE, String.valueOf(item.getId()));
        ArrayList<Sale> sales = adapter.getSales();
        String[] salesIds = new String[sales.size()];
        for (int i = 0; i < sales.size(); i++) {
            salesIds[i] = String.valueOf(sales.get(i).getId());
        }
        intent.putExtra(SaleActivity.ARRAY_SALES, salesIds);
        startActivity(intent);
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
        showError(ErrorManager.getErrorResource(e), ErrorManager.getErrorImage(e), R.string.button_try_again, view -> {
            presenter.tryAgain();
        });
    }

    @Override
    public void refreshing(boolean refresh) {
        swipeRefresh.setRefreshing(refresh);
        swipeRefresh.setEnabled(!refresh);
    }

    @Override
    public void showSnackBar(Throwable e) {
        Snackbar.make(recyclerView, ErrorManager.getErrorText(e, getActivity()), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.sales, menu);
        super.onCreateOptionsMenu(menu, inflater);

        int groupId = 12;
        MenuItem item = menu.findItem(R.id.action_size_table);
        item.getSubMenu().clear();
        arraySizesTable = getResources().getStringArray(R.array.sizes_table_lists);
        for (String filterItem : arraySizesTable) {
            MenuItem addedItem = item.getSubMenu().add(groupId, filterItem.hashCode(), Menu.NONE, filterItem);
            if (filterItem.equals(String.valueOf(TableSizes.getSaleTableSize(getActivity())))) {
                addedItem.setChecked(true);
            }
        }
        item.getSubMenu().setGroupCheckable(groupId, true, true);
        item.getSubMenu().setGroupEnabled(groupId, true);
        item.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        }
        for (String filterItem : arraySizesTable) {
            if (item.getItemId() == filterItem.hashCode()) {
                TableSizes.putSaleTableSize(getActivity(), Integer.parseInt(filterItem));
                item.setChecked(true);
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), Integer.parseInt(filterItem));
                adapter.setLayoutManager(layoutManager, Integer.parseInt(filterItem));
                recyclerView.setLayoutManager(layoutManager);
            }
        }
        return true;
    }
}
