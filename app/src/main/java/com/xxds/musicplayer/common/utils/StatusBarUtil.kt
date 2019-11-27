package com.xxds.musicplayer.common.utils

import android.app.Activity
import android.graphics.Color
import android.view.View

/**
 * 设置状态栏透明
 * 5.0以上系统
 */
fun setTransparentForWindow(activity: Activity) {
    activity.window.statusBarColor = Color.TRANSPARENT
    activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
}