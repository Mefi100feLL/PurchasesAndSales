package com.PopCorp.Purchases.presentation.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ListItemSale;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.data.utils.EmptyView;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.data.utils.sharing.SharingListBuilderFactory;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.controller.DialogController;
import com.PopCorp.Purchases.presentation.presenter.ShoppingListPresenter;
import com.PopCorp.Purchases.presentation.utils.DecoratorBigDecimal;
import com.PopCorp.Purchases.presentation.utils.TapTargetManager;
import com.PopCorp.Purchases.presentation.view.activity.InputListItemActivity;
import com.PopCorp.Purchases.presentation.view.activity.ListItemSaleActivity;
import com.PopCorp.Purchases.presentation.view.activity.MainActivity;
import com.PopCorp.Purchases.presentation.view.activity.ProductsActivity;
import com.PopCorp.Purchases.presentation.view.adapter.ListItemAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.ShoppingListView;
import com.afollestad.materialcab.MaterialCab;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ShoppingListFragment extends MvpAppCompatFragment implements ShoppingListView, MaterialCab.Callback {

    public static final String CURRENT_LIST = "current_list";

    private static final int REQUEST_CODE_FOR_INPUT_LISTITEM = 1;

    @InjectPresenter
    ShoppingListPresenter presenter;

    private Toolbar toolBar;
    private RecyclerView recyclerView;
    private View progressBar;
    private EmptyView emptyView;
    private FloatingActionButton fab;
    private LinearLayout totallayout;
    private CoordinatorLayout snackbarlayout;
    private TextView totalDesc;
    private TextView total;
    private TextView totalBuyedDesc;
    private TextView totalBuyed;

    private ListItemAdapter adapter;

    private MaterialCab actionMode;
    private Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setListId(getArguments().getLong(CURRENT_LIST, -1));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);
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
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        snackbarlayout = (CoordinatorLayout) rootView.findViewById(R.id.snackbar_layout);
        totallayout = (LinearLayout) rootView.findViewById(R.id.total_layout);
        total = (TextView) rootView.findViewById(R.id.total);
        totalDesc = (TextView) rootView.findViewById(R.id.total_desc);
        totalBuyed = (TextView) rootView.findViewById(R.id.total_buyed);
        totalBuyedDesc = (TextView) rootView.findViewById(R.id.total_buyed_desc);

        ThemeManager.getInstance().putPrimaryColor(totallayout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);

        fab.setOnClickListener(view -> showInputFragment(null));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        toolBar.setKeepScreenOn(PreferencesManager.getInstance().isDisplayNoOff());
    }

    @Override
    public void showInputFragment(ListItem listItem) {
        Intent intent = new Intent(getActivity(), InputListItemActivity.class);
        intent.putExtra(InputListItemActivity.CURRENT_LISTS, new long[]{presenter.getList().getId()});
        intent.putExtra(InputListItemActivity.CURRENT_LISTITEM, listItem);
        startActivityForResult(intent, REQUEST_CODE_FOR_INPUT_LISTITEM);
    }

    private void showSelectingProducts() {
        Intent intent = new Intent(getActivity(), ProductsActivity.class);
        intent.putParcelableArrayListExtra(SelectingProductsFragment.LISTITEMS, (ArrayList<? extends Parcelable>) presenter.getObjects());
        startActivityForResult(intent, MainActivity.REQUEST_CODE_FOR_SELECTING_PRODUCTS);
    }

    @Override
    public void showItemsRemoved(ArrayList<ListItem> itemsForRemove) {
        Snackbar.make(snackbarlayout, getString(R.string.notification_items_removed).replace("count", String.valueOf(itemsForRemove.size())), Snackbar.LENGTH_LONG)
                .setAction(R.string.action_undo, v -> presenter.onItemsRerurned(itemsForRemove.toArray(new ListItem[itemsForRemove.size()]))).show();
    }

    @Override
    public void showItemRemoved(ListItem listItem) {
        Snackbar.make(snackbarlayout, getString(R.string.notification_item_removed).replace("name", listItem.getName()), Snackbar.LENGTH_LONG)
                .setAction(R.string.action_undo, v -> presenter.onItemsRerurned(listItem)).show();
    }

    @Override
    public void showNothingRemoving() {
        Snackbar.make(fab, getString(R.string.notification_no_items_for_removing), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void updateCurrency(String currency) {
        if (adapter != null) {
            adapter.setCurrency(currency);
            adapter.updateAll();
        }
    }

    @Override
    public void onItemSaleClicked(View v, ListItemSale sale) {
        Intent intent = new Intent(getActivity(), ListItemSaleActivity.class);
        intent.putExtra(ListItemSaleActivity.CURRENT_LISTITEM_SALE, sale);
        startActivity(intent);
    }

    @Override
    public void shareListAsSMS(ShoppingList list) {
        Intent intent = SharingListBuilderFactory.getBuilder(0).getIntent(list.getName(), list.getCurrency(), list.getItems());
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.string_send_list_with_app)));
        } catch (Exception e) {
            Toast.makeText(getActivity(), R.string.notification_no_apps_for_share_list, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void shareListAsEmail(ShoppingList list) {
        Intent intent = SharingListBuilderFactory.getBuilder(1).getIntent(list.getName(), list.getCurrency(), list.getItems());
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.string_send_list_with_app)));
        } catch (Exception e) {
            Toast.makeText(getActivity(), R.string.notification_no_apps_for_share_list, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void shareListAsText(ShoppingList list) {
        Intent intent = SharingListBuilderFactory.getBuilder(2).getIntent(list.getName(), list.getCurrency(), list.getItems());
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.string_send_list_with_app)));
        } catch (Exception e) {
            Toast.makeText(getActivity(), R.string.notification_no_apps_for_share_list, Toast.LENGTH_SHORT).show();
        }
    }

    private TapTargetView.Listener tapTargetListener = new TapTargetView.Listener() {
        @Override
        public void onTargetClick(TapTargetView view) {
            super.onTargetClick(view);
            presenter.showTapTarget();
        }
    };

    @Override
    public void showTapTargetForCreate() {
        new TapTargetManager.Builder(getActivity(), fab, R.string.tap_target_title_items_create, R.string.tap_target_content_items_create)
                .listener(tapTargetListener)
                .tintTarget(false)
                .show();
    }

    @Override
    public void showTapTargetForAdd() {
        View view = getActionViewForMenuItem(toolBar, R.id.action_select_from_products);
        if (view != null) {
            new TapTargetManager.Builder(getActivity(), view, R.string.tap_target_title_items_add, R.string.tap_target_content_items_add)
                    .listener(tapTargetListener)
                    .show();
        }
    }

    @Override
    public void showTapTargetForItemInfo() {
        recyclerView.post(() -> {
            if (adapter.getFirstView() != null) {
                new TapTargetManager.Builder(getActivity(), adapter.getFirstView(), R.string.tap_target_title_item_info, R.string.tap_target_content_item_info)
                        .listener(tapTargetListener)
                        .show();
            }
        });
    }

    @Override
    public void showTapTargetForItemsFilterForShop() {
        toolBar.post(() -> {
            View view = getActionViewForMenuItem(toolBar, R.id.action_shop);
            if (view != null) {
                new TapTargetManager.Builder(getActivity(), view, R.string.tap_target_title_items_filter_by_shop, R.string.tap_target_content_items_filter_by_shop)
                        .listener(tapTargetListener)
                        .show();
            }
        });
    }

    private View getActionViewForMenuItem(ViewGroup rootView, int id) {
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
    }

    private TapTargetView.Listener tapTargetListenerInActionMode = new TapTargetView.Listener() {
        @Override
        public void onTargetClick(TapTargetView view) {
            super.onTargetClick(view);
            presenter.showTapTargetForActionMode();
        }
    };

    @Override
    public void showTapTargetForItemsAddToActionMode() {
        recyclerView.post(() -> {
            if (adapter.getFirstView() != null) {
                new TapTargetManager.Builder(getActivity(), adapter.getFirstView(), R.string.tap_target_title_items_add_to_action_mode, R.string.tap_target_content_items_add_to_action_mode)
                        .listener(tapTargetListenerInActionMode)
                        .outerCircleColor(R.color.action_mode_color)
                        .show();
            }
        });
    }

    @Override
    public void showTapTargetForItemEditInActionMode() {
        if (actionMode != null && actionMode.getMenu() != null && actionMode.getMenu().findItem(R.id.action_edit) != null) {
            View view = getActionViewForMenuItem(actionMode.getToolbar(), R.id.action_edit);
            if (view != null) {
                new TapTargetManager.Builder(getActivity(), view, R.string.tap_target_title_item_edit_in_action_mode, R.string.tap_target_content_item_edit_in_action_mode)
                        .listener(tapTargetListenerInActionMode)
                        .outerCircleColor(R.color.action_mode_color)
                        .show();
            }
        }
    }

    @Override
    public void showTapTargetForItemsRemoveInActionMode() {
        if (actionMode != null && actionMode.getMenu() != null && actionMode.getMenu().findItem(R.id.action_remove) != null) {
            View view = getActionViewForMenuItem(actionMode.getToolbar(), R.id.action_remove);
            if (view != null) {
                new TapTargetManager.Builder(getActivity(), view, R.string.tap_target_title_items_remove_in_action_mode, R.string.tap_target_content_items_remove_in_action_mode)
                        .listener(tapTargetListenerInActionMode)
                        .outerCircleColor(R.color.action_mode_color)
                        .show();
            }
        }
    }

    @Override
    public void showErrorLoadingList() {
        showError(R.string.error_loading_list, R.drawable.ic_scream, R.string.button_exit, view -> finish());
    }

    @Override
    public void showBuyedTotals(int countBuyed, BigDecimal buyed) {
        totalBuyedDesc.setText(getString(R.string.total_buyed_items).replace("count", String.valueOf(countBuyed)));
        totalBuyed.setText(DecoratorBigDecimal.decor(buyed) + " " + presenter.getCurrency());
    }

    @Override
    public void showTotals(int size, BigDecimal total) {
        totalDesc.setText(getString(R.string.total_items).replace("count", String.valueOf(size)));
        this.total.setText(DecoratorBigDecimal.decor(total) + " " + presenter.getCurrency());
    }

    @Override
    public void hideShopsFilter() {
        recyclerView.post(() -> menu.findItem(R.id.action_shop).setVisible(false));
    }

    @Override
    public void showShopsFilter(ArrayList<String> shops, String filter) {
        recyclerView.post(() -> {
            menu.findItem(R.id.action_shop).setVisible(true);
            Menu subMenu = menu.findItem(R.id.action_shop).getSubMenu();
            subMenu.clear();
            if (!shops.contains(getString(R.string.menu_item_all_shops))) {
                shops.add(0, getString(R.string.menu_item_all_shops));
            }
            for (String key : shops) {
                MenuItem item = subMenu.add(R.id.action_shop_group, shops.indexOf(key), shops.indexOf(key), key).setCheckable(true);
                if (key.equals(filter) || (filter.isEmpty() && shops.indexOf(key) == 0)) {
                    item.setChecked(true);
                }
            }
            subMenu.setGroupCheckable(R.id.action_shop_group, true, true);
        });
    }

    @Override
    public void showEmptyItems() {
        showError(R.string.empty_no_listitems, R.drawable.ic_cart_minus, R.string.button_add, view -> showInputFragment(null));
    }

    @Override
    public void onItemBuyedChanged(ListItem item) {
        if (adapter != null) {
            adapter.onItemBuyed(item);
        }
    }

    @Override
    public void showSnackBar(Throwable e) {
        Snackbar.make(snackbarlayout, ErrorManager.getErrorText(e, getActivity()), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        emptyView.hide();
    }

    @Override
    public void showData() {
        if (adapter == null) {
            adapter = new ListItemAdapter(getActivity(), presenter, presenter.getObjects(), presenter.getSelectedItems(), PreferencesManager.getInstance().getListItemDecoratorComparator(), presenter.getCurrency());
            recyclerView.setAdapter(adapter);
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

    }

    @Override
    public void refreshing(boolean refresh) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.shopping_list, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getGroupId() == R.id.action_shop_group) {
            presenter.onShopFilter(item.getItemId());
            item.setChecked(true);
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
            case R.id.action_select_from_products:
                showSelectingProducts();
                break;
            case R.id.action_remove_buyed:
                presenter.removeBuyed();
                break;
            case R.id.action_change:
                DialogController.showDialogForEditingList(getActivity(), presenter.getList(), presenter);
                break;
            case R.id.action_remove:
                showDialogForRemovingList();
                break;
            case R.id.action_send:
                DialogController.showDialogForSendingList(getActivity(), presenter.getList(), presenter);
                break;
            /*case R.id.action_alarm:
                DialogController.showDialogForAlarm(getActivity(), presenter.getList(), presenter);
                break;*/
        }
        return true;
    }

    private void showDialogForRemovingList() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title(R.string.dialog_title_removing_list);
        builder.content(R.string.dialog_content_sure_remove_list);
        builder.positiveText(R.string.dialog_button_remove);
        builder.negativeText(R.string.dialog_button_cancel);
        builder.onPositive((dialog, which) -> presenter.removeList());
        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    @Override
    public void showTitle(String title) {
        recyclerView.post(() -> toolBar.setTitle(title));
    }

    @Override
    public void filter(String filter) {
        if (adapter != null) {
            adapter.getFilter().filter(filter);
        }
    }

    @Override
    public void changeItemInActionMode(int count, ListItem item) {
        if (actionMode != null) {
            actionMode.setTitle(String.valueOf(count));
            if (count == 1) {
                actionMode.getMenu().findItem(R.id.action_edit).setVisible(true);
            } else {
                actionMode.getMenu().findItem(R.id.action_edit).setVisible(false);
            }
        }
    }

    @Override
    public void finishActionMode() {
        if (actionMode != null && actionMode.isActive()) {
            actionMode.finish();
        }
    }

    @Override
    public void startActionMode() {
        actionMode = new MaterialCab((AppCompatActivity) getActivity(), R.id.cab_stub)
                .setMenu(R.menu.listitem_popup)
                .setContentInsetStartRes(R.dimen.mcab_default_content_inset)
                .setBackgroundColorRes(R.color.md_grey_700)
                .setCloseDrawableRes(R.drawable.ic_close_white_24dp)
                .start(this);
        actionMode.getToolbar().setNavigationOnClickListener(view -> presenter.closeActionMode());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_INPUT_LISTITEM) {
                ListItem item = data.getParcelableExtra(InputListItemActivity.CURRENT_LISTITEM);
                if (item != null) {
                    presenter.onItemsRerurned(item);
                }
            }
            if (requestCode == MainActivity.REQUEST_CODE_FOR_SELECTING_PRODUCTS) {
                presenter.addFromProducts(data.getParcelableArrayListExtra(SelectingProductsFragment.LISTITEMS));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCabCreated(MaterialCab cab, Menu menu) {
        cab.setTitle("1");
        totallayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.md_grey_700));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.md_grey_700));
        }
        presenter.showTapTargetForActionMode();
        return true;
    }

    @Override
    public boolean onCabItemClicked(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit: {
                presenter.editItem();
                break;
            }
            case R.id.action_remove: {
                presenter.removeItem();
                break;
            }
            case R.id.action_send_as_sms: {

                break;
            }
            case R.id.action_send_as_email: {

                break;
            }
            case R.id.action_send_as_text: {

                break;
            }
            default: {
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean onCabFinished(MaterialCab cab) {
        //presenter.clearSelectedItems();
        totallayout.setBackgroundColor(ThemeManager.getInstance().getPrimaryColor());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), ThemeManager.getInstance().getPrimaryDarkColorRes()));
        }
        return true;
    }

    public boolean onBackPressed() {
        if (actionMode != null && actionMode.isActive()) {
            presenter.closeActionMode();
            return true;
        }
        return false;
    }
}
