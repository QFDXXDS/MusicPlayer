package com.xxds.musicplayer.common.utils

import android.text.format.DateUtils
import java.util.*


fun formatTime(milli: Long): String {

    val m = (milli / DateUtils.MINUTE_IN_MILLIS).toInt()
    val s = (milli / DateUtils.SECOND_IN_MILLIS % 60).toInt()
//    获取Locale.getDefault()  本地格式
    val mm = String.format(Locale.getDefault(), "%02d", m)
    val ss = String.format(Locale.getDefault(), "%02d", s)
    return "$mm:$ss"
}