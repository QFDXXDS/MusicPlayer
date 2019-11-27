package com.xxds.cjs.common.net.socket


interface  SocketInterface {

//    var opt: String
    val name: String
}


open class SocketInfo: SocketInterface {

//    override  lateinit var opt: String
    override val name: String
    get() {
        return this::class.simpleName!!.substring(4)
    }



}
class  CHITKeepAliveRequest: SocketInfo() {


}