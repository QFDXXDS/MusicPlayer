package com.xxds.cjs.common.utils

import android.content.Context
import com.xxds.ApplicationMain
import kotlin.reflect.KProperty

class Preference<T>(private val default: T) {

    private  val prefs  by lazy { ApplicationMain.instance.getSharedPreferences("com.xxds.cjs",Context.MODE_PRIVATE) }

    operator fun setValue(thisRef:Any?,property: KProperty<*>, value: T ){

        putSharePreferences(property.name,value)
    }
    operator fun getValue(thisRef: Any?,property: KProperty<*>): T {

        return  getSharePreferences(property.name,default)
    }

   private fun getSharePreferences(name: String,default: T) : T = with(prefs) {
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
    private fun putSharePreferences(name: String,value: T ) = with(prefs.edit()) {

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