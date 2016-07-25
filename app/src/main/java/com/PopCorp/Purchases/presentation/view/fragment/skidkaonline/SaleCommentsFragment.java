package com.PopCorp.Purchases.presentation.view.fragment.skidkaonline;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.SaleChildCallback;
import com.PopCorp.Purchases.data.callback.SaleMainCallback;
import com.PopCorp.Purchases.data.utils.EmptyView;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.presenter.factory.skidkaonline.SaleCommentsPresenterFactory;
import com.PopCorp.Purchases.presentation.presenter.params.provider.SaleParamsProvider;
import com.PopCorp.Purchases.presentation.presenter.skidkaonline.SaleCommentsPresenter;
import com.PopCorp.Purchases.presentation.view.adapter.skidkaonline.SaleCommentAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.skidkaonline.SaleCommentsView;
import com.arellomobile.mvp.presenter.InjectPresenter;

public class SaleCommentsFragment extends MvpAppCompatFragment
        implements View.OnClickListener,
        SaleChildCallback,
        SaleCommentsView,
        SaleParamsProvider {

    @InjectPresenter(factory = SaleCommentsPresenterFactory.class, presenterId = "SaleCommentsPresenter")
    SaleCommentsPresenter presenter;

    private int saleId;

    private SaleMainCallback parent;

    private Toolbar toolBar;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private View progressBar;
    private EmptyView emptyView;

    private EditText commentText;
    private Button commentAuthorSave;
    private EditText commentAuthor;
    private View commentTopStroke;
    private TextInputLayout commentAuthorLayout;
    private TextInputLayout commentTextLayout;

    private SaleCommentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        saleId = getArguments().getInt(SaleFragment.CURRENT_SALE);
        super.onCreate(savedInstanceState);
        presenter.setSale(saleId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_skidkaonline_sale_comments, container, false);

        Toolbar toolBar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolBar.setTitle(getString(R.string.comments));
        toolBar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolBar.setNavigationOnClickListener(this);

        emptyView = new EmptyView(rootView);
        progressBar = rootView.findViewById(R.id.progress);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);

        commentAuthor = (EditText) rootView.findViewById(R.id.comment_author);
        commentAuthorLayout = (TextInputLayout) rootView.findViewById(R.id.comment_author_layout);
        commentAuthorSave = (Button) rootView.findViewById(R.id.comment_save_author);
        commentText = (EditText) rootView.findViewById(R.id.comment_text);
        commentTextLayout = (TextInputLayout) rootView.findViewById(R.id.comment_text_layout);
        ImageView commentSend = (ImageView) rootView.findViewById(R.id.comment_send);
        commentTopStroke = rootView.findViewById(R.id.comment_top_stroke);

        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (commentText.length() > 0) {
                    commentTopStroke.setVisibility(View.VISIBLE);
                } else {
                    commentTopStroke.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        commentSend.setBackgroundDrawable(createOvalSelector(ThemeManager.getInstance().getAccentColor()));
        commentSend.setOnClickListener(v -> presenter.sendComment(commentAuthor.getText().toString(), commentText.getText().toString()));

        swipeRefresh.setColorSchemeResources(R.color.swipe_refresh_color_one, R.color.swipe_refresh_color_two, R.color.swipe_refresh_color_three);
        swipeRefresh.setOnRefreshListener(presenter::onRefresh);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        adapter = new SaleCommentAdapter(getActivity(), presenter, presenter.getObjects());
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(commentText.getWindowToken(), 0);
    }

    public static Drawable createOvalSelector(int color) {
        ShapeDrawable coloredCircle = new ShapeDrawable(new OvalShape());
        coloredCircle.getPaint().setColor(color);
        ShapeDrawable darkerCircle = new ShapeDrawable(new OvalShape());
        darkerCircle.getPaint().setColor(ThemeManager.shiftColor(color));

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{-android.R.attr.state_pressed}, coloredCircle);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, darkerCircle);
        return stateListDrawable;
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        parent.showInfo();
    }

    @Override
    public void setParent(SaleMainCallback parent) {
        this.parent = parent;
    }

    @Override
    public String getSaleId(String presenterId) {
        return String.valueOf(saleId);
    }

    @Override
    public void showCommentTextEmpty() {
        commentTextLayout.setErrorEnabled(true);
        commentTextLayout.setError(getString(R.string.error_input_text_of_comment));
    }

    @Override
    public void hideCommentTextError() {
        commentTextLayout.setErrorEnabled(false);
    }

    @Override
    public void showCommentAuthorEmpty() {
        commentAuthorLayout.setErrorEnabled(true);
        commentAuthorLayout.setError(getString(R.string.error_input_author_of_comment));
    }

    @Override
    public void hideCommentAuthorError() {
        commentAuthorLayout.setErrorEnabled(false);
    }

    @Override
    public void showCommentsEmpty() {
        showError(R.string.empty_comments_for_sale, R.drawable.ic_menu_gallery, R.string.button_back_to_sale, v -> {
            onClick(null);
        });
    }

    @Override
    public void clearFields() {
        commentAuthor.setText("");
        commentText.setText("");
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        emptyView.hide();
    }

    @Override
    public void showData() {
        adapter.update();
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
        swipeRefresh.setRefreshing(refresh);
        swipeRefresh.setEnabled(!refresh);
    }

    @Override
    public void showSnackBar(Throwable e) {
        Snackbar.make(recyclerView, ErrorManager.getErrorText(e, getActivity()), Snackbar.LENGTH_SHORT).show();
    }
}