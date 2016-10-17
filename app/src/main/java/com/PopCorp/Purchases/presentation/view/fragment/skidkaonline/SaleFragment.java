package com.PopCorp.Purchases.presentation.view.fragment.skidkaonline;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.BackPressedCallback;
import com.PopCorp.Purchases.data.callback.SaleMainCallback;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.presenter.factory.skidkaonline.SalePresenterFactory;
import com.PopCorp.Purchases.presentation.presenter.params.provider.SaleParamsProvider;
import com.PopCorp.Purchases.presentation.presenter.skidkaonline.SalePresenter;
import com.PopCorp.Purchases.presentation.view.moxy.skidkaonline.SaleView;
import com.arellomobile.mvp.presenter.InjectPresenter;

public class SaleFragment extends MvpAppCompatFragment implements SaleMainCallback, SaleView, SaleParamsProvider, BackPressedCallback {

    private static final String CURRENT_SALE = "current_sale";
    private static final String IS_CURRENT = "is_current";
    private static final String EDIT_MODE = "edit_mode";

    @InjectPresenter(factory = SalePresenterFactory.class, presenterId = SalePresenter.PRESENTER_ID)
    SalePresenter presenter;

    private int saleId;
    private boolean showedComments = false;
    private boolean isCurrent;
    private boolean editMode;

    public static SaleFragment create(int saleId, boolean isCurrent, boolean editMode) {
        SaleFragment result = new SaleFragment();
        Bundle args = new Bundle();
        args.putInt(CURRENT_SALE, saleId);
        args.putBoolean(IS_CURRENT, isCurrent);
        args.putBoolean(EDIT_MODE, editMode);
        result.setArguments(args);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        saleId = getArguments().getInt(CURRENT_SALE);
        isCurrent = getArguments().getBoolean(IS_CURRENT, false);
        editMode = getArguments().getBoolean(EDIT_MODE, false);
        super.onCreate(savedInstanceState);
        presenter.setSale(saleId);
        presenter.setEditMode(getArguments().getBoolean(EDIT_MODE, false));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sale, container, false);
    }

    @Override
    public void showFragmentComments(int saleId) {
        showedComments = true;
        FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = SaleCommentsFragment.create(this, saleId, editMode);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    public void showFragmentInfo(int saleId) {
        showedComments = false;
        FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = SaleInfoFragment.create(this, saleId, isCurrent, editMode);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, fragment, fragment.getClass().getSimpleName())
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
    public String getSaleId(String presenterId) {
        return String.valueOf(saleId);
    }

    @Override
    public boolean onBackPressed() {
        if (showedComments){
            presenter.showInfo();
            return true;
        }
        return false;
    }
}