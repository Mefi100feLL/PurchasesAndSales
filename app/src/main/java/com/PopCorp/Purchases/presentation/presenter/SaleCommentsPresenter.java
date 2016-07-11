package com.PopCorp.Purchases.presentation.presenter;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.comparator.SaleCommentComparator;
import com.PopCorp.Purchases.data.dao.SaleCommentDAO;
import com.PopCorp.Purchases.data.dto.CommentResult;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.model.SaleComment;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.domain.interactor.SaleCommentInteractor;
import com.PopCorp.Purchases.domain.interactor.SaleInteractor;
import com.PopCorp.Purchases.presentation.view.moxy.SaleCommentsView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class SaleCommentsPresenter extends MvpPresenter<SaleCommentsView> {

    private SaleCommentInteractor interactor = new SaleCommentInteractor();

    private int currentSaleId = -1;

    private ArrayList<SaleComment> objects = new ArrayList<>();

    public void setSale(int saleId) {
        if (currentSaleId == -1) {
            currentSaleId = saleId;
            loadData();
        }
    }

    private void loadData() {
        interactor.getData(currentSaleId)
                .subscribe(new Observer<List<SaleComment>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<SaleComment> comments) {
                        /*sale = result;
                        getViewState().showComments(sale);*/
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
        SaleComment comment = new SaleComment(sale.getId(), author, "", "", "", text, 0);
        comment.setTmpText(R.string.sending_comment);
        sale.getComments().add(comment);
        getViewState().showComments(sale);
        interactor.sendComment(author, "", text, sale.getCityId(), sale.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommentResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        comment.setError(ErrorManager.getErrorResource(e));
                        getViewState().showComments(sale);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(CommentResult commentResult) {
                        if (commentResult.isResult()) {
                            comment.setDate(commentResult.getDate());
                            comment.setTime(commentResult.getTime());
                            comment.setDateTime(commentResult.getDateTime());
                            new SaleCommentDAO().updateOrAddToDB(comment);
                            Collections.sort(sale.getComments(), new SaleCommentComparator());
                        } else{
                            comment.setErrorText(commentResult.getMessage());
                        }
                        getViewState().showComments(sale);
                    }
                });
    }

    public Sale getSale() {
        return sale;
    }
}
