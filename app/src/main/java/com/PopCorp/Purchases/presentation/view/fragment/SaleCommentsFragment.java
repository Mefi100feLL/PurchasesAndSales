package com.PopCorp.Purchases.presentation.view.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
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
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.presenter.SaleCommentsPresenter;
import com.PopCorp.Purchases.presentation.presenter.factory.SaleCommentsPresenterFactory;
import com.PopCorp.Purchases.presentation.presenter.params.provider.SaleParamsProvider;
import com.PopCorp.Purchases.presentation.utils.WindowUtils;
import com.PopCorp.Purchases.presentation.view.adapter.SaleCommentAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.SaleCommentsView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.mikepenz.materialdrawer.util.KeyboardUtil;

public class SaleCommentsFragment extends MvpAppCompatFragment
        implements
        View.OnClickListener,
        SaleChildCallback,
        SaleCommentsView,
        SaleParamsProvider {

    private static final String CURRENT_SALE = "current_sale";

    @InjectPresenter(factory = SaleCommentsPresenterFactory.class, presenterId = SaleCommentsPresenter.PRESENTER_ID)
    SaleCommentsPresenter presenter;

    private int saleId;

    private SaleMainCallback parent;

    private EditText commentText;
    private EditText commentAuthor;
    private View commentTopStroke;
    private TextInputLayout commentAuthorLayout;
    private TextInputLayout commentTextLayout;
    private Toolbar toolBar;
    private View snackBarLayout;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private View progressBar;
    private EmptyView emptyView;

    private SaleCommentAdapter adapter;
    private KeyboardUtil keyboardUtil;


    public static SaleCommentsFragment create(SaleMainCallback parent, int saleId){
        SaleCommentsFragment result = new SaleCommentsFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_sale_comments, container, false);

        toolBar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolBar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolBar.setNavigationOnClickListener(this);

        emptyView = new EmptyView(rootView);
        progressBar = rootView.findViewById(R.id.progress);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        snackBarLayout = rootView.findViewById(R.id.snackbar_layout);

        commentAuthor = (EditText) rootView.findViewById(R.id.comment_author);
        commentAuthorLayout = (TextInputLayout) rootView.findViewById(R.id.comment_author_layout);
        Button commentAuthorSave = (Button) rootView.findViewById(R.id.comment_save_author);
        commentText = (EditText) rootView.findViewById(R.id.comment_text);
        commentTextLayout = (TextInputLayout) rootView.findViewById(R.id.comment_text_layout);
        ImageView commentSend = (ImageView) rootView.findViewById(R.id.comment_send);
        commentTopStroke = rootView.findViewById(R.id.comment_top_stroke);
        View navigationBarView = rootView.findViewById(R.id.navigation_bar_view);
        navigationBarView.setVisibility(WindowUtils.isNavigationBarExists(getActivity()) && !WindowUtils.isLandscape(getActivity()) ? View.VISIBLE : View.GONE);

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            keyboardUtil = new KeyboardUtil(getActivity(), (View) commentText.getParent());
            commentText.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    keyboardUtil.enable();
                } else {
                    keyboardUtil.disable();
                }
            });
        }
        commentAuthor.setText(presenter.getSavedAuthorComment());
        commentSend.setBackgroundDrawable(createOvalSelector(ThemeManager.getInstance().getAccentColor()));
        commentSend.setOnClickListener(v -> presenter.sendComment(commentAuthor.getText().toString(), commentText.getText().toString()));

        commentAuthorSave.setOnClickListener(view -> {
            if (!commentAuthor.getText().toString().isEmpty()) {
                presenter.saveAuthor(commentAuthor.getText().toString());
            } else {
                showCommentAuthorEmpty();
            }
        });

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

    @Override
    public void onResume() {
        super.onResume();
        toolBar.setTitle(getString(R.string.comments));
        toolBar.setKeepScreenOn(PreferencesManager.getInstance().isDisplayNoOff());
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(commentText.getWindowToken(), 0);
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
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
        showError(R.string.empty_no_comments, R.drawable.ic_owl, R.string.button_add, view -> {
            commentText.requestFocus();
            showKeyboard();
        });
    }

    private void backToSale() {
        onClick(null);
    }

    @Override
    public void clearFields() {
        commentAuthor.setText("");
        commentText.setText("");
    }

    @Override
    public void showAuthorSaved() {
        Snackbar.make(snackBarLayout, R.string.notification_author_saved, Snackbar.LENGTH_SHORT).show();
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
    public void showError(Throwable e) {
        showError(ErrorManager.getErrorExpandedText(e, getActivity()), ErrorManager.getErrorImage(e), R.string.button_back_to_sale, view -> {
            backToSale();
        });
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
