package com.xxds.cjs.common.utils

import android.content.Context
import com.xxds.ApplicationMain

class CommonPreference {

    companion object {

        val prefs = ApplicationMain.instance.getSharedPreferences("com.xxds.cjs", Context.MODE_PRIVATE)

        fun <T> getSharePreferences(name: String,default: T) : T = with(prefs) {
            var res: Any = when(default) {

                is Long -> getLong(name, default)
                is String -> getString(name, default)
                is Int -> getInt(name, default)
                is Boolean -> getBoolean(name, default)
                is Float -> getFloat(name, default)
                else -> throw IllegalArgumentException("类型错误")
            }
            return  res as T
        }
        fun <T> putSharePreferences(name: String,value: T ) = with(prefs.edit()) {

            when(value) {
                is Long -> putLong(name,value)
                is String -> putString(name, value)
                is Int -> putInt(name, value)
                is Boolean -> putBoolean(name, value)
                is Float -> putFloat(name, value)
                else -> throw IllegalArgumentException("类型错误")
            }

            commit()
        }

    }



}