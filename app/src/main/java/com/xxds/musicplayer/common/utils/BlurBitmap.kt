package com.xxds.musicplayer.common.utils

import android.graphics.Bitmap

fun blurBitmap(bitmap: Bitmap, process: Int): Bitmap? {

    val blurBitmap: Bitmap?
    val manager = BlurManager(bitmap)
    blurBitmap = manager.process(process)

    return blurBitmap

}