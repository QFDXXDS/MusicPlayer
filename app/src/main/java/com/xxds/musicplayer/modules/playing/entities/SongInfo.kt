package com.xxds.musicplayer.modules.playing.entities

class SongInfo {

    var data: Data? = null

    class Data {

        var songList: List<SongList>? = null
    }

    class SongList {

        var songId : Long = 0
        var songName : String? = null
        var artistName :  String? = null
        var songPicBig : String? = null
        var songLink : String? = null
        var lrcLink : String? = null
        var time : Long = 0
        var CurrentTime : Long = 0
    }


}