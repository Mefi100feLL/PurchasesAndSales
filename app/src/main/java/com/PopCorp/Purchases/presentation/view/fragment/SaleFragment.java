package com.PopCorp.Purchases.presentation.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.SaleChildCallback;
import com.PopCorp.Purchases.data.callback.SaleMainCallback;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.presenter.SalePresenter;
import com.PopCorp.Purchases.presentation.presenter.factory.SalePresenterFactory;
import com.PopCorp.Purchases.presentation.presenter.params.provider.SaleParamsProvider;
import com.PopCorp.Purchases.presentation.view.activity.SameSaleActivity;
import com.PopCorp.Purchases.presentation.view.moxy.SaleView;
import com.arellomobile.mvp.presenter.InjectPresenter;

public class SaleFragment extends MvpAppCompatFragment implements SaleMainCallback, SaleView, SaleParamsProvider {

    public static final String CURRENT_SALE = "current_sale";

    @InjectPresenter(factory = SalePresenterFactory.class, presenterId = "SalePresenter")
    SalePresenter presenter;

    private int saleId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        saleId = getArguments().getInt(SaleFragment.CURRENT_SALE);
        super.onCreate(savedInstanceState);
        presenter.setSale(saleId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sale, container, false);
    }

    @Override
    public void showFragmentComments(int saleId) {
        FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = new SaleCommentsFragment();
        Bundle args = new Bundle();
        args.putInt(SaleFragment.CURRENT_SALE, saleId);
        fragment.setArguments(args);
        ((SaleChildCallback) fragment).setParent(this);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, fragment, fragment.getClass().getSimpleName() + saleId)
                .commit();
    }

    @Override
    public void showFragmentInfo(int saleId) {
        FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = new SaleInfoFragment();
        Bundle args = new Bundle();
        args.putInt(SaleFragment.CURRENT_SALE, saleId);
        fragment.setArguments(args);
        ((SaleChildCallback) fragment).setParent(this);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, fragment, fragment.getClass().getSimpleName() + saleId)
                //.addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    public void showComments() {
        presenter.showComments();
    }

    @Override
    public void showInfo() {
        presenter.showInfo();
    }

    @Override
    public void openSameSale(View view, int saleId) {
        Intent intent = new Intent(getActivity(), SameSaleActivity.class);
        intent.putExtra(SameSaleActivity.CURRENT_SALE, String.valueOf(saleId));
        startActivity(intent);
    }

    @Override
    public String getSaleId(String presenterId) {
        return String.valueOf(saleId);
    }
}
