package com.xxds.musicplayer.modules.bases

import com.google.gson.JsonElement
import java.io.IOException

interface ConvertData<T> {

    @Throws(IOException::class)
    fun convertData(jsonElement: JsonElement): T
}