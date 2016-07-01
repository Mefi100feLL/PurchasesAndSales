package com.PopCorp.Purchases.presentation.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import com.PopCorp.Purchases.data.callback.RecyclerCallback;
import com.PopCorp.Purchases.data.callback.ShoppingListCallback;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.data.utils.EmptyView;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.controller.DialogController;
import com.PopCorp.Purchases.presentation.presenter.ShoppingListPresenter;
import com.PopCorp.Purchases.presentation.view.activity.InputListItemActivity;
import com.PopCorp.Purchases.presentation.view.adapter.ListItemAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.ShoppingListView;
import com.afollestad.materialcab.MaterialCab;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.ArrayList;
import java.util.List;

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

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
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

    }

    @Override
    public void showItemRemoved(ListItem listItem) {

    }

    @Override
    public void showSnackBar(int errorRes) {
        Snackbar.make(recyclerView, errorRes, Snackbar.LENGTH_SHORT).show();
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
            adapter = new ListItemAdapter(getActivity(), presenter, presenter.getObjects(), presenter.getSelectedItems(),  presenter.getCurrency());
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

        return true;
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
                    presenter.onItemRerurned(item);
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
