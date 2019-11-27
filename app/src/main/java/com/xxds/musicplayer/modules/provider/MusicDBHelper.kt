package com.xxds.musicplayer.modules.provider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.xxds.ApplicationMain
import com.xxds.chitjishi.Common.Config.AppConfig.DATABASE_NAME
import com.xxds.chitjishi.Common.Config.AppConfig.DATABASE_VERSION
import com.xxds.musicplayer.common.utils.*
import com.xxds.musicplayer.modules.provider.database.entity.MusicInfoCache
import com.xxds.musicplayer.modules.provider.database.provider.ImageStore
import com.xxds.musicplayer.modules.provider.database.provider.LoveSongStore
import com.xxds.musicplayer.modules.provider.database.provider.RecentStore

class MusicDBHelper(context: Context,name: String?,factory: SQLiteDatabase.CursorFactory?,version:Int):SQLiteOpenHelper(context,name,factory,version) {

    constructor(): this(ApplicationMain.instance,DATABASE_NAME,null, DATABASE_VERSION)

    companion object {
        const val SONG_LIST_TABLE = "SongListTable"
        const val MUSIC_INFO_TABLE = "MusicInfoTable"
        const val LOVE_SONG_TABLE = "LoveSongTable"
        const val IMAGE_TABLE = "ImageTable"
        val instance: MusicDBHelper by lazy { MusicDBHelper() }

        const val SONG_LIST_TABLE_CREATE = "create table " + SONG_LIST_TABLE +
                " (" + SL_ID + " varchar(128), " +
                SL_NAME + " varchar(50) NOT NULL," +
                SL_COUNT + " int," +
                SL_CREATOR + " varchar(20)," +
                SL_CREATE_TIME + " varchar(30) NOT NULL," +
                SL_PIC + " varchar(50)," +
                SL_INFO + " varchar(50)," +
                "PRIMARY KEY (" + SL_ID + ")" +
                ");"
        const val MUSIC_INFO_TABLE_CREATE = "create table " + MUSIC_INFO_TABLE +
                " (" + MusicInfoCache.SONG_ID + " varchar(128), " +
                MusicInfoCache.SONG_LIST_ID + " varchar(50) NOT NULL," +
                MusicInfoCache.NAME + " varchar(50) NOT NULL," +
                MusicInfoCache.ALBUM_ID + " varchar(20)," +
                MusicInfoCache.ALBUM_NAME + " varchar(20)," +
                MusicInfoCache.ALBUM_PIC + " varchar(50)," +
                MusicInfoCache.ARTIST_ID + " varchar(20) NOT NULL," +
                MusicInfoCache.ARTIST_NAME + " varchar(10) NOT NULL," +
                MusicInfoCache.PATH + " varchar(20)," +
                MusicInfoCache.IS_LOCAL + " int," +
                MusicInfoCache.ID + " varchar(128)," +
                MusicInfoCache.IS_LOVE + " int," +
                "PRIMARY KEY (" + MusicInfoCache.ID + ")" +
                ");"
    }
    override fun onCreate(p0: SQLiteDatabase?) {

        p0!!.execSQL(SONG_LIST_TABLE_CREATE)
        p0!!.execSQL(MUSIC_INFO_TABLE_CREATE)

        RecentStore.instance.onCreate(p0)
        ImageStore.instance.onCreate(p0)
        LoveSongStore.instance.onCreate(p0)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

        p0!!.execSQL("DROP TABLE IF EXISTS $SONG_LIST_TABLE")
        p0!!.execSQL("DROP TABLE IF EXISTS $MUSIC_INFO_TABLE")

        RecentStore.instance.onUpgrade(p0,p1,p2)
        ImageStore.instance.onUpgrade(p0,p1,p2)
        LoveSongStore.instance.onUpgrade(p0,p1,p2)

        onCreate(p0)
    }

}