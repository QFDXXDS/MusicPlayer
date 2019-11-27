package com.xxds.musicplayer.modules.playing.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.xxds.musicplayer.R
import com.xxds.musicplayer.common.utils.getImageId
import com.xxds.musicplayer.common.utils.getLargeImageUrl
import com.xxds.musicplayer.common.utils.loadUrl
import com.xxds.musicplayer.modules.playing.entities.OtherVersionInfo
import com.xxds.musicplayer.modules.playing.entities.SimilarSongInfo

/**
 * TODO: document your custom view class.
 */
class PlayingSongRelatedInfoView : LinearLayout {

    private lateinit var firstContainer: LinearLayout
    private lateinit var secondContainer: LinearLayout
    private lateinit var thirdContainer: LinearLayout

    private lateinit var firstTitle: TextView
    private lateinit var secondTitle: TextView
    private lateinit var thirdTitle: TextView

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {

    }

    fun init(context: Context){

        orientation = LinearLayout.VERTICAL
        View.inflate(context, R.layout.sample_playing_song_related_info_view, this)
        firstContainer = findViewById(R.id.first_container)
        secondContainer = findViewById(R.id.second_container)
        thirdContainer = findViewById(R.id.third_container)

        firstTitle = findViewById(R.id.first_title)
        secondTitle = findViewById(R.id.second_title)
        thirdTitle = findViewById(R.id.third_title)
    }

    fun addOtherVersionInfo(otherVersionInfo: OtherVersionInfo) {
        firstContainer.removeAllViews()
        firstTitle.visibility = View.GONE

        if (otherVersionInfo.track != null && otherVersionInfo.track!!.isNotEmpty()) {
            firstTitle.visibility = View.VISIBLE
        } else {
            firstTitle.visibility = View.GONE
        }

        otherVersionInfo.track?.forEach { bean ->
            val itemView = generateItemView()
            itemView.title.text = bean.name
            itemView.subTitle.text = bean.artist
            bean.image?.run {
                val mid = get(0).imgUrl.getImageId()
                itemView.headImg.loadUrl(mid.getLargeImageUrl())
            }
            firstContainer.addView(itemView)
        }
    }

    fun addSimilarInfo(similarSongInfo: SimilarSongInfo) {
        secondContainer.removeAllViews()

        if (similarSongInfo.track != null && similarSongInfo.track!!.isNotEmpty()) {
            secondTitle.visibility = View.VISIBLE
        } else {
            secondTitle.visibility = View.GONE
        }
        similarSongInfo.track?.forEach { bean ->
            val itemView = generateItemView()
            itemView.title.text = bean.name
            itemView.subTitle.text = bean.artist?.name
            bean.image?.run {
                val mid = get(0).imgUrl.getImageId()
                itemView.headImg.loadUrl(mid.getLargeImageUrl())
            }
            secondContainer.addView(itemView)
        }
    }

    private fun generateItemView(): ContentItemView {

        val itemView = ContentItemView(context)
        val itemHeight = resources.getDimensionPixelOffset(R.dimen.content_image_head)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHeight)
        params.bottomMargin = resources.getDimensionPixelOffset(R.dimen.content_item_bottom_margin)
        itemView.layoutParams = params

        return itemView
    }

}
