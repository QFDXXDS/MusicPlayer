package com.xxds.cjs.common.utils

import android.util.Log
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.xxds.cjs.common.net.socket.SocketInterface
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.full.memberProperties

object  CommonUtils {


    private const val MILLIS_LIMIT = 1000.0

    private const val SECONDS_LIMIT = 60 * MILLIS_LIMIT

    private const val MINUTES_LIMIT = 60 * SECONDS_LIMIT

    private const val HOURS_LIMIT = 24 * MINUTES_LIMIT

    private const val DAYS_LIMIT = 30 * HOURS_LIMIT


    private var mTransId = 0
    private val transId: Int
        get() {
            mTransId += 1
            return mTransId
        }

    fun toMap(any: SocketInterface): MutableMap<String,Any> {

        val props = any::class.memberProperties
        var json = mutableMapOf<String,Any>()
        var value = mutableMapOf<String,Any>()

        props.forEach {

            val temp = it.call(this)
            value[it.name] = temp!!
        }
        value["transId"] = transId
        json["name"] = any.name
        json["value"] = value

        return json
    }

    /**
     * 获取时间格式化
     */
    fun getNewsTimeStr(date: Date?): String {
        if (date == null) {
            return ""
        }
        val interval = Date().time - date.time
       return  when {
           interval < MILLIS_LIMIT -> "刚刚"
           interval < SECONDS_LIMIT -> Math.round(interval / MILLIS_LIMIT).toString() + " 秒前"
           interval < MINUTES_LIMIT -> Math.round(interval / SECONDS_LIMIT).toString() + " 分前"
           interval < HOURS_LIMIT -> Math.round(interval / MINUTES_LIMIT).toString() + " 小时前"
           interval < DAYS_LIMIT -> Math.round(interval / HOURS_LIMIT).toString() + " 天前"
           else -> getDateStr(date)
       }
    }
    fun getDateStr(date: Date?): String {
        if (date?.toString() == null) {
            return ""
        } else if (date.toString().length < 10) {
            return date.toString()
        }
//        SimpleDateFormat 设置日期格式
//        java.util.Locale
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date).substring(0, 10)
    }

    fun toWSString(obj: SocketInterface): String  {

        val properties =  obj::class.memberProperties

        val json = mutableMapOf<String,Any>()

        val value = mutableMapOf<String,Any>()

        properties.forEach {

            var v = it.call(obj)

            if (it.name == "name") {
                json[it.name] = v!!
            } else {
                value[it.name] = v!!
            }
        }

        value["transId"] = transId
        json["value"] = value
        return Gson().toJson(json)
    }

     fun toWSResponse(json: String): Map<String,Any> {

         val map = mapOf<String,Any>()
//         return Gson().fromJson<Map<String,Any>>(json,map)

         return Gson().fromJson(json,object :TypeToken<Map<String,Any>>(){}.type)
     }


}