package com.xxds.musicplayer.modules.playing.entities

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.xxds.musicplayer.base.Null
import com.xxds.musicplayer.modules.bases.ConvertData

class SimilarSongInfo: ConvertData<SimilarSongInfo?> {

    override fun convertData(jsonElement: JsonElement): SimilarSongInfo? {

        val root = jsonElement.asJsonObject
        if (!root.has("similartracks")) {

            return null
        }
        return Gson().fromJson(root.get("similartracks"),SimilarSongInfo::class.java)
    }

    var track: List<SimilarTrackBean>? = null

    class SimilarTrackBean {
        var name: String? = null
        var playcount: Int = 0
        var mbid: String? = null
        var match: Int = 0
        var url: String? = null
        var duration: Int = 0
        var artist: ArtistBean? = null
        var image: List<ImageBean>? = null

        class ArtistBean {
            var name: String = Null
            var mbid: String? = null
            var url: String? = null
        }
    }
}