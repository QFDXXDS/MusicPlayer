package com.xxds.musicplayer.modules.playing.ui

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager
import java.util.jar.Attributes

class PlayingViewPager: ViewPager {

    constructor(context: Context,attrs: AttributeSet?): super(context,attrs)

    constructor(context: Context):this(context,null)

    override fun getChildDrawingOrder(childCount: Int, i: Int): Int {

        return if (childCount >= 3) {

            when(i){

                childCount - 1 -> childCount -2
                childCount - 2 -> childCount -1
                else -> i
            }

        } else {

              super.getChildDrawingOrder(childCount, i)
        }


    }


}