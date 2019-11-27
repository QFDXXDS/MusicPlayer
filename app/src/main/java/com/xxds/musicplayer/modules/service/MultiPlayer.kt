package com.xxds.musicplayer.modules.service

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.PowerManager
import com.xxds.ApplicationMain
import com.xxds.musicplayer.base.Null
import com.xxds.musicplayer.modules.service.MusicPlayer.Companion.seek
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.ref.WeakReference

class MultiPlayer(service: MediaService, private val mHandler: Handler) {

    private val mService: WeakReference<MediaService> = WeakReference(service)
    private val handler = Handler()
    private var isPlayerNet = false

    var isInitialized = false
    var isPlayerPrepared = false
    private var isNextPlayerPrepared = false
    private var mIllegalState = false
    private var mIsNextInitialized = false

    var secondaryPosition = 0
    private var isFirstLoad = true
    private var mNextMediaPath = Null
    private var mCurrentMediaPlayer = MediaPlayer()
    private var mNextMediaPlayer: MediaPlayer? = null

    private var preparedNextListener = MediaPlayer.OnPreparedListener {
        isNextPlayerPrepared = true

    }

    private var audioAttributes = AudioAttributes.Builder().apply {
        setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        setFlags(AudioAttributes.CONTENT_TYPE_MUSIC)
        setUsage(AudioAttributes.USAGE_MEDIA)
        setLegacyStreamType(AudioManager.STREAM_MUSIC)
    }.build()



    private val preparedListener = MediaPlayer.OnPreparedListener {

        if (isFirstLoad) {
            val seekPos = mService.get()?.mLastSeekPos ?: 0
            seek(seekPos)
            isFirstLoad = false
        }
        it.setOnCompletionListener(completionListener)
        isPlayerPrepared = true

        mService.get()?.prepared()

    }
    private val bufferingUpdateListener = MediaPlayer.OnBufferingUpdateListener { _, percent ->

        mService.get()?.sendUpdateBuffer(percent)

        secondaryPosition = percent
    }

    private val errorListener = MediaPlayer.OnErrorListener { _, what, extra ->

        when(what) {
            MediaPlayer.MEDIA_ERROR_SERVER_DIED -> {
                isInitialized = false
                isPlayerPrepared = false
                mCurrentMediaPlayer.release()
                mCurrentMediaPlayer = MediaPlayer()
                mCurrentMediaPlayer.setWakeMode(service,PowerManager.PARTIAL_WAKE_LOCK)
                val errorInfo = TrackErrorInfo(mService.get()?.getAudioId() ?: 0, mService.get()?.getTrackName())
                val msg = mHandler.obtainMessage(MediaService.SERVER_DIED,errorInfo)
                mHandler.sendMessageDelayed(msg, 2000)

                return@OnErrorListener true
            }
        }

        return@OnErrorListener false

    }
    private val completionListener = MediaPlayer.OnCompletionListener {

         isPlayerPrepared = false
        if (it === mCurrentMediaPlayer && mNextMediaPlayer != null) {

//          player释放
            mCurrentMediaPlayer.release()

            mCurrentMediaPlayer = mNextMediaPlayer!!

            isPlayerPrepared = true

            mNextMediaPlayer = null

            mNextMediaPath = Null

            mHandler.sendEmptyMessage(MediaService.TRACK_WENT_TO_NEXT)
        } else {

            mHandler.sendEmptyMessage(MediaService.TRACK_ENDED)
            mHandler.sendEmptyMessage(MediaService.RELEASE_WAKELOCK)
        }

    }

    private val startMediaPlayerIfPrepared = object : Runnable {

        override fun run() {

            if (isPlayerPrepared) {

                mCurrentMediaPlayer.start()
                val duration = duration()
                if (mService.get()?.mRepeatMode != MediaService.REPEAT_CURRENT && duration > 2000 && position() >= duration - 2000) {
                    mService.get()?.nextPlay()
                }
            } else {

                handler.postDelayed(this, 700)
            }
        }
    }


    init {
        mCurrentMediaPlayer.setWakeMode(mService.get(),PowerManager.PARTIAL_WAKE_LOCK)
    }

