package com.xxds.cjs.modules.bases

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.os.Messenger


const val UPDATE_ACTION = "com.xxds.bases.SocketReceiver"

class SocketBaseActivity : BaseActivity() {

     var localMessenger: Messenger? = null
    var serviceConnect = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            localMessenger = Messenger(service)
        }
        override fun onServiceDisconnected(name: ComponentName?) {

            localMessenger = null
        }
    }

    lateinit var  mReciver: SocketReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initReciver()
    }

    fun initReciver() {

        val filter = IntentFilter()
        filter.addAction(UPDATE_ACTION)
        mReciver = SocketReceiver()
        registerReceiver(mReciver,filter)
    }

//    <action android:name="com.xxds.bases.SocketReceiver"></action>

   inner class SocketReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
//            TODO("SocketReceiver.onReceive() is not implemented")


            println("onreceive+ " + intent.extras.get("MSG"))
        }
    }

}
