package com.acclex.verificationinput

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatEditText
import android.util.Log
import android.widget.TextView

/**
 * 作为验证码输入框的自定义的EditText
 *
 *
 * Created by acclex on 2018/12/31
 */
open class EditTextCustom(context: Context) : AppCompatEditText(context) {

    private var hasCustomBg: Boolean = false
    private lateinit var mPaint: Paint // 画笔
    // 下划线属性
    private var mClickColor: Int = 0
    private var mUnClickColor: Int = 0
    private var mColor: Int = 0
    private var mSpacing: Float = 0f
    private var mHeight: Float = 0f

    private lateinit var mNormalBG: Drawable
    private lateinit var mFocusBG: Drawable

    init {
        initView()
    }

    private fun initView() {
        mPaint = Paint()
    }

    /**
     * 设置是否有自定义的Drawable的背景
     *
     * @param hasCustomBg 是否有自定义的背景
     */
    fun setHasCustomBG(hasCustomBg: Boolean) {
        this.hasCustomBg = hasCustomBg
    }

    /**
     * 设置自定义的光标
     *
     * @param ResId 资源id
     */
    fun setCursor(ResId: Int) {
        try {
            // 1. 通过反射 获取光标属性
            val f = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            f.isAccessible = true
            // 2. 传入资源ID
            f.set(this, ResId)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
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
    fun setUnderline(clickColor: Int, unClickColor: Int, spacing: Float, height: Float) {
        background = null
        mClickColor = clickColor
        mUnClickColor = unClickColor
        mSpacing = spacing
        mHeight = height
        mColor = mUnClickColor
        invalidate()
    }

    /**
     * 设置聚焦时的背景和和非聚焦时的背景
     *
     * @param boxBgNormal 未聚焦的背景
     * @param boxBgFocus  聚焦时的背景
     */
    fun setBackGround(boxBgNormal: Drawable, boxBgFocus: Drawable) {
        mNormalBG = boxBgNormal
        mFocusBG = boxBgFocus
        background = mNormalBG
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (!hasCustomBg) setFocus(focused) else setBg(focused)
    }

    private fun setFocus(isFocus: Boolean) {
        mColor = if (isFocus) mClickColor else mUnClickColor
        invalidate()
    }

    private fun setBg(isFocus: Boolean) {
        background = if (isFocus) mFocusBG else mNormalBG

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPaint.color = mColor
        Log.d(javaClass.toString(), "Height = $measuredHeight Width =$measuredWidth")
        // 通过画矩形的方式去控制下划线的宽度高度等 如果下划线高度只有1的话 会过细导致颜色不清晰，因此默认会加1px的高度
        if (!hasCustomBg) {
            canvas.drawRect(
                0f, this.measuredHeight - mSpacing, (this.measuredWidth + this.scrollX).toFloat(),
                this.measuredHeight - mSpacing + mHeight, mPaint
            )
        }
    }
}
