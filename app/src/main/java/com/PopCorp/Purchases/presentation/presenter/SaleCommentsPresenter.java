package com.PopCorp.Purchases.presentation.presenter;

import android.view.View;

import com.PopCorp.Purchases.AnalyticsTrackers;
import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.RecyclerCallback;
import com.PopCorp.Purchases.data.dao.SaleCommentDAO;
import com.PopCorp.Purchases.data.model.SaleComment;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.domain.interactor.SaleCommentInteractor;
import com.PopCorp.Purchases.presentation.view.moxy.SaleCommentsView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class SaleCommentsPresenter extends MvpPresenter<SaleCommentsView> implements RecyclerCallback<SaleComment> {

    public static final String PRESENTER_ID = "SaleCommentsPresenter";

    private SaleCommentInteractor interactor = new SaleCommentInteractor();

    private int currentSaleId = -1;

    private ArrayList<SaleComment> objects = new ArrayList<>();


    public SaleCommentsPresenter(){
        getViewState().showProgress();
    }

    public void setSale(int saleId) {
        if (currentSaleId == -1) {
            currentSaleId = saleId;
            loadData();
        }
    }

    private void loadData() {
        interactor.getData(currentSaleId, Integer.parseInt(PreferencesManager.getInstance().getRegionId()))
                .subscribe(new Observer<List<SaleComment>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().refreshing(false);
                        AnalyticsTrackers.getInstance().sendError(e);
                        ErrorManager.printStackTrace(e);
                        if (objects.size() > 0) {
                            getViewState().showSnackBar(e);
                        } else {
                            getViewState().showError(e);
                        }
                    }

                    @Override
                    public void onNext(List<SaleComment> comments) {
                        getViewState().refreshing(false);
                        if (comments.size() == 0) {
                            getViewState().showCommentsEmpty();
                        } else {
                            objects.clear();
                            objects.addAll(comments);
                            getViewState().showData();
                        }
                    }
                });
    }

    public void sendComment(String author, String text) {
        if (text.isEmpty()) {
            getViewState().showCommentTextEmpty();
            return;
        } else {
            getViewState().hideCommentTextError();
        }
        if (author.isEmpty()) {
            getViewState().showCommentAuthorEmpty();
            return;
        } else {
            getViewState().hideCommentAuthorError();
        }
        getViewState().clearFields();
        Calendar dateTime = Calendar.getInstance();
        SaleComment comment = new SaleComment(currentSaleId, author, "", text, dateTime.getTimeInMillis());
        sendComment(comment);
    }

    private void sendComment(SaleComment comment){
        comment.setError(0);
        comment.setErrorText(null);
        comment.setTmpText(R.string.sending_comment);
        objects.add(comment);
        getViewState().showData();
        interactor.sendComment(comment.getAuthor(), "", comment.getText(), Integer.parseInt(PreferencesManager.getInstance().getRegionId()), currentSaleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SaleComment>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        comment.setError(ErrorManager.getErrorResource(e));
                        getViewState().showData();
                        AnalyticsTrackers.getInstance().sendError(e);
                        ErrorManager.printStackTrace(e);
                    }

                    @Override
                    public void onNext(SaleComment saleComment) {
                        if (saleComment != null) {
                            comment.setDateTime(saleComment.getDateTime());
                            comment.setError(0);
                            comment.setErrorText(null);
                            comment.setTmpText(0);
                            new SaleCommentDAO().updateOrAddToDB(comment);
                        }
                        getViewState().showData();
                    }
                });
    }

    public void onRefresh() {
        getViewState().showProgress();
        loadData();
    }

    @Override
    public void onItemClicked(View view, SaleComment item) {
        if (item.getErrorText() != null || item.getError() != 0){
            sendComment(item);
        }
    }

    @Override
    public void onItemLongClicked(View view, SaleComment item) {

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

    public void saveAuthor(String author) {
        PreferencesManager.getInstance().putAuthorComment(author);
        getViewState().showAuthorSaved();
    }

    public String getSavedAuthorComment() {
        return PreferencesManager.getInstance().getAuthorCOmment();
    }

    public ArrayList<SaleComment> getObjects() {
        return objects;
    }
}
