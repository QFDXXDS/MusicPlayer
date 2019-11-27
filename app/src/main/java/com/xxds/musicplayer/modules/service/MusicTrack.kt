package com.xxds.musicplayer.modules.service

import android.os.Parcel
import android.os.Parcelable

class MusicTrack(val mId: Long, val mSourcePosition: Int): Parcelable {

    override fun describeContents(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        return 0
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        p0?.writeLong(mId)
        p0?.writeInt(mSourcePosition)
    }
    companion object CREATOR : Parcelable.Creator<MusicTrack> {
        override fun createFromParcel(parcel: Parcel): MusicTrack {
            return MusicTrack(parcel.readLong(),parcel.readInt())
        }

        override fun newArray(size: Int): Array<MusicTrack?> {
            return arrayOfNulls(size)
        }
    }
}