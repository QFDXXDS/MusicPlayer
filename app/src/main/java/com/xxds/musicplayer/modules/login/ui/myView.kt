package com.xxds.cjs.modules.login.ui

import android.content.Context
import android.graphics.Canvas
import android.view.View

class myView(context: Context):View(context) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        并在里面调用super.onMeasure()，触发原有的自我测量。
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        用getMeasuredWidth()和getMeasuredHeight()获取到之前的测量结果，
            measuredHeight
            measuredWidth
//          MeasureSpec.getMode(widthMeasureSpec)
//          setMeasuredDimension()
    }
//    只有ViewGroup需要重写onLayout()。
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
    }

}