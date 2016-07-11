package com.PopCorp.Purchases.presentation.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.RecyclerCallback;
import com.PopCorp.Purchases.data.callback.SaleChildCallback;
import com.PopCorp.Purchases.data.callback.SaleMainCallback;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.model.SameSale;
import com.PopCorp.Purchases.data.utils.UIL;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.presenter.SaleInfoPresenter;
import com.PopCorp.Purchases.presentation.presenter.factory.SaleInfoPresenterFactory;
import com.PopCorp.Purchases.presentation.presenter.params.provider.SaleParamsProvider;
import com.PopCorp.Purchases.presentation.view.adapter.SameSaleAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.SaleInfoView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class SaleInfoFragment extends MvpAppCompatFragment
        implements RecyclerCallback<SameSale>,
        Toolbar.OnMenuItemClickListener,
        SaleChildCallback,
        View.OnClickListener,
        SaleInfoView,
        SaleParamsProvider {

    @InjectPresenter(factory = SaleInfoPresenterFactory.class, presenterId = "SaleInfoPresenter")
    SaleInfoPresenter presenter;

    private int saleId;

    private SaleMainCallback parent;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        saleId = getArguments().getInt(SaleFragment.CURRENT_SALE);
        super.onCreate(savedInstanceState);
        presenter.setSale(saleId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sale_info, container, false);

        Toolbar toolBar = (Toolbar) rootView.findViewById(R.id.toolbar);
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

        return rootView;
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

        if (sale.getPeriodStart().isEmpty() && sale.getPeriodEnd().isEmpty()) {
            periodLayout.setVisibility(View.GONE);
        } else {
            periodLayout.setVisibility(View.VISIBLE);
            String periodString = sale.getPeriodStart();
            if (!sale.getPeriodStart().equals(sale.getPeriodEnd())) {
                periodString += " - " + sale.getPeriodEnd();
            }
            period.setText(periodString);
        }
        showSameSales(sale);
    }

    @Override
    public void setParent(SaleMainCallback parent) {
        this.parent = parent;
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
            SameSaleAdapter sameSaleAdapter = new SameSaleAdapter(getActivity(), this, sale.getSameSales());
            sameSalesRecycler.setAdapter(sameSaleAdapter);

            sameSalesLayout.setVisibility(View.VISIBLE);
        } else {
            sameSalesLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClicked(View view, SameSale item) {
        parent.openSameSale(view, item.getSaleId());
    }

    @Override
    public void onItemLongClicked(View view, SameSale item) {

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
        SimpleDateFormat parser = new SimpleDateFormat("dd.MM.yyyy", new Locale("ru"));
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
        String periodBegin;
        try {
            periodBegin = format.format(parser.parse(sale.getPeriodStart()));
        } catch (ParseException e) {
            e.printStackTrace();
            periodBegin = sale.getPeriodStart();
        }
        String periodFinish;
        try {
            periodFinish = format.format(parser.parse(sale.getPeriodEnd()));
        } catch (ParseException e) {
            e.printStackTrace();
            periodFinish = sale.getPeriodEnd();
        }
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
