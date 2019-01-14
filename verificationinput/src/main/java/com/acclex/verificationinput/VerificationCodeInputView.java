package com.acclex.verificationinput;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;


/**
 * 验证码输入框自定义view
 *
 * @author Acclex
 * @Date 2018/12/3
 */
public class VerificationCodeInputView extends ViewGroup {
    private final String TAG = getClass().toString();
    private final static String TYPE_NUMBER = "number";
    private final static String TYPE_TEXT = "text";
    private final static String TYPE_PASSWORD = "password";
    private final static String TYPE_PHONE = "phone";

    private int mBox = 4;
    private int mBoxWidth = 120;
    private int mBoxHeight = 120;
    private int mChildHorizontalPadding = 14;
    private int mChildVerticalPadding = 14;
    private String inputType = TYPE_NUMBER;
    private int mFocusColor;
    private int mUnFocusColor;
    private int mCursor;
    private float mUnderLineHeight;
    private float mSpacing;
    private int mTextColor;
    private float mTextSize;
    private Drawable mBoxBgNormal = null;
    private Drawable mBoxBgFocus = null;
    private CompleteListener completeListener;
    private PasteListener pasteListener;

    public VerificationCodeInputView(Context context) {
        super(context);
        initAttribute(context, null);
    }

    public VerificationCodeInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttribute(context, attrs);
    }

    public VerificationCodeInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttribute(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VerificationCodeInputView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttribute(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void initAttribute(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeInputView);
        mBox = array.getInteger(R.styleable.VerificationCodeInputView_box, 4);
        mBoxWidth = (int) array.getDimension(R.styleable.VerificationCodeInputView_child_width, mBoxWidth);
        mBoxHeight = (int) array.getDimension(R.styleable.VerificationCodeInputView_child_height, mBoxHeight);
        mChildHorizontalPadding = (int) array.getDimension(R.styleable.VerificationCodeInputView_child_horizontal_padding, mChildHorizontalPadding);
        mChildVerticalPadding = (int) array.getDimension(R.styleable.VerificationCodeInputView_child_vertical_padding, mChildVerticalPadding);


        inputType = array.getString(R.styleable.VerificationCodeInputView_input_type);
        mTextColor = array.getColor(R.styleable.VerificationCodeInputView_text_color, Color.BLACK);
        mTextSize = array.getFloat(R.styleable.VerificationCodeInputView_text_size, 13f);

        mBoxBgNormal = array.getDrawable(R.styleable.VerificationCodeInputView_box_bg_normal);
        mBoxBgFocus = array.getDrawable(R.styleable.VerificationCodeInputView_box_bg_focus);

        mFocusColor = array.getColor(R.styleable.VerificationCodeInputView_underline_focus_color,
                ContextCompat.getColor(context, R.color.color_47555f));
        mUnFocusColor = array.getColor(R.styleable.VerificationCodeInputView_underline_normal_color,
                ContextCompat.getColor(context, R.color.color_b5bec4));
        mCursor = array.getResourceId(R.styleable.VerificationCodeInputView_cursor, R.drawable.shape_verification_cursor);
        mUnderLineHeight = array.getDimension(R.styleable.VerificationCodeInputView_underline_height, 3);
        mSpacing = array.getDimension(R.styleable.VerificationCodeInputView_underline_space, 10);
        initViews();
        array.recycle();
    }

    private void initViews() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    focus();
                    checkAndCommit();
                }
            }
        };

        OnKeyListener onKeyListener = new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    backFocus();
                } else if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
                    toNext(keyCode);
                }
                return false;
            }
        };

        // 加载这个ViewGroup内的EditText
        for (int i = 0; i < mBox; i++) {
            EditTextCustom editText;
            editText = new EditTextCustom(getContext()) {
                @Override
                public boolean onTextContextMenuItem(int id) {
                    if (id == android.R.id.paste) {
                        ClipboardManager clip = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        if (clip != null) {
                            ClipData data = clip.getPrimaryClip();
                            if (data != null) {
                                if (pasteListener != null) {
                                    pasteListener.onPaste(data, mBox);
                                }
                            }
                        }
                    }
                    return super.onTextContextMenuItem(id);
                }
            };
            if (mBoxBgNormal == null || mBoxBgFocus == null) {
                editText.setHasCustomBG(false);
                editText.setUnderline(mFocusColor, mUnFocusColor, mSpacing, mUnderLineHeight);
            } else {
                editText.setHasCustomBG(true);
                editText.setBackGround(mBoxBgNormal, mBoxBgFocus);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mBoxWidth, mBoxHeight);
            params.setMargins(mChildHorizontalPadding, mChildVerticalPadding, mChildHorizontalPadding, mChildVerticalPadding);
            params.gravity = Gravity.CENTER;

            editText.setCursor(mCursor);
            editText.setTextColor(mTextColor);
            editText.setTypeface(Typeface.DEFAULT_BOLD);
            editText.setLayoutParams(params);
            editText.setGravity(Gravity.CENTER);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            editText.setId(i);
            editText.setTextSize(mTextSize);
            editText.setClickable(false);

            // EditText的Listener
            editText.setOnKeyListener(onKeyListener);
            editText.addTextChangedListener(textWatcher);

            if (TYPE_NUMBER.equals(inputType)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (TYPE_PASSWORD.equals(inputType)) {
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else if (TYPE_TEXT.equals(inputType)) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (TYPE_PHONE.equals(inputType)) {
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
            }

            addView(editText, i);
        }
    }

    /**
     * 当前焦点的EditText输入后，跳转到下一个空的EditText并设置Focus
     */
    private void focus() {
        EditText editText;
        for (int i = 0; i < mBox; i++) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() < 1) {
                editText.requestFocus();
                return;
            }
        }
    }

    /**
     * 检查并合并文本
     */
    private void checkAndCommit() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean full = true;
        for (int i = 0; i < mBox; i++) {
            EditText editText = (EditText) getChildAt(i);
            String content = editText.getText().toString();
            if (content.length() == 0) {
                full = false;
                break;
            } else {
                stringBuilder.append(content);
            }

        }
        if (full) {
            if (completeListener != null) {

                completeListener.onComplete(stringBuilder.toString());
            }

        }
    }

    /**
     * 退格删除再输入的情况下，如果当前选中的EditText是已经有文本了，那么直接跳到下一个EditText并填充文本
     *
     * @param keyCode 来自键盘输入的keyCode
     */
    private void toNext(int keyCode) {
        EditText editText;
        EditText nextText;
        for (int i = 0; i < mBox; i++) {
            editText = (EditText) getChildAt(i);
            if (editText != null && editText.isFocused() && editText.hasFocus()) {
                if (editText.getText().length() > 0) {
                    if (i + 1 < mBox) {
                        nextText = (EditText) getChildAt(i + 1);
                        nextText.requestFocus();
                        nextText.setText(KeyEventToString.getInstance().getStringFromEvent(keyCode));
                        nextText.setSelection(1);
                        return;
                    }
                }
            }
        }
    }

    /**
     * 按下退格时返回清除并返回上一格EditText
     */
    private void backFocus() {
        EditText editText;
        for (int i = mBox - 1; i >= 0; i--) {
            editText = (EditText) getChildAt(i);
            if (editText != null && editText.getText().length() == 1) {
                editText.requestFocus();
                editText.setSelection(0);
                editText.setText("");
                return;
            }
        }
    }

    /**
     * 复制并将文本填充到验证码输入框内
     *
     * @param c 复制截取的文本
     */
    public void paste(CharSequence c) {
        EditText editText;
        for (int i = 0; i < mBox; i++) {
            editText = (EditText) getChildAt(i);
            editText.setText(String.valueOf(c.charAt(i)));
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        for (int i = 0; i < mBox; i++) {
            View child = getChildAt(i);
            child.setEnabled(enabled);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LinearLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0; i < mBox; i++) {
            View child = getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        if (mBox > 0) {
            View child = getChildAt(0);
            int cHeight = child.getMeasuredHeight();
            int cWidth = child.getMeasuredWidth();
            int maxH = cHeight + 2 * mChildVerticalPadding;
            int maxW = (cWidth + mChildHorizontalPadding) * mBox + mChildHorizontalPadding;
            setMeasuredDimension(resolveSize(maxW, widthMeasureSpec),
                    resolveSize(maxH, heightMeasureSpec));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < mBox; i++) {
            View child = getChildAt(i);
            child.setVisibility(View.VISIBLE);
            int cWidth = child.getMeasuredWidth();
            int cHeight = child.getMeasuredHeight();
            int cl = (i) * (cWidth + mChildHorizontalPadding);
            int cr = cl + cWidth;
            int ct = mChildVerticalPadding;
            int cb = ct + cHeight;
            child.layout(cl, ct, cr, cb);
        }
    }

    /**
     * 设置CompleteListener的回调
     *
     * @param listener CompleteListener的回调
     */
    public void setOnCompleteListener(CompleteListener listener) {
        this.completeListener = listener;
    }

    /**
     * 设置PasteListener的回调
     *
     * @param pasteListener PasteListener的回调
     */
    public void setPasteListener(PasteListener pasteListener) {
        this.pasteListener = pasteListener;
    }

    /**
     * 清空验证码输入框，并且focus第一个输入框
     */
    public void restNullCode() {
        EditText editText;
        for (int i = mBox - 1; i >= 0; i--) {
            editText = (EditText) getChildAt(i);
            editText.setText("");
        }
        getChildAt(0).requestFocus();
    }

    /**
     * 验证码输入框填完后的接口
     */
    public interface CompleteListener {
        void onComplete(String content);
    }

    /**
     * 粘贴时触发的回调接口
     */
    public interface PasteListener {
        /**
         * 粘贴触发的回调函数
         *
         * @param data 粘贴板的数据
         */
        void onPaste(ClipData data, int box);
    }
}
