package com.PopCorp.Purchases.presentation.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.AdapterView;
import android.widget.Spinner;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.comparator.ProductAlphabetComparator;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.Product;
import com.PopCorp.Purchases.data.utils.EmptyView;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.presenter.SelectingProductsPresenter;
import com.PopCorp.Purchases.presentation.utils.TapTargetManager;
import com.PopCorp.Purchases.presentation.view.adapter.SelectingProductsAdapter;
import com.PopCorp.Purchases.presentation.view.adapter.SpinnerAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.SelectingProductsView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.Comparator;

public class SelectingProductsFragment extends MvpAppCompatFragment implements SelectingProductsView {

    public static final String LISTITEMS = "listitems";

    @InjectPresenter
    SelectingProductsPresenter presenter;

    private Toolbar toolBar;
    private Spinner spinner;
    private FastScrollRecyclerView recyclerView;
    private View progressBar;
    private EmptyView emptyView;
    private FloatingActionButton fab;
    private SearchView searchView;

    private SelectingProductsAdapter adapter;

    private Menu menu;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setListItems(getArguments().getParcelableArrayList(LISTITEMS));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_products_selecting, container, false);
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

        spinner = (Spinner) rootView.findViewById(R.id.toolbar_spinner);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity(), new String[]{getString(R.string.spinner_all_products), getString(R.string.spinner_favorite_products)});
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        adapter = new SelectingProductsAdapter(getActivity(), presenter, presenter.getObjects(), presenter.getSelectedProducts(), new ProductAlphabetComparator());
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> presenter.onFabClicked());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter.getObjects().size() == 0){
            toolBar.setTitle(R.string.title_all_products);
        } else {
            toolBar.setTitle("");
        }
        toolBar.setKeepScreenOn(PreferencesManager.getInstance().isDisplayNoOff());
    }

    @Override
    public void filter(String filter) {
        adapter.getFilter().filter(filter);
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

    }

    @Override
    public void refreshing(boolean refresh) {

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
    public void setAdapterComparator(Comparator<Product> comparator) {
        adapter.setComparator(comparator);
    }

    @Override
    public void checkItem(int itemId) {
        recyclerView.post(() -> {
            if (menu != null && menu.findItem(itemId) != null) {
                menu.findItem(itemId).setChecked(true);
            }
        });
    }

    @Override
    public void showProductsEmpty() {
        showError(R.string.empty_no_products, R.drawable.ic_menu_gallery, R.string.button_back_to_list, view -> {
            getActivity().setResult(Activity.RESULT_CANCELED);
            getActivity().finish();
        });
    }

    @Override
    public void showFavoriteProductsEmpty() {
        showError(R.string.empty_no_favorite_products, R.drawable.ic_menu_gallery, R.string.button_all_products, view -> spinner.setSelection(0));
    }

    @Override
    public void showSearchProductsEmpty(String currentFilter) {
        showError(getString(R.string.empty_no_finded_products).replace("query", currentFilter), R.drawable.ic_menu_gallery, R.string.button_all_products, view -> {
            presenter.search("");
            if (searchView != null) {
                searchView.onActionViewCollapsed();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.products, menu);
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
    public void onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
                break;
            }
            case R.id.action_sort_by_alphabet:
                presenter.sortByAlphabet(item.getItemId());
                break;
            case R.id.action_sort_by_category:
                presenter.sortByCategories(item.getItemId());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setResultAndExit(ArrayList<ListItem> result) {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(LISTITEMS, result);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    private TapTargetView.Listener tapTargetListener = new TapTargetView.Listener() {
        @Override
        public void onTargetClick(TapTargetView view) {
            super.onTargetClick(view);
            presenter.showTapTarget();
        }
    };

    @Override
    public void showTapTargetForProductsSearch() {
        if (menu != null && menu.findItem(R.id.action_search) != null) {
            new TapTargetManager(getActivity())
                    .tapTarget(
                            TapTargetManager.forToolbarMenuItem(getActivity(),
                                    toolBar,
                                    R.id.action_search,
                                    R.string.tap_target_title_products_search,
                                    R.string.tap_target_content_products_search)
                    )
                    .listener(tapTargetListener)
                    .show();
        }
    }

    @Override
    public void showTapTargetForProductsFilter() {
        spinner.post(() -> {
                    View view = spinner.findViewById(android.R.id.text1);
                    if (view == null) {
                        view = spinner;
                    }
                    if (view != null) {
                        new TapTargetManager(getActivity())
                                .tapTarget(
                                        TapTargetManager.forView(getActivity(), view, R.string.tap_target_title_products_filter, R.string.tap_target_content_products_filter))
                                .listener(tapTargetListener)
                                .show();
                    }
                }
        );
    }

    @Override
    public void showTapTargetForProductsSorting() {
        if (menu != null && menu.findItem(R.id.action_sort) != null) {
            new TapTargetManager(getActivity())
                    .tapTarget(
                            TapTargetManager.forToolbarMenuItem(getActivity(),
                                    toolBar,
                                    R.id.action_sort,
                                    R.string.tap_target_title_products_sorting,
                                    R.string.tap_target_content_products_sorting)
                    )
                    .listener(tapTargetListener)
                    .show();
        }
    }

    @Override
    public void showTapTargetForProductsReturn() {
        View view = fab;
        if (view != null) {
            new TapTargetManager(getActivity())
                    .tapTarget(
                            TapTargetManager.forView(getActivity(), view, R.string.tap_target_title_products_return, R.string.tap_target_content_products_return)
                                    .tintTarget(false))
                    .show();
        }
    }

    /*private View getActionViewForMenuItem(ViewGroup rootView, int id) {
        for (int toolbarChildIndex = 0; toolbarChildIndex < rootView.getChildCount(); toolbarChildIndex++) {
            View view = rootView.getChildAt(toolbarChildIndex);
            if (view.getId() == id) {
                return view;
            }
            if (view instanceof ActionMenuView) {
                ActionMenuView menuView = (ActionMenuView) view;
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    if (menuView.getChildAt(i).getId() == id) {
                        return menuView.getChildAt(i);
                    }
                }
            }
        }
        return null;
    }*/
}
