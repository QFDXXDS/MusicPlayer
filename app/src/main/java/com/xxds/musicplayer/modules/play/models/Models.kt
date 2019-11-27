package com.xxds.musicplayer.modules.play.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlayModel(val map: Map<String,String>?): Parcelable {

    val songId : Any by map
    val songName : Any by map
    val artistName :  Any by map
    val songPicBig : Any by map
    val songLink : Any by map
    val lrcLink : Any by map
    val time : Any by map
    var CurrentTime : Int = 0

}