package com.xxds.musicplayer.modules.playing.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.xxds.musicplayer.modules.playing.ui.fragments.AlbumInfoFragment
import com.xxds.musicplayer.modules.playing.ui.fragments.LrcInfoFragment
import com.xxds.musicplayer.modules.playing.ui.fragments.SongInfoFragment

class PlayerPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    companion object {
        const val SONG_INFO = 0
        const val ALBUM_INFO = 1
    }

    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {

       return when(position) {

           SONG_INFO -> SongInfoFragment()
           ALBUM_INFO -> AlbumInfoFragment()
           else -> LrcInfoFragment()
        }
    }
}