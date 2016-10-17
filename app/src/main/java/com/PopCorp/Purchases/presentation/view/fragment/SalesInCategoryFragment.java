package com.PopCorp.Purchases.presentation.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import com.PopCorp.Purchases.data.analytics.AnalyticsTrackers;
import com.PopCorp.Purchases.data.callback.DialogFavoriteCallback;
import com.PopCorp.Purchases.data.comparator.SalesShopComparator;
import com.PopCorp.Purchases.data.model.Category;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.model.Shop;
import com.PopCorp.Purchases.data.utils.EmptyView;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.controller.DialogController;
import com.PopCorp.Purchases.presentation.presenter.SalesInCategoryPresenter;
import com.PopCorp.Purchases.presentation.utils.TableSizes;
import com.PopCorp.Purchases.presentation.utils.TapTargetManager;
import com.PopCorp.Purchases.presentation.view.activity.SaleActivity;
import com.PopCorp.Purchases.presentation.view.adapter.SalesAdapter;
import com.PopCorp.Purchases.presentation.view.adapter.SalesInCategoryAdapter;
import com.PopCorp.Purchases.presentation.view.adapter.SpinnerAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.SalesInCategoryView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.getkeepsafe.taptargetview.TapTargetView;

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
    private SearchView searchView;

    private Menu menu;

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
        Category category = getArguments().getParcelable(CURRENT_CATEGORY);
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
        AppBarLayout appBarLayout = (AppBarLayout) rootView.findViewById(R.id.app_bar);
        ThemeManager.getInstance().putPrimaryColor(appBarLayout);

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
        presenter.refreshFavorites();
    }

    @Override
    public void showShopsEmpty() {
        showError(R.string.empty_no_shops, R.drawable.ic_shop, R.string.button_try_again, v -> presenter.loadShops());
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
        showError(R.string.empty_no_favorite_shops_in_category, R.drawable.ic_folder_favorite, R.string.button_select_shops, v -> presenter.selectShops());
    }

    @Override
    public void showSalesEmpty() {
        showError(R.string.empty_no_sales_in_category, R.drawable.ic_ghost_top, R.string.button_back_to_categories, v -> getActivity().onBackPressed());
    }

    @Override
    public void filter(String filter) {
        adapter.getFilter().filter(filter);
    }

    @Override
    public void update() {
        adapter.notifyDataSetChanged();
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
    public void showEmptyForSearch(String query) {
        showError(getString(R.string.empty_sales_for_search).replace("query", query), R.drawable.ic_file_favorite, R.string.button_show_all_sales, v -> {
            presenter.search("");
            if (searchView != null) {
                searchView.onActionViewCollapsed();
            }
        });
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        emptyView.hide();
    }

    @Override
    public void showData() {
        if (menu != null && menu.findItem(R.id.action_search) != null) {
            menu.findItem(R.id.action_search).setVisible(true);
        }
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
        showError(ErrorManager.getErrorResource(e), ErrorManager.getErrorImage(e), R.string.button_try_again, view -> presenter.tryAgain());
    }

    @Override
    public void refreshing(boolean refresh) {
        swipeRefresh.setRefreshing(refresh);
    }

    @Override
    public void showSnackBar(Throwable e) {
        Snackbar.make(recyclerView, ErrorManager.getErrorText(e, getActivity()), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.sales, menu);
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
        this.menu = menu;
        menu.findItem(R.id.action_search).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);

        try {
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
        } catch (IllegalStateException e) {//иногда ошибка на Samsung GT-P5200 c Android 4.4.2
            AnalyticsTrackers.getInstance().sendError(e);
            MenuItem item = menu.findItem(R.id.action_size_table);
            if (item != null) {
                item.setVisible(false);
            }
        }
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


    private TapTargetView.Listener tapTargetListener = new TapTargetView.Listener() {
        @Override
        public void onTargetClick(TapTargetView view) {
            super.onTargetClick(view);
            presenter.showTapTarget();
        }
    };

    @Override
    public void showTapTargetForFilter() {
        spinner.post(() -> {
                    View view = spinner.findViewById(android.R.id.text1);
                    if (view == null) {
                        view = spinner;
                    }
                    new TapTargetManager(getActivity())
                            .tapTarget(
                                    TapTargetManager.forView(getActivity(), view, R.string.tap_target_title_sale_filter_by_shops, R.string.tap_target_content_sale_filter_by_shops))
                            .listener(tapTargetListener)
                            .show();
                }
        );
    }

    @Override
    public void showTapTargetForSalesSearch() {
        if (menu != null && menu.findItem(R.id.action_search) != null) {
            new TapTargetManager(getActivity())
                    .tapTarget(
                            TapTargetManager.forToolbarMenuItem(getActivity(),
                                    toolBar,
                                    R.id.action_search,
                                    R.string.tap_target_title_sales_search,
                                    R.string.tap_target_content_sales_search)
                    )
                    .listener(tapTargetListener)
                    .show();
        }
    }
}
