package com.xxds.musicplayer.common.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init

class GridItemDecoration(context: Context, orientation: Int, spanCount: Int): RecyclerView.ItemDecoration() {

    private val ATTRS = intArrayOf(android.R.attr.listDivider)
    var mDivider: Drawable? = null

    private var mOrientation: Int = 0
    private var mSpanCount: Int = 0
    private var showSpanDivider = false
    private val mBounds = Rect()



    init {

        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        if (mDivider == null) {


        }
        a.recycle()
        setOrientation(orientation)
        setSpanCount(spanCount)
    }
    fun setOrientation(orientation: Int) {


        mOrientation = orientation
    }
    fun setDrawable(drawable: Drawable) {

        mDivider = drawable
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        if (parent.layoutManager == null || mDivider == null) {

            return
        }
        if (mOrientation == LinearLayout.VERTICAL){

            drawVertical(c,parent)
        } else {
            drawHorizontal(c,parent)
        }
    }
    fun setShowSpanDivider(showSpanDivider: Boolean) {
        this.showSpanDivider = showSpanDivider
    }

    fun setSpanCount(spanCount: Int) {
        this.mSpanCount = spanCount
    }
    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int

        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(left, parent.paddingTop, right,
                parent.height - parent.paddingBottom)
        } else {
            left = 0
            right = parent.width
        }

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            val bottom = mBounds.bottom + Math.round(child.translationY)
            val top = bottom - mDivider!!.intrinsicHeight
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(canvas)
        }
        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val top: Int
        val bottom: Int

        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(parent.paddingLeft, top,
                parent.width - parent.paddingRight, bottom)
        } else {
            top = 0
            bottom = parent.height
        }

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.layoutManager?.getDecoratedBoundsWithMargins(child, mBounds)
            val right = mBounds.right + Math.round(child.translationX)
            val left = right - mDivider!!.intrinsicWidth
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(canvas)
        }
        canvas.restore()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        if (mDivider == null) {
            outRect.set(0, 0, 0, 0)
            return
        }

        val current = parent.getChildLayoutPosition(view)

        if (mOrientation == LinearLayout.VERTICAL) {
            if (mSpanCount != 0 && (current + 1) % mSpanCount == 0 && !showSpanDivider) {
                outRect.set(0, 0, 0, 0)
            } else {

                outRect.set(0, 0, 0, mDivider!!.intrinsicHeight)
            }
        } else {
            if (mSpanCount != 0 && (current + 1) % mSpanCount == 0 && !showSpanDivider) {
                outRect.set(0, 0, 0, 0)
            } else {
                outRect.set(0, 0, mDivider!!.intrinsicWidth, 0)
            }
        }
    }



}