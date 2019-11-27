package com.xxds.musicplayer.modules.playing.entities

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.xxds.musicplayer.base.Null
import com.xxds.musicplayer.modules.bases.ConvertData

class OtherVersionInfo: ConvertData<OtherVersionInfo?> {

    var track: List<OtherVersionTrackBean>? = null

    class OtherVersionTrackBean {
        var name: String = Null
        var artist: String = Null
        var url: String = Null
        var streamable: String? = null
        var listeners: String? = null
        var mbid: String? = null
        var image: List<ImageBean>? = null
    }

    override fun convertData(jsonElement: JsonElement): OtherVersionInfo? {

        val root = jsonElement.asJsonObject
        if (!root.has("results")) {

            return null
        }
        val trackMatch = root["results"].asJsonObject
        if (!trackMatch.has("trackmatches")) {

            return null
        }

        return Gson().fromJson(trackMatch["trackmatches"],OtherVersionInfo::class.java)



    }

}