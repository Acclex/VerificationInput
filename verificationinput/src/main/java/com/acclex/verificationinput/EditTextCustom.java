package com.acclex.verificationinput;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * 作为验证码输入框的自定义的EditText
 * <p>
 * Created by acclex on 2018/12/31
 */
public class EditTextCustom extends AppCompatEditText {

    private boolean hasCustomBg;
    private Paint mPaint; // 画笔
    // 下划线属性
    private int mClickColor, mUnClickColor;
    private int mColor;
    private float mSpacing;
    private float mHeight;

    private Drawable mNormalBG, mFocusBG;

    public EditTextCustom(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        mPaint = new Paint();
    }

    /**
     * 设置是否有自定义的Drawable的背景
     *
     * @param hasCustomBg 是否有自定义的背景
     */
    public void setHasCustomBG(boolean hasCustomBg) {
        this.hasCustomBg = hasCustomBg;
    }

    /**
     * 设置自定义的光标
     *
     * @param ResId 资源id
     */
    public void setCursor(final int ResId) {
        try {
            // 1. 通过反射 获取光标属性
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            // 2. 传入资源ID
            f.set(this, ResId);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置下划线的属性并绘制
     *
     * @param clickColor   点击时的颜色
     * @param unClickColor 没有点击时的颜色
     * @param spacing      下划线的间隔
     * @param height       下划线的高度
     */
    public void setUnderline(int clickColor, int unClickColor, float spacing, float height) {
        setBackground(null);
        mClickColor = clickColor;
        mUnClickColor = unClickColor;
        mSpacing = spacing;
        mHeight = height;
        mColor = mUnClickColor;
        invalidate();
    }

    /**
     * 设置聚焦时的背景和和非聚焦时的背景
     *
     * @param boxBgNormal 未聚焦的背景
     * @param boxBgFocus  聚焦时的背景
     */
    public void setBackGround(Drawable boxBgNormal, Drawable boxBgFocus) {
        mNormalBG = boxBgNormal;
        mFocusBG = boxBgFocus;
        setBackground(mNormalBG);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (!hasCustomBg) {
            setFocus(focused);
        } else {
            setBg(focused);
        }
    }

    private void setFocus(boolean isFocus) {
        mColor = isFocus ? mClickColor : mUnClickColor;
        invalidate();
    }

    private void setBg(boolean isFocus) {
        if (mNormalBG != null && !isFocus) {
            setBackground(mNormalBG);
        } else if (mFocusBG != null && isFocus) {
            setBackground(mFocusBG);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mColor);
        Log.d(getClass().toString(), "Height = " + getMeasuredHeight() + " Width =" + getMeasuredWidth());
        // 通过画矩形的方式去控制下划线的宽度高度等 如果下划线高度只有1的话 会过细导致颜色不清晰，因此默认会加1px的高度
        if (!hasCustomBg) {
            canvas.drawRect(0, this.getMeasuredHeight() - mSpacing + mHeight, this.getMeasuredWidth() + this.getScrollX(),
                    this.getMeasuredHeight() - mSpacing, mPaint);
        }
    }
}
