package com.xxds.musicplayer.modules.playing.ui

import android.content.ContentValues
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSeekBar
import com.xxds.musicplayer.R
import com.xxds.musicplayer.common.utils.LOVE_SONG_CONTENT_URI
import com.xxds.musicplayer.common.utils.PlayTimeListener
import com.xxds.musicplayer.common.utils.ViewBlockAction
import com.xxds.musicplayer.common.utils.ms2Minute
import com.xxds.musicplayer.modules.playing.manager.GlobalPlayTimeManager
import com.xxds.musicplayer.modules.provider.database.entity.MusicInfoCache
import com.xxds.musicplayer.modules.provider.database.provider.LoveSongStore
import com.xxds.musicplayer.modules.service.MusicPlayer

class PlayingBottomView: LinearLayout, View.OnClickListener,SeekBar.OnSeekBarChangeListener, ViewBlockAction,PlayTimeListener {

    private lateinit var repeatMode: ImageView
    private lateinit var preSong: ImageView
    private lateinit var centerControl: ImageView
    private lateinit var nextSong: ImageView
    private lateinit var songList: ImageView
    private lateinit var favorite: ImageView
    private lateinit var download: ImageView
    private lateinit var share: ImageView
    private lateinit var comment: ImageView

    private lateinit var seekTimePlayed: TextView
    private lateinit var seekTotalTime: TextView
    private lateinit var seekBar: AppCompatSeekBar

    private var duration: Long = 0
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun init(context: Context) {

        orientation = VERTICAL
        View.inflate(context, R.layout.block_playing_bottom,this)

        repeatMode = findViewById(R.id.repeat_mode)
        preSong = findViewById(R.id.pre_song)
        centerControl = findViewById(R.id.center_control)
        nextSong = findViewById(R.id.next_song)
        songList = findViewById(R.id.song_list)

        favorite = findViewById(R.id.playing_favorite)
        download = findViewById(R.id.playing_download)
        share = findViewById(R.id.playing_share)
        comment = findViewById(R.id.playing_comment)

        seekTimePlayed = findViewById(R.id.seek_current_time)
        seekTotalTime = findViewById(R.id.seek_all_time)
        seekBar = findViewById(R.id.player_seek_bar)


        seekBar.isIndeterminate = false
        seekBar.max = 1000
        seekBar.progress = 1

        seekBar.setOnSeekBarChangeListener(this)
        repeatMode.setOnClickListener(this)
        preSong.setOnClickListener(this)
        centerControl.setOnClickListener(this)
        nextSong.setOnClickListener(this)
        songList.setOnClickListener(this)
        favorite.setOnClickListener(this)
        download.setOnClickListener(this)
        share.setOnClickListener(this)
        comment.setOnClickListener(this)


        GlobalPlayTimeManager.registerListener(this)

    }

    override fun musicChanged() {

        setSeekBar()
        updatePlayingStatus()
    }

    override fun playedTime(position: Long, duration: Long) {

        var progress = (position * 1000 / duration).toInt()
        seekBar.progress = progress

    }

    override fun updateBlock() {

        updateRepeatStatus()
        updatePlayingStatus()
        setSeekBar()
    }
    fun setRepeatStatus(){


    }
    fun updateRepeatStatus() {


    }

    fun  updatePlayingStatus() {

        if (MusicPlayer.isPlaying()) {

            centerControl.setImageResource(R.drawable.default_player_btn_pause_selector)
        } else {

            centerControl.setImageResource(R.drawable.default_player_btn_play_selector)

        }

    }
    override fun onClick(p0: View?) {

        when (p0!!.id) {
            R.id.repeat_mode -> {
                setRepeatStatus()
                updateRepeatStatus()
            }
            R.id.pre_song -> {
                MusicPlayer.previous()
            }
            R.id.center_control -> {
                MusicPlayer.playOrPause()
                updatePlayingStatus()
            }
            R.id.next_song -> MusicPlayer.nextPlay()

            R.id.playing_favorite -> {

                var bb = LoveSongStore.instance.hasSong(MusicPlayer.getAudioId())

                if (!bb) {

                    val values = ContentValues()
                    values.run {

                        put(MusicInfoCache.SONG_ID, MusicPlayer.getAudioId())
                        put(MusicInfoCache.NAME, MusicPlayer.getTrackName())
                        put(MusicInfoCache.ALBUM_ID, MusicPlayer.getAlbumId())
                        put(MusicInfoCache.ALBUM_NAME, MusicPlayer.getAlbumName())
                        put(MusicInfoCache.ALBUM_PIC, MusicPlayer.getAlbumPic())
                        put(MusicInfoCache.ARTIST_NAME, MusicPlayer.getArtistName())
                        put(MusicInfoCache.DURATION, MusicPlayer.duration())

                        if (MusicPlayer.isTrackLocal()) {
                            put(MusicInfoCache.IS_LOCAL, 0)
                        } else {
                            put(MusicInfoCache.IS_LOCAL, 1)
                        }

                    }
                    context.contentResolver.insert(LOVE_SONG_CONTENT_URI, values)
                }

            }
        }
    }

    private fun setSeekBar() {

        duration = MusicPlayer.duration()
        seekTotalTime.text = duration.ms2Minute()
        seekTimePlayed.text = MusicPlayer.position().ms2Minute()

        if (duration != 0.toLong()) {
            seekBar.progress = (1000 * MusicPlayer.position() / duration).toInt()
        }

    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

    }

    override fun onStartTrackingTouch(p0: SeekBar?) {

    }

    override fun onStopTrackingTouch(p0: SeekBar?) {

    }


}