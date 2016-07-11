package com.PopCorp.Purchases.presentation.presenter.skidkaonline;

import android.view.View;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.RecyclerCallback;
import com.PopCorp.Purchases.data.dao.skidkaonline.CityDAO;
import com.PopCorp.Purchases.data.model.skidkaonline.SaleComment;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.domain.interactor.skidkaonline.SaleInteractor;
import com.PopCorp.Purchases.presentation.view.moxy.skidkaonline.SaleCommentsView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class SaleCommentsPresenter extends MvpPresenter<SaleCommentsView> implements RecyclerCallback<SaleComment> {

    private SaleInteractor interactor = new SaleInteractor();
    private CityDAO cityDAO = new CityDAO();

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

    private void loadData(){
        interactor.getComments(currentSaleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SaleComment>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().refreshing(false);
                        e.printStackTrace();
                        getViewState().showSnackBar(ErrorManager.getErrorResource(e));
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

    @Override
    public void onItemClicked(View view, SaleComment item) {

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
        SaleComment comment = new SaleComment(currentSaleId, author, "Только что", text, cityDAO.getWithId(PreferencesManager.getInstance().getCity()).getName());
        comment.setTmpText(R.string.sending_comment);
        objects.add(comment);
        getViewState().showData();
        /*interactor.sendComment(author, text, Integer.parseInt(PreferencesManager.getInstance().getCity()), currentSaleId)
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
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(SaleComment commentResult) {
                        if (commentResult != null){
                            SaleComment exists = objects.get(objects.indexOf(commentResult));
                            exists.setError();
                        }
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

    public ArrayList<SaleComment> getObjects() {
        return objects;
    }

    public void onRefresh() {
        getViewState().refreshing(true);
        loadData();
    }
}
