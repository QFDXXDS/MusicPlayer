package com.xxds

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.hardware.input.InputManager
import android.os.Process
import android.util.Log
import android.view.inputmethod.InputMethodManager
//import androidx.core.content.systemService
//import com.vidyo.VidyoClient.Connector.ConnectorPkg
import com.xxds.cjs.modules.info.managers.UserManager
//import org.jetbrains.anko.Android
//import org.jetbrains.anko.intentFor
import kotlin.properties.Delegates

class ApplicationMain : Application() {

    companion object {

        final var TAG = "viclee";
        var instance: ApplicationMain by Delegates.notNull()
    }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        instance = this
    }
    override fun onCreate() {
        super.onCreate()


        var pid = android.os.Process.myPid();

        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps = am.runningAppProcesses

        val iMM = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (iMM != null){



        }
//        iMM.hideSoftInputFromWindow()
        if (runningApps != null && !runningApps.isEmpty()) {

            for (info in runningApps) {

                if (info.pid == pid) {

//                    android.util.Log
                    println("进程名是" + info.processName)
//                     根据id杀死进程
//                    Process.killProcess(info.pid)

                }
            }
        }

        UserManager.instance
//        SocketManager(this)
//        ConnectorPkg.initialize()

        Log.d(TAG, "MyApplication onCreate");
        Log.d(TAG, "MyApplication pid is " + pid);

//        GNWSManager(this)
    }

    override fun onTerminate() {
        super.onTerminate()


    }


}