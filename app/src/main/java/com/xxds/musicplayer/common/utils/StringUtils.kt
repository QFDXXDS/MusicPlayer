package com.xxds.musicplayer.common.utils

import com.xxds.chitjishi.Common.Config.AppConfig.LAST_FM_IMAGE_LARGE
import com.xxds.chitjishi.Common.Config.AppConfig.LAST_FM_IMAGE_MEDIUM
import com.xxds.chitjishi.Common.Config.AppConfig.LAST_FM_IMAGE_MEGA
import com.xxds.musicplayer.base.Null


fun String.getMediumImageUrl(): String {
    return String.format(LAST_FM_IMAGE_MEDIUM, this)
}

fun String.getLargeImageUrl(): String {
    return String.format(LAST_FM_IMAGE_LARGE, this)
}

fun String.getMegaImageUrl(): String {
    return String.format(LAST_FM_IMAGE_MEGA, this)
}

fun String.getImageId(): String {
    val regex = Regex("[a-z0-9]*(?=.png)")
    val result = regex.find(this)
    return result?.value ?: "null"
}

fun String.isContentEmpty(): Boolean {
    return this == Null
}

/**
 * 将 234736 转化为分钟和秒应为 03:55
 */
fun Long.ms2Minute(): String {
    var time = ""
    val minute = this / 60000
    val seconds = this % 60000
    val second = Math.round(seconds.toFloat() / 1000).toLong()
    if (minute < 10) {
        time += "0"
    }
    time += "$minute:"
    if (second < 10) {
        time += "0"
    }
    time += second
    return time
}
