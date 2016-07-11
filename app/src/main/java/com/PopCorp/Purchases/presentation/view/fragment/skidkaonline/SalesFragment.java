package com.PopCorp.Purchases.presentation.view.fragment.skidkaonline;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.data.model.skidkaonline.Shop;
import com.PopCorp.Purchases.data.utils.EmptyView;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.presenter.skidkaonline.SalesPresenter;
import com.PopCorp.Purchases.presentation.view.activity.skidkaonline.SaleActivity;
import com.PopCorp.Purchases.presentation.view.activity.skidkaonline.SalesActivity;
import com.PopCorp.Purchases.presentation.view.adapter.SpinnerAdapter;
import com.PopCorp.Purchases.presentation.view.adapter.skidkaonline.SaleAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.skidkaonline.SalesView;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.ArrayList;

public class SalesFragment extends MvpAppCompatFragment implements SalesView {

    @InjectPresenter
    SalesPresenter presenter;

    private Toolbar toolBar;
    private Spinner spinner;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private View progressBar;
    private EmptyView emptyView;

    private SaleAdapter adapter;

    private String[] arraySizesTable;

    private String title;

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
        View rootView = inflater.inflate(R.layout.fragment_skidkaonline_sales, container, false);
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

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(PreferencesManager.getInstance().getSkidkaonlineShopTableSize(), StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        adapter = new SaleAdapter(presenter, presenter.getObjects());
        recyclerView.setAdapter(adapter);

        return rootView;
    }


    @Override
    public void showSpinner() {
        ArrayList<String> names = presenter.getFilterStrings();
        if (!names.contains(getString(R.string.spinner_all_catalogs))) {
            names.add(0, getString(R.string.spinner_all_catalogs));
        }
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
    public void showSnackBar(int errorRes) {
        Snackbar.make(recyclerView, errorRes, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        toolBar.setTitle(title);
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
            if (filterItem.equals(String.valueOf(PreferencesManager.getInstance().getSkidkaonlineSaleTableSize()))) {
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
            getActivity().finish();
        }
        for (String filterItem : arraySizesTable) {
            if (item.getItemId() == filterItem.hashCode()) {
                PreferencesManager.getInstance().putSkidkaonlineSaleTableSize(Integer.parseInt(filterItem));
                item.setChecked(true);
                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(Integer.parseInt(filterItem), StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
            }
        }
        return true;
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
}
