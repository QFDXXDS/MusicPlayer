package com.xxds.musicplayer.modules.main.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xxds.musicplayer.R
import com.xxds.musicplayer.common.utils.inflate
import com.xxds.musicplayer.modules.Singletons.HandlerSingleton
import com.xxds.musicplayer.modules.entity.MusicEntity
import com.xxds.musicplayer.modules.service.MusicPlayer

class MusicListAdapter(var context: Context, var mList: MutableList<MusicEntity>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mHeadLayout = 0X01
    private val mContentLayout = 0X02
    private var playMusic: PlayMusic? = null
    private val handle = HandlerSingleton.instance

    override fun getItemCount(): Int {

        return mList.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        if (holder is ListItemViewHolder) {

            holder.onBindData(mList[position - 1 ])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when(viewType) {

        mHeadLayout -> CommonItemViewHolder(parent.inflate(R.layout.common_item))
        else ->  ListItemViewHolder(parent.inflate(R.layout.fragment_music_song_item))
    }

    override fun getItemViewType(position: Int): Int = when (position) {

        0 -> mHeadLayout
        else -> mContentLayout
    }

    internal inner class PlayMusic(var position: Int) : Runnable {
        /**
         * 运行在主线程
         */
        override fun run() {

            val list = LongArray(mList.size)
            val infoMap = hashMapOf<Long, MusicEntity>()
            for (i in mList.indices) {

                val info = mList[i]
                list[i] = info.audioId
                info.islocal = false
                infoMap[list[i]] = mList[i]
            }
            if (position > -1)
                MusicPlayer.playAll(infoMap, list, position)
        }
    }

    inner class CommonItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var select: ImageView? = null

        override fun onClick(p0: View?) {

            if (playMusic != null) {

                handle.removeCallbacks(playMusic)
            }

            if(adapterPosition > -1) {

                playMusic = PlayMusic(0)
                handle.postDelayed(playMusic, 70)

            }

        }
        init {
            select = view.findViewById(R.id.select)
            view.setOnClickListener(this)
        }

    }
    inner class ListItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private var mMusicName: TextView = view.findViewById(R.id.music_name)
        private var mMusicInfo: TextView = view.findViewById(R.id.music_info)
        private var mAlbumInfo: TextView = view.findViewById(R.id.album_info)
        private var mListButton: ImageView = view.findViewById(R.id.viewpager_list_button)
        override fun onClick(p0: View?) {

        }
        init {
            mListButton.setOnClickListener(this)
            view.setOnClickListener(this)
        }

        fun onBindData(musicEntity: MusicEntity) {
            mMusicName.text = musicEntity.musicName
            mMusicInfo.text = musicEntity.artist
            mAlbumInfo.text = musicEntity.albumName
        }

    }

}