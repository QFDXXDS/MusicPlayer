package com.xxds.musicplayer.modules.entity

import android.os.Parcelable
import com.xxds.musicplayer.base.Null
import kotlinx.android.parcel.Parcelize



/**
 * 专辑bean
 */
data class AlbumEntity(
    var albumId: Long = -1,
    var albumName: String,
    var numberOfSongs: Int,
    var albumArtist: String,
    var albumKey: String
) {
    var imageUrl: String = Null
}

/**
 * 歌手bean
 */
data class ArtistEntity(
    var artistName: String,
    var numberOfTracks: Int,
    var artistId: Int,
    var artistKey: String
) {
    var imageId: String = Null
}

/**
 * 文件夹bean
 */
data class FolderEntity(
    var folderName: String,
    var folderPath: String,
    var folderSort: String
) {
    var folderCount: Int = 0
}

@Parcelize
data class MusicEntity(

    var audioId: Long = -1

) : Parcelable {

    var musicName: String? = null
    var artist: String? = null
    var albumData: String? = null
    var albumKey: String? = null
    var duration: Long = 0
    var albumName: String? = null
    var artistId: Long = 0
    var data: String? = null
    var size: Long = 0
    var folder: String? = null
    var lrc: String? = null
    var sort: String? = null
    // 0表示没有收藏 1表示收藏,
    var favorite: Int = 0
    var islocal: Boolean = false
}

data class SongListEntity(
    var id: String,
    var name: String,
    var createTime: String
) {
    var count: Int = 0
    var creator: String? = null
    var listPic: String? = null
    var info: String? = null
}

data class OverFlowItem(
    // 信息标题
    var title: String,
    // 图片ID
    var avatar: Int = 0
)