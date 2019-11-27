package com.xxds.cjs.common.net.socket

import android.content.Context
import android.content.Intent
import android.media.session.MediaSession
import android.system.Os.link
import android.util.Log
import com.xxds.cjs.common.aidl.CHITAdmissionRequest
import com.xxds.cjs.common.aidl.CHITKeepAliveRequest
import com.xxds.cjs.common.utils.CommonUtils
import io.reactivex.Observable
import org.java_websocket.WebSocket
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_17
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URI
import java.net.URLConnection
import java.util.*

private const val PORTAL_PREFIX = "ws://"

private const val PORTAL_SUFFIX = ":7010/jc-uccp/ws/uccp.do"

private const  val PORTAL_SUFFIX_SUFFIX = "/ue"

const val UPDATE_ACTION = "com.xxds.bases.SocketReceiver"

//#define PORTAL_SUFFIX @":7010/jc-uccp/ws/uccp.do"
//#define PORTAL_SUFFIX_SUFFIX @"/jc-uccp/ws/uccp.do"


class SocketObject(  val context: Context) {

    companion object {

        val HEART_INTERVAL: Long = 20_000
        val RECONNECT_INTERVAL: Long = 5_000
        val RSP_CHECK_INTERVAL = 1
    }

    var token: String by com.xxds.cjs.common.utils.Preference("")
    val intent = Intent(UPDATE_ACTION)
    lateinit var socketUrl: String
    lateinit var socket: WebSocketClient
    val timerControll: TimerControll by lazy {
        TimerControll()
    }
//    fun link(url: String): Observable<Boolean>{

        fun link(url: String){

//        if (socket != null && socket?.readyState != WebSocket.READYSTATE.CLOSED)
//        {
//            return@link
//        }

        socketUrl = url
        val URLString = PORTAL_PREFIX + socketUrl + PORTAL_SUFFIX


//        return Observable.create {

//            socket阻塞式进行连接
            socket  = object: WebSocketClient(URI(URLString), Draft_17()){

                override fun onOpen(handshakedata: ServerHandshake?) {

//                    intent.putExtra("status",1)
//                    it.onNext(true)
//                context.sendBroadcast(intent)
                    println("onOpen is ${handshakedata!!.httpStatus}" )

                    socketDidConnect()
                }

                override fun onMessage(message: String?) {

//                    intent.putExtra("msg",message)
////                    context.sendBroadcast(intent)


//                    (context as SocketService).onMessage(message!!)
                    Log.d("接收数据",message)
                }

                override fun onClose(code: Int, reason: String?, remote: Boolean) {
//                    URLConnection
//                    HttpURLConnection
//                    intent.putExtra("status",2)
//                    context.sendBroadcast(intent)
//                    it.onNext(false)
                    println("onClose is $code" + reason)

                    socketDidClose()
                }

                override fun onError(ex: Exception?) {

//                    intent.putExtra("status",3)
//                    context.sendBroadcast(intent)
//                    it.onNext(false)
                    println("onError is " + ex.toString())

                    socketDidClose()
                }
            }
            socket.connect()
//        }
    }
    fun sendMessage(message: String?){

        Log.d("111+发送数据",message)
        socket.send(message)
    }

    fun stop(){

        socket.close()
    }

      fun admission() {

//        var service = context as SocketService
//        service.messageSender.sendMessage(CHITAdmissionRequest(token))
          val req =  CommonUtils.toWSString(CHITAdmissionRequest())
          sendMessage(req)

      }

    fun socketDidConnect() {

        admission()

        timerControll.stopReconnectTimer()
    }
    fun socketDidClose() {


        timerControll.stopHeartTimer()
    }
    inner class TimerControll {

        var heartTimer: Timer? = null
        var reconnectTimer: Timer? = null

        fun stopHeartTimer() {

            if (timerControll.heartTimer != null) {
                timerControll.heartTimer?.cancel()
                timerControll.heartTimer = null
            }
            val reconnectTimerTask = object : TimerTask(){
                override fun run() {

                    link(socketUrl)
                }
            }
            reconnectTimer = Timer()
            reconnectTimer?.schedule(reconnectTimerTask, RECONNECT_INTERVAL,
                RECONNECT_INTERVAL)

        }
        fun stopReconnectTimer() {

            if (timerControll.reconnectTimer != null) {
                timerControll.reconnectTimer?.cancel()
                timerControll.reconnectTimer = null
            }
            val heartTimerTask = object : TimerTask(){
                override fun run() {

            val req =  CommonUtils.toWSString(CHITKeepAliveRequest())
            sendMessage(req)
        }
    }
            timerControll.heartTimer = Timer()
            timerControll.heartTimer?.schedule(heartTimerTask, HEART_INTERVAL,HEART_INTERVAL)

        }



    }


}