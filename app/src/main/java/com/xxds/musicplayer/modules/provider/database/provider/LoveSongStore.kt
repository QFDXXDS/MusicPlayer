package com.xxds.musicplayer.modules.provider.database.provider

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.xxds.musicplayer.modules.provider.MusicDBHelper
import com.xxds.musicplayer.modules.provider.database.entity.MusicInfoCache

class LoveSongStore {

    companion object {

        const val LOVE_SONG_TABLE_CREATE = "create table " + MusicDBHelper.LOVE_SONG_TABLE +
                " (" + MusicInfoCache.SONG_ID + " integer(128), " +
                MusicInfoCache.NAME + " varchar(50) NOT NULL," +
                MusicInfoCache.ALBUM_ID + " integer(20)," +
                MusicInfoCache.ALBUM_NAME + " varchar(20)," +
                MusicInfoCache.ALBUM_PIC + " varchar(50)," +
                MusicInfoCache.ARTIST_ID + " integer(20)," +
                MusicInfoCache.ARTIST_NAME + " varchar(10) NOT NULL," +
                MusicInfoCache.PATH + " varchar(20)," +
                MusicInfoCache.IS_LOCAL + " int," +
                MusicInfoCache.DURATION + " integer," +
                "PRIMARY KEY (" + MusicInfoCache.SONG_ID + ")" +
                ");"

        val instance: LoveSongStore by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { LoveSongStore() }
    }

    fun onCreate(db: SQLiteDatabase) {
        db.execSQL(LOVE_SONG_TABLE_CREATE)
    }

    fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${MusicDBHelper.LOVE_SONG_TABLE}")
    }

    fun addSong(value: ContentValues) {

    }

    fun hasSong(key: Long): Boolean {

        val database = MusicDBHelper.instance.writableDatabase
        database.beginTransaction()
        var hasValue = false
        try {
            val sql = "SELECT ${MusicInfoCache.SONG_ID} FROM ${MusicDBHelper.LOVE_SONG_TABLE} WHERE ${MusicInfoCache.SONG_ID}='$key'"
            val cursor = database.rawQuery(sql, null)
            if (cursor.count > 0) {
                hasValue = true
            }
            cursor.close()
        } finally {
            database.setTransactionSuccessful()
            database.endTransaction()
        }
        return hasValue
    }
}