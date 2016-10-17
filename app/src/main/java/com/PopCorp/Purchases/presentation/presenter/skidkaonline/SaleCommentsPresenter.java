package com.PopCorp.Purchases.presentation.presenter.skidkaonline;

import android.view.View;

import com.PopCorp.Purchases.data.analytics.AnalyticsTrackers;
import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.RecyclerCallback;
import com.PopCorp.Purchases.data.model.skidkaonline.SaleComment;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.domain.interactor.skidkaonline.SaleCommentInteractor;
import com.PopCorp.Purchases.presentation.view.moxy.skidkaonline.SaleCommentsView;
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

    private SaleCommentInteractor interactor = new SaleCommentInteractor();

    private int currentSaleId = -1;

    private ArrayList<SaleComment> objects = new ArrayList<>();
    private boolean editMode;

    public SaleCommentsPresenter(){
        getViewState().showProgress();
    }

    public void setSale(int saleId) {
        if (currentSaleId == -1) {
            currentSaleId = saleId;
            loadData();
        }
    }

    private void loadData(){
        interactor.getData(currentSaleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SaleComment>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().refreshing(false);
                        AnalyticsTrackers.getInstance().sendError(e);
                        ErrorManager.printStackTrace(e);
                        if (objects.size() == 0) {
                            getViewState().showError(e);
                        } else {
                            getViewState().showSnackBar(e);
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
        SaleComment comment = new SaleComment(currentSaleId, author, "", Calendar.getInstance().getTimeInMillis(), text);
        sendComment(comment);
    }

    private void sendComment(SaleComment comment) {
        comment.setError(0);
        comment.setErrorText(null);
        comment.setTmpText(R.string.sending_comment);
        objects.add(comment);
        getViewState().showData();
        interactor.sendComment(comment.getAuthor(), comment.getText(), Integer.parseInt(PreferencesManager.getInstance().getCity()), currentSaleId)
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
                    public void onNext(SaleComment commentResult) {
                        if (commentResult != null){
                            objects.remove(comment);
                            objects.add(commentResult);
                        } else {
                            comment.setError(R.string.error_comment_no_sended);
                        }
                        getViewState().showData();
                    }
                });
    }

    public ArrayList<SaleComment> getObjects() {
        return objects;
    }

    public void onRefresh() {
        getViewState().refreshing(true);
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

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }
}
