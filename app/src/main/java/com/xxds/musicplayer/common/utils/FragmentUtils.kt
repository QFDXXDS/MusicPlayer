package com.xxds.musicplayer.common.utils

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xxds.cjs.modules.bases.BaseActivity
import com.xxds.musicplayer.R


/**
 * Fragment跳转工具
 */


fun startFragment(current: Fragment, target: Fragment, tag: String) {

    current.fragmentManager!!.beginTransaction().run {

        setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
        (current.context as BaseActivity).supportFragmentManager.findFragmentByTag(tag)?.let { hide(it) }
        addToBackStack(null)
        add(R.id.se_main_content, target)
        commit()
    }
}

fun startFragment(current: ViewGroup, target: Fragment, tag: String) {
    if (current is Fragment) {
        startFragment(current as Fragment, target, tag)
    }
}