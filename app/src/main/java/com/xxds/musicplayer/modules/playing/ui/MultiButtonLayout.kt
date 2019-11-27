package com.xxds.musicplayer.modules.playing.ui

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.widget.LinearLayout

class MultiButtonLayout: LinearLayout {
    companion object {
        private const val DEFAULT_BUTTON_COUNT = 0
        private const val DEFAULT_BUTTON_RADIUS = 8
    }
    private var buttonCount = DEFAULT_BUTTON_COUNT
    private var buttonRadius = DEFAULT_BUTTON_RADIUS

    private var buttonSolid: ColorStateList? = null
    private var buttonSelectedColor: ColorStateList? = null

    private var buttonMarginLeft = 0
    private var buttonMarginRight = 0


    constructor(context: Context): this(context,null)

    constructor(context: Context, attrs: AttributeSet?): this(context,attrs,0)

    constructor(context: Context,attrs: AttributeSet?,defStyleAttr: Int): super(context,attrs,defStyleAttr){



        init()
    }

    fun init() {

    }


    fun setSelectedChild(index: Int) {

        for (i in 0 until childCount) {

            val child = this.getChildAt(i)
            child.isSelected = i == index

        }

    }
}