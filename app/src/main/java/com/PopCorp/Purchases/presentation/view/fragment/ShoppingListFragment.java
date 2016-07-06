package com.PopCorp.Purchases.presentation.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.utils.EmptyView;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.controller.DialogController;
import com.PopCorp.Purchases.presentation.presenter.ShoppingListPresenter;
import com.PopCorp.Purchases.presentation.view.activity.InputListItemActivity;
import com.PopCorp.Purchases.presentation.view.adapter.ListItemAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.ShoppingListView;
import com.afollestad.materialcab.MaterialCab;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.mikepenz.materialize.color.Material;

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

    private ListItemAdapter adapter;

    private MaterialCab actionMode;
    private FloatingActionButton fab;

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

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        emptyView = new EmptyView(rootView);
        progressBar = rootView.findViewById(R.id.progress);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);


        fab.setOnClickListener(v -> showInputFragment(null));

        return rootView;
    }

    @Override
    public void showInputFragment(ListItem listItem) {
        Intent intent = new Intent(getActivity(), InputListItemActivity.class);
        intent.putExtra(InputListItemFragment.CURRENT_LIST, presenter.getList().getId());
        intent.putExtra(InputListItemFragment.CURRENT_CURRENCY, presenter.getList().getCurrency());
        intent.putExtra(InputListItemFragment.CURRENT_LISTITEM, listItem);
        startActivityForResult(intent, REQUEST_CODE_FOR_INPUT_LISTITEM);
    }

    @Override
    public void showItemsRemoved(ArrayList<ListItem> itemsForRemove) {
        Snackbar.make(fab, getString(R.string.notification_items_removed).replace("count", String.valueOf(itemsForRemove.size())), Snackbar.LENGTH_LONG)
                .setAction(R.string.action_undo, v -> {
                    presenter.onItemsRerurned(itemsForRemove.toArray(new ListItem[itemsForRemove.size()]));
                }).show();
    }

    @Override
    public void showItemRemoved(ListItem listItem) {
        Snackbar.make(fab, getString(R.string.notification_item_removed).replace("name", listItem.getName()), Snackbar.LENGTH_LONG)
                .setAction(R.string.action_undo, v -> {
                    presenter.onItemsRerurned(listItem);
                }).show();
    }

    @Override
    public void updateAllItems() {
        adapter.updateAll();
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
        adapter.setCurrency(currency);
        adapter.updateAll();
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
    public void refreshing(boolean refresh) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.shopping_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
            case R.id.action_shop:

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
            case R.id.action_alarm:
                DialogController.showDialogForAlarm(getActivity(), presenter.getList(), presenter);
                break;
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
        toolBar.setTitle(title);
    }

    @Override
    public void filter(String filter) {
        adapter.getFilter().filter(filter);
    }

    @Override
    public void changeItemInActionMode(int count, ListItem item) {
        actionMode.setTitle(String.valueOf(count));
        if (count == 1) {
            actionMode.getMenu().findItem(R.id.action_edit).setVisible(true);
        } else {
            actionMode.getMenu().findItem(R.id.action_edit).setVisible(false);
        }
    }

    @Override
    public void finishActionMode() {
        actionMode.finish();
    }

    @Override
    public void startActionMode() {
        actionMode = new MaterialCab((AppCompatActivity) getActivity(), R.id.cab_stub)
                .setMenu(R.menu.listitem_popup)
                .setPopupMenuTheme(R.style.ThemeOverlay_AppCompat_Light)
                .setContentInsetStartRes(R.dimen.mcab_default_content_inset)
                .setBackgroundColorRes(R.color.primary_color_dark)
                .setCloseDrawableRes(R.drawable.ic_close_white_24dp)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_INPUT_LISTITEM) {
                ListItem item = data.getParcelableExtra(InputListItemFragment.CURRENT_LISTITEM);
                if (item != null){
                    presenter.onItemsRerurned(item);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCabCreated(MaterialCab cab, Menu menu) {
        cab.setTitle("1");
        return true;
    }

    @Override
    public boolean onCabItemClicked(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                presenter.closeActionMode();
                break;
            }
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
        return true;
    }
}
