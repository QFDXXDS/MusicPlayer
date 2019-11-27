package com.xxds.musicplayer.modules.main.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.xxds.musicplayer.R
import com.xxds.musicplayer.common.utils.GridItemDecoration
import com.xxds.musicplayer.modules.entity.MusicEntity
import com.xxds.musicplayer.modules.main.models.RecommendListModel
import com.xxds.musicplayer.modules.service.MusicPlayer
import kotlinx.android.synthetic.main.online_day_recommend_song_block.*
import kotlinx.android.synthetic.main.online_day_recommend_song_block.view.*


class RecommendSongListBlock: LinearLayout {

    private lateinit var recommendRecycleView: RecyclerView
    private lateinit var iconEnter: View


    constructor(context: Context): this(context,null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }
    fun init() {

        orientation = LinearLayout.VERTICAL
        setBackgroundColor(ContextCompat.getColor(context,R.color.abc_background_cache_hint_selector_material_dark))

        View.inflate(context,R.layout.online_day_recommend_song_block,this)

        initView()
    }
    private fun initView() {

        recommendRecycleView = recommend_recycle_view
        recommendRecycleView.layoutManager = GridLayoutManager(context,3)
        val mDividerItemDecoration = GridItemDecoration(context,LinearLayout.HORIZONTAL,3)
        mDividerItemDecoration.setDrawable(ContextCompat.getDrawable(context,R.drawable.transparent_divider)!!)
        recommendRecycleView.addItemDecoration(mDividerItemDecoration)
        recommendRecycleView.isNestedScrollingEnabled = true
    }

    fun dataChange(data: RecommendListModel) {

        recommendRecycleView.adapter = RecommendGridAdapter(context,data.song_list)
    }

    class RecommendGridAdapter(context: Context, song_list: List<RecommendListModel.songList>?) : RecyclerView.Adapter<RecommendViewHolder>() {

        private var context: Context = context
        private val ITEMCOUNT = 6
        private var hotList = song_list

        override fun getItemCount(): Int {

            return when {

                hotList?.isEmpty() ?: true -> 0
                hotList!!.size < 6 -> hotList!!.size
                else -> ITEMCOUNT
            }
        }

        override fun onBindViewHolder(holder: RecommendViewHolder, position: Int) {

            val vHotBean = hotList!![position] as RecommendListModel.songList

            holder.recommendView.setImageView(vHotBean.pic_big!!).setDescription(vHotBean.title!!)

            holder.recommendView.setOnClickListener {

                val songInfo = hotList!![position]

                MusicPlayer.playOnLine(songInfo.song_id!!)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendViewHolder {


            return RecommendViewHolder(RecommendItemView(context))
        }

        internal inner class PlayMusic(var position: Int) : Runnable {
            /**
             * 运行在主线程
             */
            override fun run() {

                val list = LongArray(hotList!!.size)

                val infoMap = hashMapOf<Long, MusicEntity>()

                for (i in hotList!!.indices) {

                    val info = hotList!![i]

//                    val musicEntity = MusicEntity()
//                    list[i] = info.song_id!!
//                    info.islocal = true
//                    infoMap[list[i]] = mList[i]
                }
                if (position > -1)
                    MusicPlayer.playAll(infoMap, list, position)
            }
        }


    }
    class RecommendViewHolder(itemView: RecommendItemView) : RecyclerView.ViewHolder(itemView) {
        var recommendView: RecommendItemView = itemView
    }

}