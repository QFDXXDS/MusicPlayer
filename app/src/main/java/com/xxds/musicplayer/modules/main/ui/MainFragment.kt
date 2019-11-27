package com.xxds.musicplayer.modules.main.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.xxds.musicplayer.modules.bases.ui.BaseFragment
import com.xxds.musicplayer.R
import com.xxds.musicplayer.modules.main.adapter.MainFragmentAdapter
import kotlinx.android.synthetic.main.fragment_app_main.*
import org.jetbrains.anko.support.v4.onPageChangeListener

class MainFragment(): BaseFragment(),ViewPager.OnPageChangeListener,View.OnClickListener  {

    companion object {
        const val TAG = "MainFragment"
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_app_main,container,false)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tool_bar_mine.setOnClickListener(this)
        tool_bar_find.setOnClickListener(this)
        tool_bar_music_room.setOnClickListener(this)

        view_pager.offscreenPageLimit = 2
        view_pager.adapter = MainFragmentAdapter(activity?.supportFragmentManager!!)
        view_pager.currentItem = 1
        view_pager.addOnPageChangeListener(this)

        setTitleStyle(1)

    }


    override fun onClick(v: View) {

        var position = 0
        when(v.id) {

            R.id.tool_bar_mine -> position = 0
            R.id.tool_bar_music_room -> position = 1
            R.id.tool_bar_find -> position = 2
        }

        view_pager.currentItem = position
        setTitleStyle(position)
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {


    }

    override fun onPageSelected(position: Int) {
        setTitleStyle(position)
    }

    private fun setTitleStyle(position: Int) {
        when (position) {
            MINE -> {
                tool_bar_mine.textSize = 19f
                tool_bar_mine.setTextColor(Color.WHITE)

                tool_bar_music_room.textSize = 18f
                tool_bar_music_room.setTextColor(Color.LTGRAY)

                tool_bar_find.textSize = 18f
                tool_bar_find.setTextColor(Color.LTGRAY)
            }
            MUSIC1 -> {
                tool_bar_music_room.textSize = 19f
                tool_bar_music_room.setTextColor(Color.WHITE)

                tool_bar_mine.textSize = 18f
                tool_bar_mine.setTextColor(Color.LTGRAY)

                tool_bar_find.textSize = 18f
                tool_bar_find.setTextColor(Color.LTGRAY)
            }
            FIND -> {
                tool_bar_find.textSize = 19f
                tool_bar_find.setTextColor(Color.WHITE)

                tool_bar_mine.textSize = 18f
                tool_bar_mine.setTextColor(Color.LTGRAY)

                tool_bar_music_room.textSize = 18f
                tool_bar_music_room.setTextColor(Color.LTGRAY)
            }
        }
    }
}