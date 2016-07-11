package com.PopCorp.Purchases.presentation.presenter.skidkaonline;


import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.domain.interactor.skidkaonline.SaleInteractor;
import com.PopCorp.Purchases.presentation.view.moxy.skidkaonline.SaleView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class SalePresenter extends MvpPresenter<SaleView> {

    private SaleInteractor interactor = new SaleInteractor();

    private Sale sale;

    public void setSale(int saleId) {
        if (sale == null) {
            interactor.getSale(Integer.valueOf(PreferencesManager.getInstance().getCity()), saleId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Sale>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Sale result) {
                            sale = result;
                            getViewState().showFragmentInfo(sale);
                            getViewState().showInfo(sale);
                        }
                    });
        }
    }

    public Sale getSale() {
        return sale;
    }

    public void sendComment(String author, String text) {
        /*if (text.isEmpty()) {
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
                });*/
    }

    public void showComments() {
        getViewState().showFragmentComments(sale);
    }

    public void showInfo() {
        getViewState().showFragmentInfo(sale);
    }
}