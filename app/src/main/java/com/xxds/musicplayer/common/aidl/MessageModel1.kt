package com.xxds.cjs.common.aidl

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

//@SuppressLint("ParcelCreator")// 用于处理 Lint 的错误提示
//@Parcelize




 open class MessageModel1: Parcelable {

   open  val name:String
//       get() {
//           return this::class.simpleName!!.substring(4)
//       }
//     set(value) {}
//    val opt:String by lazy {
//
//    }

    init {

        name = this::class.simpleName!!.substring(4)
    }

    override fun describeContents(): Int {

        return  0
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {

        p0?.writeString(name)
//        p0?.writeString(to)
//        p0?.writeString(content)
//        p0?.writeString(token)
    }

    companion object CREATOR : Parcelable.Creator<MessageModel1> {
        override fun createFromParcel(parcel: Parcel): MessageModel1 {
            return MessageModel1()
            }
        override fun newArray(size: Int): Array<MessageModel1?> {
            return arrayOfNulls(size)
            }
        }


}

//class MessageModel2: MessageModel1 {
//
//
//}