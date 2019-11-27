package com.xxds.musicplayer.common.net

import kotlin.reflect.full.memberProperties

interface HTTPInterface {

    fun parameters(): MutableMap<String,Any>
}

open class  HTTPReq: HTTPInterface {

    override fun parameters(): MutableMap<String, Any> {

        val kc = this::class
        val members = kc.memberProperties
        var map =  mutableMapOf<String, Any>()
        members.forEach {

            map[it.name] = it.call(this)!!

        }
       return map
    }

}