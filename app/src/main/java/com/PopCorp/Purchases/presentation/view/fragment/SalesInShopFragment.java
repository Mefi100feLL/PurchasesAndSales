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
import com.PopCorp.Purchases.data.comparator.SalesCategoryComparator;
import com.PopCorp.Purchases.data.model.Category;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.model.Shop;
import com.PopCorp.Purchases.data.utils.EmptyView;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.controller.DialogController;
import com.PopCorp.Purchases.presentation.presenter.SalesInShopPresenter;
import com.PopCorp.Purchases.presentation.view.activity.SaleActivity;
import com.PopCorp.Purchases.presentation.view.activity.SalesActivity;
import com.PopCorp.Purchases.presentation.view.adapter.SalesAdapter;
import com.PopCorp.Purchases.presentation.view.adapter.SalesInShopAdapter;
import com.PopCorp.Purchases.presentation.view.adapter.SpinnerAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.SalesInShopView;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.ArrayList;
import java.util.List;

public class SalesInShopFragment extends MvpAppCompatFragment implements SalesInShopView {

    @InjectPresenter
    SalesInShopPresenter presenter;

    private Toolbar toolBar;
    private Spinner spinner;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private View progressBar;
    private EmptyView emptyView;

    private SalesAdapter adapter;

    private String title = "";

    private String[] arraySizesTable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Shop shop = getArguments().getParcelable(SalesActivity.CURRENT_SHOP);
        presenter.setCurrentShop(shop);
        if (shop != null) {
            title = shop.getName();
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

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), PreferencesManager.getInstance().getSaleTableSize());

        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        adapter = new SalesInShopAdapter(presenter, presenter.getObjects(), new SalesCategoryComparator());
        adapter.setLayoutManager(layoutManager, PreferencesManager.getInstance().getSaleTableSize());
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        toolBar.setTitle(title);
    }

    @Override
    public void showCategoriesEmpty() {
        showError(R.string.empty_no_categgories, R.drawable.ic_menu_gallery, R.string.button_try_again, v -> {
            presenter.loadCategories();
        });
    }

    @Override
    public void showCategoriesForSelectingFavorites(ArrayList<Category> categories) {
        DialogController.showDialogForFavoriteCategories(getActivity(), categories, new DialogFavoriteCallback<Category>() {
            @Override
            public void onSelected(List<Category> result) {
                presenter.onFavoriteCategoriesSelected(result);
            }

            @Override
            public void onCancel() {
                showFavoriteCategoriesEmpty();
            }
        });
    }

    @Override
    public void showFavoriteCategoriesEmpty() {
        showError(R.string.empty_no_favorite_categories_in_shop, R.drawable.ic_menu_gallery, R.string.button_select_categories, v -> {
            presenter.selectCategories();
        });
    }

    @Override
    public void showErrorLoadingSales(Throwable e) {
        showError(getString(R.string.error_when_loading_sales) + "\n" + getString(ErrorManager.getErrorResource(e)), R.drawable.ic_menu_gallery, R.string.button_try_again, v -> {
            presenter.tryAgain();
        });
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
        for (int i=0; i < sales.size(); i++){
            salesIds[i] = String.valueOf(sales.get(i).getId());
        }
        intent.putExtra(SaleActivity.ARRAY_SALES, salesIds);
        startActivity(intent);
    }

    @Override
    public void showSalesEmpty() {
        showError(R.string.empty_no_sales_in_shop, R.drawable.ic_menu_gallery, R.string.button_try_again, v -> {
            presenter.tryAgain();
        });
    }

    @Override
    public void filter(String filter) {
        adapter.getFilter().filter(filter);
    }

    @Override
    public void showSpinner() {
        ArrayList<String> names = presenter.getFilterStrings();
        names.add(0, getString(R.string.spinner_all_categories));
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

    @Override
    public void showSnackBar(int errorRes) {
        Snackbar.make(recyclerView, errorRes, Snackbar.LENGTH_SHORT).show();
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
            if (filterItem.equals(String.valueOf(PreferencesManager.getInstance().getSaleTableSize()))) {
                addedItem.setChecked(true);
            }
        }
        item.getSubMenu().setGroupCheckable(groupId, true, true);
        item.getSubMenu().setGroupEnabled(groupId, true);
        item.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            getActivity().onBackPressed();
        }
        for (String filterItem : arraySizesTable) {
            if (item.getItemId() == filterItem.hashCode()) {
                PreferencesManager.getInstance().putSaleTableSize(Integer.parseInt(filterItem));
                item.setChecked(true);
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), Integer.parseInt(filterItem));
                adapter.setLayoutManager(layoutManager, Integer.parseInt(filterItem));
                recyclerView.setLayoutManager(layoutManager);
            }
        }
        return true;
    }
}
