package com.xxds.cjs.common.aidl

import android.os.Parcel
import android.os.Parcelable
import com.xxds.cjs.common.net.socket.SocketInfo
import com.xxds.cjs.common.net.socket.SocketInterface
import com.xxds.cjs.common.utils.CommonPreference


class  CHITKeepAliveRequest: SocketInfo() {

//    override var name: String = super.name

}

class  CHITAdmissionRequest: SocketInfo() {

    val token: String
//    override val name: String

    init {
        token = CommonPreference.getSharePreferences("token", "")
    }
// override fun writeToParcel(p0: Parcel?, p1: Int) {
//        super.writeToParcel(p0, p1)
//        p0?.writeString(token)
//    }
//
//    companion object CREATOR : Parcelable.Creator<CHITAdmissionRequest> {
//        override fun createFromParcel(parcel: Parcel): CHITAdmissionRequest {
//            return CHITAdmissionRequest(parcel.readString())
//        }
//        override fun newArray(size: Int): Array<CHITAdmissionRequest?> {
//            return arrayOfNulls(size)
//        }
//    }


}


