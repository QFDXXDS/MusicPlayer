package com.xxds.cjs.modules.info.managers

import android.os.UserManager
import com.xxds.cjs.modules.info.models.UserInfo
import kotlin.properties.Delegates

class UserManager private constructor(){

    companion object {

        @Volatile
        private var mUserManager: com.xxds.cjs.modules.info.managers.UserManager? = null
        val instance: com.xxds.cjs.modules.info.managers.UserManager
        get(){

            synchronized(UserManager::class.java) {
                if (mUserManager == null) {

                    mUserManager = UserManager()
                }
            }
            return mUserManager!!
        }
    }

    lateinit var userInfo: UserInfo




}