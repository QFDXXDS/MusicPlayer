package com.xxds.musicplayer.modules.playing.others

import android.view.View
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.xxds.musicplayer.modules.playing.ui.fragments.AlbumInfoFragment

class PlayingAlbumPageTransformer: ViewPager.PageTransformer {

    override fun transformPage(page: View, position: Float) {

        val pageWidth = page.width
        if (page.tag is String && page.tag.toString() == AlbumInfoFragment.TAG ) {

            when {
                position <= 0 -> {  //position -1~0

                    page.alpha = 1 + position
                    page.translationX = pageWidth * -position
                }
                position <= 1 -> {//position 0~1

                    page.alpha = 1 - position
                    page.translationX = pageWidth * -position
                }
            }

        }
    }
}