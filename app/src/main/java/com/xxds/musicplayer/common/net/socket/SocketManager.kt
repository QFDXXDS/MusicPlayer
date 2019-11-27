//package com.xxds.cjs.common.net.socket
//
//import android.content.ComponentName
//import android.content.Context
//import android.content.Intent
//import android.content.ServiceConnection
//import android.os.IBinder
//import android.os.Message
//import android.os.Messenger
//import com.google.gson.Gson
//import com.xxds.cjs.common.MessageReceiver
//import com.xxds.cjs.common.MessageSender
//import com.xxds.cjs.common.aidl.MessageModel1
//import com.xxds.cjs.common.utils.CommonPreference
//import com.xxds.cjs.common.utils.CommonUtils
//import com.xxds.cjs.services.SocketService
//import org.greenrobot.eventbus.EventBus
//import java.lang.Exception
//import java.net.Socket
//import kotlin.properties.Delegates
//import kotlin.reflect.KClass
//
//class SocketManager(val context: Context) {
//
//    companion object {
//
//        var instance: SocketManager by Delegates.notNull()
//
//        fun sendMsg(any: SocketInterface) {
//
////            var json = CommonUtils.toMap(any)
////            val msg = Message()
////            msg.data.putString("msg",json.toString())
//
//            val msg = CommonUtils.toWSString(any)
//            instance.messageSender?.sendMessage(msg)
//        }
//    }
//
//    lateinit var token: String
//    val deathRecipient = object: IBinder.DeathRecipient {
//
//        override fun binderDied() {
////            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//
//            if (messageSender != null) {
//                messageSender?.asBinder()?.unlinkToDeath(this,0)
//                messageSender = null
//            }
//            bindService()
//        }
//    }
//    var serviceConnect = object : ServiceConnection {
//
//        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//
//            messageSender = MessageSender.Stub.asInterface(service)
//            try {
//                messageSender?.asBinder()?.linkToDeath(deathRecipient,0)
//                messageSender?.registerReceiveListener(messageReceiver)
//            }catch (o: Exception){
//
//
//            }
//        }
//        override fun onServiceDisconnected(name: ComponentName?) {
//
//            messageSender = null
//        }
//    }
//    val  messageReceiver = object : MessageReceiver.Stub() {
//
//        override fun onMessageReceived(msg: String) {
//
//            val response = CommonUtils.toWSResponse(msg)
//
//            println("客户端接收" + msg)
//        }
//    }
//
//    var messageSender: MessageSender? = null
//
//    init {
//        instance = this
//        bindService()
//    }
//
//    fun bindService() {
//
//        val intent = Intent(context,SocketService::class.java)
////       跨应用需指定
////        intent.`package`
//        context.bindService(intent,serviceConnect, Context.BIND_AUTO_CREATE)
//    }
//
//
//    fun destroy() {
//
//        if (messageSender != null && messageSender?.asBinder()!!.isBinderAlive) {
//            try {
//                messageSender?.unregisterReceiveListener(messageReceiver)
//
//            } catch (o: Exception) {
//
//
//            }
//
//        }
//        context.unbindService(serviceConnect)
//
//    }
//
//
//    fun sendUserInfo(token: String) {
//        this.token = token ;
//        CommonPreference.putSharePreferences("token",token)
//        messageSender?.sendUserInfo(this.token)
//
////        val msg = MessageModel1("client user id","receiver user id","This is message content",this.token)
////        messageSender?.sendMessage(msg)
//
////        EventBus.getDefault().post()
//  }
//
//
//
//
//}