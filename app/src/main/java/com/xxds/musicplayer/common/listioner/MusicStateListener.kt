package com.xxds.musicplayer.common.listioner

interface MusicStateListener {

    /**
     * 更新歌曲状态信息
     */
    fun updatePlayInfo()

    /**
     * 更新播放时间
     */
    fun updateTime()

    fun reloadAdapter()

    fun musciChanged()

    fun updateLrc()
}