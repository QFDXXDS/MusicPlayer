package com.xxds.cjs.modules.bases

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xxds.musicplayer.R
import com.xxds.musicplayer.common.listioner.MusicStateListener
import com.xxds.musicplayer.common.utils.setTransparentForWindow
import com.xxds.musicplayer.modules.bases.ui.QuickControlsFragment
import com.xxds.musicplayer.modules.playing.manager.GlobalPlayTimeManager
import com.xxds.musicplayer.modules.service.*
import kotlinx.android.synthetic.main.content_main.view.*
import java.lang.UnsupportedOperationException
import java.lang.ref.WeakReference

@SuppressLint("Registered")

abstract class BaseActivity : AppCompatActivity() {

    private var fragment: QuickControlsFragment? = null // 底部播放控制栏
    private  var mMusicListener = mutableListOf<MusicStateListener>()
    private lateinit var mPlaybackStatus: PlaybackStatus
    private var mToken: ServiceToken? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        MusicPlayer.bindToService(this)
        mPlaybackStatus = PlaybackStatus(this)
        mToken = MusicPlayer.bindToService(this)


        val intentFilter = IntentFilter().apply {
            addAction(PLAY_STATE_CHANGED)
            addAction(META_CHANGED)
            addAction(MUSIC_CHANGED)
            addAction(QUEUE_CHANGED)
            addAction(TRACK_PREPARED)
            addAction(BUFFER_UP)
            addAction(MUSIC_CHANGED)
            addAction(LRC_UPDATED)
            addAction(MUSIC_LOADING)
            addAction(EMPTY_LIST)
            addAction(PLAYLIST_COUNT_CHANGED)
            addAction(MUSIC_COUNT_CHANGED)
        }
        registerReceiver(mPlaybackStatus, intentFilter)


        setTransparentForWindow(this)


    }

    open fun showQuickControl(show: Boolean) {

        val ft =  supportFragmentManager.beginTransaction()
        if (show) {

            if (fragment == null) {

                fragment = QuickControlsFragment.newInstance()
                ft.add(R.id.bottom_container,fragment!!).commit()

            } else {

                ft.show(fragment!!).commit()
            }
        } else {

            fragment?.let {
                ft.hide(fragment!!).commit()
            }
        }
    }
    /**
     * @param p 更新歌曲缓冲进度值，p取值从0~100
     */
    open fun updateBuffer(p: Int) {

        if (p >= 100) {

            baseUpdatePlayInfo()
        }

    }

    /**
     * @param l 歌曲是否加载中
     */
    open fun loading(l: Boolean) {

    }

    /**
     * 更新播放队列
     */
    open fun updateQueue() {}

    /**
     * 歌曲切换
     */
    open fun musicChanged() {

        for (listener in mMusicListener) {

            listener.musciChanged()
        }

    }
    /**
     * 更新歌词
     */
    open fun updateLrc() {
        for (listener in mMusicListener) {
            listener.updateLrc()
        }
    }
    /**
     * 更新歌曲状态信息
     */
    open fun baseUpdatePlayInfo() {

        for (listener in mMusicListener) {
            listener.reloadAdapter()
            listener.updatePlayInfo()
        }
    }

    /**
     * fragment界面刷新
     */
    fun refreshUI() {
        for (listener in mMusicListener) {
            listener.reloadAdapter()
        }
    }

    fun updateTime() {
        for (listener in mMusicListener) {
            listener.updateTime()
        }
    }


    fun setMusicStateListenerListener(status: MusicStateListener) {

        if (status == this) {
            throw UnsupportedOperationException("Override the method, don't add a listener")
        }
        mMusicListener.add(status)
    }
    fun removeMusicStateListenerListener(status: MusicStateListener?) {

        status?.let {
            mMusicListener.remove(status)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService()
        mMusicListener.clear()
        unregisterReceiver(mPlaybackStatus)
    }

    private fun unbindService() {
        if (mToken != null) {
            MusicPlayer.unbindFromService(mToken!!)
            mToken = null
        }
    }
    class PlaybackStatus(activity: BaseActivity) : BroadcastReceiver() {

        private val mReference: WeakReference<BaseActivity> = WeakReference(activity)

        override fun onReceive(context: Context, intent: Intent) {

            val action = intent.action
            val baseActivity = mReference.get()

            if (baseActivity != null) {
                when (action) {

                    META_CHANGED -> baseActivity.baseUpdatePlayInfo()
                    PLAY_STATE_CHANGED -> GlobalPlayTimeManager.trigger()
                    TRACK_PREPARED -> baseActivity.updateTime()
                    BUFFER_UP -> baseActivity.updateBuffer(intent.getIntExtra("progress", 0))
                    MUSIC_LOADING -> baseActivity.loading(intent.getBooleanExtra("isloading", false))
                    REFRESH -> {
                    }
                    MUSIC_COUNT_CHANGED -> baseActivity.refreshUI()
                    PLAYLIST_COUNT_CHANGED -> baseActivity.refreshUI()
                    QUEUE_CHANGED -> baseActivity.updateQueue()
                    TRACK_ERROR -> Toast.makeText(baseActivity, "错误了嘤嘤嘤", Toast.LENGTH_SHORT).show()
                    MUSIC_CHANGED -> baseActivity.musicChanged()
                    LRC_UPDATED -> baseActivity.updateLrc()
                }
            }
        }
    }


}
