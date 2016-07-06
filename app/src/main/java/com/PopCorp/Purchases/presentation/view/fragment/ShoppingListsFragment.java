package com.PopCorp.Purchases.presentation.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.ShoppingListCallback;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.data.utils.EmptyView;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.data.utils.sharing.SharingListBuilderFactory;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.controller.DialogController;
import com.PopCorp.Purchases.presentation.presenter.ShoppingListsPresenter;
import com.PopCorp.Purchases.presentation.view.activity.ShoppingListActivity;
import com.PopCorp.Purchases.presentation.view.adapter.ShoppingListsAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.ShoppingListsView;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.lang.reflect.Field;

public class ShoppingListsFragment extends MvpAppCompatFragment implements ShoppingListsView, ShoppingListCallback {

    @InjectPresenter
    ShoppingListsPresenter presenter;

    private Toolbar toolBar;
    private RecyclerView recyclerView;
    private View progressBar;
    private EmptyView emptyView;
    private FloatingActionButton fab;

    private ShoppingListsAdapter adapter;

    private String[] arraySizesTable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shopping_lists, container, false);
        setHasOptionsMenu(true);

        toolBar = (Toolbar) rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        activity.setSupportActionBar(toolBar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setHomeButtonEnabled(true);
            toolBar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        }

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        emptyView = new EmptyView(rootView);
        progressBar = rootView.findViewById(R.id.progress);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(PreferencesManager.getInstance().getListTableSize(), StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        adapter = new ShoppingListsAdapter(getActivity(), this, presenter.getObjects(), PreferencesManager.getInstance().getShoppingListComparator());
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> presenter.createNewList());

        return rootView;
    }

    @Override
    public void showSnackBar(int errorRes) {
        Snackbar.make(fab, errorRes, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadData();
        toolBar.setTitle(R.string.navigation_drawer_lists);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        emptyView.hide();
    }

    @Override
    public void showData() {
        adapter.update();
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

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.lists, menu);
        super.onCreateOptionsMenu(menu, inflater);

        int groupId = 12;
        MenuItem item = menu.findItem(R.id.action_size_table);
        item.getSubMenu().clear();
        arraySizesTable = getResources().getStringArray(R.array.sizes_table_lists);
        for (String filterItem : arraySizesTable) {
            MenuItem addedItem = item.getSubMenu().add(groupId, filterItem.hashCode(), Menu.NONE, filterItem);
            if (filterItem.equals(String.valueOf(PreferencesManager.getInstance().getListTableSize()))) {
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
                PreferencesManager.getInstance().putListTableSize(Integer.parseInt(filterItem));
                item.setChecked(true);
                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(Integer.parseInt(filterItem), StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
            }
        }
        return true;
    }

    @Override
    public void onOverflowClicked(View v, ShoppingList list) {
        showPopupMenu(v, list);
    }

    @Override
    public void onItemClicked(View view, ShoppingList item) {
        openShoppingList(item);
    }

    @Override
    public void onItemLongClicked(View view, ShoppingList item) {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onEmpty(int stringRes, int drawableRes, int buttonRes, View.OnClickListener listener) {

    }

    @Override
    public void onEmpty(String string, int drawableRes, int buttonRes, View.OnClickListener listener) {

    }

    @Override
    public void showEmptyLists() {
        showError(R.string.empty_no_lists, R.drawable.ic_menu_gallery, R.string.button_create, v -> {
            presenter.createNewList();
        });
    }

    @Override
    public void showDialogForNewList() {
        DialogController.showDialogForNewList(getActivity(), presenter);
    }

    @Override
    public void openShoppingList(ShoppingList list) {
        Intent intent = new Intent(getActivity(), ShoppingListActivity.class);
        intent.putExtra(ShoppingListActivity.CURRENT_LIST, list.getId());
        startActivity(intent);
    }

    @Override
    public void shareListAsSMS(ShoppingList list) {
        Intent intent = SharingListBuilderFactory.getBuilder(0).getIntent(list.getName(), list.getCurrency(), list.getItems());
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.string_send_list_with_app)));
        } catch (Exception e){
            Toast.makeText(getActivity(), R.string.notification_no_apps_for_share_list, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void shareListAsEmail(ShoppingList list) {
        Intent intent = SharingListBuilderFactory.getBuilder(1).getIntent(list.getName(), list.getCurrency(), list.getItems());
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.string_send_list_with_app)));
        } catch (Exception e){
            Toast.makeText(getActivity(), R.string.notification_no_apps_for_share_list, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void shareListAsText(ShoppingList list) {
        Intent intent = SharingListBuilderFactory.getBuilder(2).getIntent(list.getName(), list.getCurrency(), list.getItems());
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.string_send_list_with_app)));
        } catch (Exception e){
            Toast.makeText(getActivity(), R.string.notification_no_apps_for_share_list, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showRemovedList(ShoppingList list) {
        Snackbar.make(fab, getString(R.string.notification_list_removed).replace("name", list.getName()), Snackbar.LENGTH_LONG)
                .setAction(R.string.action_undo, v -> {
                    presenter.returnList(list);
                })
                .show();
    }

    public void showPopupMenu(View view, final ShoppingList list) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.inflate(R.menu.list_popup);
        Object menuHelper;
        Class[] argTypes;
        try {
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popupMenu);
            argTypes = new Class[]{boolean.class};
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
        } catch (Exception e) {
            popupMenu.show();
            return;
        }
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_change: {
                    DialogController.showDialogForEditingList(getActivity(), list, presenter);
                    return true;
                }
                case R.id.action_remove: {
                    presenter.removeList(list);
                    return true;
                }
                case R.id.action_send: {
                    if (list.getItems().size() > 0) {
                        DialogController.showDialogForSendingList(getActivity(), list, presenter);
                    } else{
                        showToast(R.string.notification_list_empty);
                    }
                    return true;
                }
                case R.id.action_alarm: {
                    DialogController.showDialogForAlarm(getActivity(), list, presenter);
                    return true;
                }
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    private void showToast(int text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
