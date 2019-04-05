package harmanbeer007.easylibrary.easyautocompleteview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;

import harmanbeer007.easylibrary.easyautocompleteview.exceptions.NoModelDefinedException;
import harmanbeer007.easylibrary.easyautocompleteview.exceptions.NoSearchParamDefinedException;
import harmanbeer007.easylibrary.easyautocompleteview.utils.CustomLog;


/**
 * Created by harman.
 */
public class EasyAutoCompleteView extends AutoCompleteTextView {
    private static final int MESSAGE_TEXT_CHANGED = 100;
    private static final int DEFAULT_AUTOCOMPLETE_DELAY = 750;
    private int mAutoCompleteDelay = DEFAULT_AUTOCOMPLETE_DELAY;
    private String mAutocompleteUrl, mModelClassName, mSearchParam;
    private int mLayoutId;
    private AutoCompleteResponseParser mParser;
    private RequestDispatcher mRequestDispatcher;
    private AutoCompleteAdapter mAdapter;
    private AutoCompleteItemSelectionListener mListener;
    private View mLoadingIndicator;
    private int mRequestMethod;
    private boolean mEnableLog;

    public interface AutoCompleteResponseParser {
        public ArrayList<? extends Object> parseAutoCompleteResponse(String response);
    }

    public interface AutoCompleteItemSelectionListener {
        public void onItemSelection(Object obj);
    }

    public interface RequestDispatcher {
        /*Important Note: getResponse() is called in worker thread.*/
        public String getResponse();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            EasyAutoCompleteView.super.performFiltering((CharSequence) msg.obj, msg.arg1);
        }
    };

    public EasyAutoCompleteView(Context context) {
        super(context);
        setAdapter();
    }

    public EasyAutoCompleteView(Context context, AttributeSet attrs) throws NoModelDefinedException, NoSearchParamDefinedException {
        super(context, attrs);
        initAttrs(context, attrs);
        setAdapter();
    }

    public EasyAutoCompleteView(Context context, AttributeSet attrs, int defStyleAttr) throws NoModelDefinedException, NoSearchParamDefinedException {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        setAdapter();
    }

    @TargetApi(21)
    public EasyAutoCompleteView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) throws NoModelDefinedException, NoSearchParamDefinedException {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
        setAdapter();
    }

    public void setSelectionListener(AutoCompleteItemSelectionListener listener) {
        this.mListener = listener;
        if (mAdapter != null) mAdapter.setItemSelectionListener(listener);
    }

    public void setParser(AutoCompleteResponseParser parser) {
        this.mParser = parser;
        if (mAdapter != null) mAdapter.setParser(parser);
    }

    public void setRequestDispatcher(RequestDispatcher requestDispatcher) {
        this.mRequestDispatcher = requestDispatcher;
        if (mAdapter != null) mAdapter.setRequestDispatcher(requestDispatcher);
    }

    private void initAttrs(Context context, AttributeSet attrs) throws NoModelDefinedException, NoSearchParamDefinedException {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EasyAutoCompleteView);
            mAutocompleteUrl = a.getString(R.styleable.EasyAutoCompleteView_autocompleteUrl);
            mSearchParam = a.getString(R.styleable.EasyAutoCompleteView_autocompleteParam);
            mRequestMethod = a.getInteger(R.styleable.EasyAutoCompleteView_method, 1);
            mEnableLog = a.getBoolean(R.styleable.EasyAutoCompleteView_method, true);
            mModelClassName = a.getString(R.styleable.EasyAutoCompleteView_modelClass);
            mLayoutId = a.getResourceId(R.styleable.EasyAutoCompleteView_rowLayout, android.R.layout.simple_dropdown_item_1line);
        }
        if (mEnableLog) {
            CustomLog.initialize(true, "EasyAutoCompleteLog");
        } else {
            CustomLog.initialize(false, "EasyAutoCompleteLog");
        }
        if (mModelClassName == null) {
            throw new NoModelDefinedException();
        }
        if (mSearchParam == null) {
            throw new NoSearchParamDefinedException();
        }
    }

    public void setAutocompleteUrl(String mAutocompleteUrl) {
        this.mAutocompleteUrl = mAutocompleteUrl;
    }

    public void setModelClassName(String mModelClassName) {
        this.mModelClassName = mModelClassName;
    }

    public void setLoadingIndicator(View loadingIndicator) {
        this.mLoadingIndicator = loadingIndicator;
    }

    public void setAutoCompleteDelay(int autoCompleteDelay) {
        mAutoCompleteDelay = autoCompleteDelay;
    }

    private void setAdapter() {
        mAdapter = new AutoCompleteAdapter(getContext(), mSearchParam, mRequestMethod, mModelClassName, mAutocompleteUrl, mParser, mLayoutId);
        setAdapter(mAdapter);
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        if (mLoadingIndicator != null) mLoadingIndicator.setVisibility(View.VISIBLE);
        mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, text), mAutoCompleteDelay);
    }

    @Override
    public void onFilterComplete(int count) {
        if (mLoadingIndicator != null) mLoadingIndicator.setVisibility(View.GONE);
        super.onFilterComplete(count);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isPopupShowing()) {
            InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (inputManager.hideSoftInputFromWindow(findFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS)) {
                return true;
            }
        }

        return super.onKeyPreIme(keyCode, event);
    }

}
