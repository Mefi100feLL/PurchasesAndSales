package com.PopCorp.Purchases.presentation.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.SaleChildCallback;
import com.PopCorp.Purchases.data.callback.SaleMainCallback;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.data.utils.UIL;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.controller.DialogController;
import com.PopCorp.Purchases.presentation.presenter.SaleInfoPresenter;
import com.PopCorp.Purchases.presentation.presenter.factory.SaleInfoPresenterFactory;
import com.PopCorp.Purchases.presentation.presenter.params.provider.SaleParamsProvider;
import com.PopCorp.Purchases.presentation.view.activity.InputListItemActivity;
import com.PopCorp.Purchases.presentation.view.activity.SaleActivity;
import com.PopCorp.Purchases.presentation.view.activity.SameSaleActivity;
import com.PopCorp.Purchases.presentation.view.adapter.SameSaleAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.SaleInfoView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SaleInfoFragment extends MvpAppCompatFragment
        implements
        Toolbar.OnMenuItemClickListener,
        SaleChildCallback,
        View.OnClickListener,
        SaleInfoView,
        SaleParamsProvider {

    private static final String CURRENT_SALE = "current_sale";

    @InjectPresenter(factory = SaleInfoPresenterFactory.class, presenterId = SaleInfoPresenter.PRESENTER_ID)
    SaleInfoPresenter presenter;

    private int saleId;

    private SaleMainCallback parent;

    private Toolbar toolBar;
    private ImageView image;
    private TextView title;
    private TextView subTitle;
    private TextView coast;
    private TextView quantity;
    private TextView shop;
    private TextView category;
    private TextView period;
    private TextView coastForQuantity;
    private View coastForQuantityLayout;
    private View coastLayout;
    private View quantityLayout;
    private View shopLayout;
    private View categoryLayout;
    private View periodLayout;

    private View sameSalesLayout;
    private RecyclerView sameSalesRecycler;

    private FloatingActionButton fab;


    public static Fragment create(SaleMainCallback parent, int saleId) {
        SaleInfoFragment result = new SaleInfoFragment();
        Bundle args = new Bundle();
        args.putInt(CURRENT_SALE, saleId);
        result.setArguments(args);
        result.setParent(parent);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        saleId = getArguments().getInt(CURRENT_SALE);
        super.onCreate(savedInstanceState);
        presenter.setSale(saleId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sale_info, container, false);

        toolBar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolBar.inflateMenu(R.menu.sale);
        toolBar.setOnMenuItemClickListener(this);
        toolBar.setNavigationOnClickListener(this);

        image = (ImageView) rootView.findViewById(R.id.image);

        title = (TextView) rootView.findViewById(R.id.title);
        subTitle = (TextView) rootView.findViewById(R.id.sub_title);

        coast = (TextView) rootView.findViewById(R.id.coast);
        coastLayout = rootView.findViewById(R.id.coast_layout);

        quantity = (TextView) rootView.findViewById(R.id.quantity);
        quantityLayout = rootView.findViewById(R.id.quantity_layout);

        shop = (TextView) rootView.findViewById(R.id.shop);
        shopLayout = rootView.findViewById(R.id.shop_layout);

        category = (TextView) rootView.findViewById(R.id.category);
        categoryLayout = rootView.findViewById(R.id.category_layout);

        period = (TextView) rootView.findViewById(R.id.period);
        periodLayout = rootView.findViewById(R.id.period_layout);

        coastForQuantity = (TextView) rootView.findViewById(R.id.coast_for_quantity);
        coastForQuantityLayout = rootView.findViewById(R.id.coast_for_quantity_layout);

        sameSalesRecycler = (RecyclerView) rootView.findViewById(R.id.same_sales_recycler);
        sameSalesLayout = rootView.findViewById(R.id.same_sales_layout);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(v -> presenter.loadShoppingLists());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        toolBar.setKeepScreenOn(PreferencesManager.getInstance().isDisplayNoOff());
        hideKeyboard();
    }

    @Override
    public void showInfo(Sale sale) {
        ImageLoader.getInstance().displayImage(sale.getImage(), image, UIL.getImageOptions());

        title.setText(sale.getTitle());
        if (sale.getSubTitle().isEmpty()) {
            subTitle.setVisibility(View.GONE);
        } else {
            subTitle.setVisibility(View.VISIBLE);
            subTitle.setText(sale.getSubTitle());
        }

        if (sale.getCoast().isEmpty()) {
            coastLayout.setVisibility(View.GONE);
        } else {
            coastLayout.setVisibility(View.VISIBLE);
            coast.setText(sale.getCoast());
        }

        if (sale.getQuantity().isEmpty()) {
            quantityLayout.setVisibility(View.GONE);
        } else {
            quantityLayout.setVisibility(View.VISIBLE);
            quantity.setText(sale.getQuantity());
        }

        if (sale.getShop().getName().isEmpty()) {
            shopLayout.setVisibility(View.GONE);
        } else {
            shopLayout.setVisibility(View.VISIBLE);
            shop.setText(sale.getShop().getName());
        }

        if (sale.getCategory().getName().isEmpty()) {
            categoryLayout.setVisibility(View.GONE);
        } else {
            categoryLayout.setVisibility(View.VISIBLE);
            category.setText(sale.getCategory().getName());
        }

        if (sale.getCoastForQuantity().isEmpty()) {
            coastForQuantityLayout.setVisibility(View.GONE);
        } else {
            coastForQuantityLayout.setVisibility(View.VISIBLE);
            coastForQuantity.setText(sale.getCoastForQuantity());
        }

        if (sale.getPeriodStart() == 0 && sale.getPeriodEnd() == 0) {
            periodLayout.setVisibility(View.GONE);
        } else {
            SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru"));
            periodLayout.setVisibility(View.VISIBLE);
            String periodString = format.format(new Date(sale.getPeriodStart()));
            if (sale.getPeriodStart() != sale.getPeriodEnd()) {
                periodString += " - " + format.format(new Date(sale.getPeriodEnd()));
            }
            period.setText(periodString);
        }
        showSameSales(sale);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(fab.getWindowToken(), 0);
    }

    @Override
    public void showError(Throwable e) {
        Snackbar.make(fab, R.string.empty_no_shopping_lists_short, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showSaleEmpty() {

    }

    @Override
    public void showEmptyLists() {
        Snackbar.make(fab, R.string.empty_no_shopping_lists_short, Snackbar.LENGTH_LONG)
                .setAction(R.string.button_create, view -> {
                    DialogController.showDialogForNewList(getActivity(), presenter);
                })
                .show();
    }

    @Override
    public void showListsSelecting(List<ShoppingList> shoppingLists) {
        ArrayList<String> items = new ArrayList<>();
        for (ShoppingList list : shoppingLists) {
            items.add(list.getName());
        }
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title(R.string.dialog_title_selecting_lists);
        builder.items(items);
        builder.positiveText(R.string.dialog_button_send);
        builder.negativeText(R.string.dialog_button_cancel);
        builder.autoDismiss(false);
        builder.itemsCallbackMultiChoice(null, (dialog, which, text) -> {
            if (which.length > 0) {
                presenter.listsSelected(which);
                dialog.dismiss();
            } else {
                showToast(R.string.error_select_lists);
            }
            return false;
        });
        builder.onNegative((dialog, which) -> dialog.dismiss());
        MaterialDialog dialog = builder.build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void openInputListItemFragment(ListItem item, long[] listsIds) {
        Intent intent = new Intent(getActivity(), InputListItemActivity.class);
        intent.putExtra(InputListItemActivity.CURRENT_LISTS, listsIds);
        intent.putExtra(InputListItemActivity.CURRENT_LISTITEM, item);
        startActivityForResult(intent, SaleActivity.REQUEST_CODE_FOR_INPUT_LISTITEM);
    }

    @Override
    public void showErrorLoadingLists(Throwable e) {
        Snackbar.make(toolBar, R.string.error_can_not_load_lists, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showItemAdded() {
        Snackbar.make(toolBar, R.string.notification_sale_sended_in_lists, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SaleActivity.REQUEST_CODE_FOR_INPUT_LISTITEM) {
                ListItem item = data.getParcelableExtra(InputListItemActivity.CURRENT_LISTITEM);
                if (item != null) {
                    presenter.onItemsRerurned(item);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setParent(SaleMainCallback parent) {
        this.parent = parent;
    }

    private void showToast(int error_select_lists) {
        Toast.makeText(getActivity(), error_select_lists, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        getActivity().onBackPressed();
    }

    @Override
    public String getSaleId(String presenterId) {
        return String.valueOf(saleId);
    }

    public void showSameSales(Sale sale) {
        if (sale.getSameSales().size() > 0) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            sameSalesRecycler.setLayoutManager(layoutManager);
            RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
            sameSalesRecycler.setItemAnimator(itemAnimator);
            SameSaleAdapter sameSaleAdapter = new SameSaleAdapter(getActivity(), presenter, sale.getSameSales());
            sameSalesRecycler.setAdapter(sameSaleAdapter);

            sameSalesLayout.setVisibility(View.VISIBLE);
        } else {
            sameSalesLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void openSameSale(View view, int saleId) {
        Intent intent = new Intent(getActivity(), SameSaleActivity.class);
        intent.putExtra(SameSaleActivity.CURRENT_SALE, String.valueOf(saleId));
        startActivity(intent);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareSale(presenter.getSale());
                break;
            case R.id.action_comments:
                parent.showComments();
                break;
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return false;
    }

    private void shareSale(Sale sale) {
        SimpleDateFormat format = new SimpleDateFormat("d MMMM", new Locale("ru"));
        File image = ImageLoader.getInstance().getDiskCache().get(sale.getImage());
        if (image == null) {
            Toast.makeText(getActivity(), R.string.toast_no_founded_sale_image, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        String string = getString(R.string.string_for_share_sale);
        string = string.replace("shop", sale.getShop().getName());
        string = string.replace("name", sale.getTitle());
        if (!sale.getSubTitle().isEmpty()) {
            string = string.replace("comment", sale.getSubTitle());
        } else {
            string = string.replace(" (comment)", "");
        }
        String periodBegin = format.format(sale.getPeriodStart());
        String periodFinish = format.format(sale.getPeriodEnd());
        string = string.replace("period", periodBegin.equals(periodFinish) ? periodBegin : "c " + periodBegin + " по " + periodFinish);
        string = string.replace("coast", sale.getCoast());

        shareIntent.putExtra(Intent.EXTRA_TEXT, string);

        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(image));
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(Intent.createChooser(shareIntent, getString(R.string.string_send_sale_with_app)));
        } catch (Exception e) {
            Toast.makeText(getActivity(), R.string.notification_no_apps_for_share_sale, Toast.LENGTH_SHORT).show();
        }
    }
}