    fun setDataSource(path: String) {
        isInitialized = setDataSourceImpl(mCurrentMediaPlayer,path)
    }
    private fun setDataSourceImpl(player: MediaPlayer, path: String): Boolean {

        try {
            stop()
            player.setAudioAttributes(audioAttributes)

            if (path.startsWith("content://")) {

                player.setOnPreparedListener(null)
                player.setDataSource(ApplicationMain.instance, Uri.parse(path))
                player.prepare()
                player.setOnCompletionListener(completionListener)
                isPlayerPrepared = true
            } else {
                isPlayerNet = true
                player.setDataSource(path)
                player.setOnPreparedListener(preparedListener)
                player.prepareAsync()
            }

        } catch (ignored: IOException) {
            return false
        }catch (ignored: IllegalArgumentException) {
            return true
        }catch (ignored: IllegalStateException) {

            ignored.printStackTrace()
            if (!mIllegalState) {
                mCurrentMediaPlayer = MediaPlayer()
                mCurrentMediaPlayer.setWakeMode(mService.get(), PowerManager.PARTIAL_WAKE_LOCK)
                setDataSourceImpl(mCurrentMediaPlayer,path)
                mIllegalState = true
            } else {
                mIllegalState = false
                return false
            }
        }


        player.setOnErrorListener(errorListener)
        player.setOnBufferingUpdateListener(bufferingUpdateListener)

        start()
        return true
    }
        fun duration(): Long {

        return if (isPlayerPrepared) {
            mCurrentMediaPlayer.duration.toLong()
        } else -1
    }


    fun setNextDataSource(path: String?) {


        mIsNextInitialized = false
        try {
            mCurrentMediaPlayer.setNextMediaPlayer(null)

        }catch (ignored: IllegalArgumentException) {

        }catch (ignored: IllegalStateException) {
            return
        }

        if (mNextMediaPlayer != null) {
            mNextMediaPlayer?.release()
            mNextMediaPlayer = null
        }
        if (path == null || path.isEmpty()) {

            return
        }

        mNextMediaPlayer = MediaPlayer()
        mNextMediaPlayer!!.setWakeMode(mService.get(),PowerManager.PARTIAL_WAKE_LOCK)


        if (setNextDataSourceImpl(mNextMediaPlayer!!, path)) {
            mNextMediaPath = path
            mCurrentMediaPlayer.setNextMediaPlayer(mNextMediaPlayer)
        } else {
            mNextMediaPlayer!!.release()
        }



    }
    private fun setNextDataSourceImpl(player: MediaPlayer, path: String): Boolean {


        isNextPlayerPrepared = false
        try {
            player.reset()
            player.setAudioAttributes(audioAttributes)

            if (path.startsWith("content://")) {
                player.setOnPreparedListener(preparedNextListener)
                player.setDataSource(ApplicationMain.instance, Uri.parse(path))
                player.prepare()
            } else {
                player.setDataSource(path)
                player.setOnPreparedListener(preparedNextListener)
                player.prepare()
                isNextPlayerPrepared = false
            }
        } catch (ignored: IOException) {
            return false
        } catch (ignored: IllegalArgumentException) {
            return false
        }
        player.setOnCompletionListener(completionListener)
        player.setOnErrorListener(errorListener)
        return true
    }

    fun start() {

        if (isPlayerNet) {
            secondaryPosition = 0
            mService.get()?.loading(true)
            handler.postDelayed(startMediaPlayerIfPrepared,50)
        } else {
            mService.get()?.sendUpdateBuffer(100)
            secondaryPosition = 100
            mCurrentMediaPlayer.start()
        }

    }
    fun stop() {

        handler.removeCallbacks(startMediaPlayerIfPrepared)
//      player进行重设
        mCurrentMediaPlayer.reset()
        isInitialized = false
        isPlayerPrepared = false
        isPlayerNet = false

    }
    fun release() {
        mCurrentMediaPlayer.release()
    }

    fun pause() {
        handler.removeCallbacks(startMediaPlayerIfPrepared)
        mCurrentMediaPlayer.pause()
    }

    fun position(): Long {

        if (isPlayerPrepared) {
            try {
                return mCurrentMediaPlayer.currentPosition.toLong()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return -1
    }

    fun seek(whereto: Long): Long {
        mCurrentMediaPlayer.seekTo(whereto.toInt())
        return whereto
    }


}