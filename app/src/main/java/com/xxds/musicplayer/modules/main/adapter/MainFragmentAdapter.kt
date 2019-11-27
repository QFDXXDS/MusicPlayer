package com.xxds.musicplayer.modules.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.xxds.musicplayer.modules.main.ui.LocalFolderFragment
import com.xxds.musicplayer.modules.main.ui.MvpMineFragment
import com.xxds.musicplayer.modules.main.ui.MvpMusicFragment

class MainFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment = when(position) {

        0 -> MvpMineFragment()
        1 -> MvpMusicFragment()
        2 -> LocalFolderFragment()
        else -> LocalFolderFragment()
    }
}