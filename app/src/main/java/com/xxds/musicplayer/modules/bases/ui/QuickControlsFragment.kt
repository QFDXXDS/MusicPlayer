package com.xxds.musicplayer.modules.bases.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.xxds.musicplayer.R
import com.xxds.musicplayer.common.utils.getLargeImageUrl
import com.xxds.musicplayer.common.utils.isContentEmpty
import com.xxds.musicplayer.common.utils.loadUrl
import com.xxds.musicplayer.modules.playing.ui.PlayingActivity
import com.xxds.musicplayer.modules.service.MusicPlayer
import kotlinx.android.synthetic.main.view_quick_controls.*
import org.jetbrains.anko.ScreenSize

class QuickControlsFragment: BaseFragment(),View.OnClickListener {

    private lateinit var circleAnim: ObjectAnimator

    companion object {
        fun newInstance(): QuickControlsFragment {
            return QuickControlsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate( R.layout.view_quick_controls,container,false)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val albumCenterPoint = resources.getDimension(R.dimen.bottom_fragment_album_size) / 2
        play_bar_img.pivotX = albumCenterPoint
        play_bar_img.pivotY = albumCenterPoint

        circleAnim = ObjectAnimator.ofFloat(play_bar_img,"rotation",0f,360f).apply {
            interpolator = LinearInterpolator()
            repeatCount = -1
            duration = 12000
            start()
        }




//        android:elevation这一属性，可以控制View底部渐变阴影，给一个View在其底部增加一定的灰度渐变阴影效果
        view.elevation = 100f

        view.setOnClickListener(this)
        play_list.setOnClickListener(this)
        control.setOnClickListener(this)
        play_next.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        updateFragment()

    }
    override fun onClick(p0: View?) {

        when(p0) {

            play_list -> {}
            control -> { MusicPlayer.playOrPause() }
            play_next -> { MusicPlayer.nextPlay() }
            else -> {
                val intent = Intent(activity, PlayingActivity::class.java)
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out)


            }
        }

    }
    override fun updatePlayInfo() {
        updateFragment()
    }

    private fun updateFragment() {

        play_bar_img.loadUrl(MusicPlayer.getAlbumPic(),R.drawable.player_albumcover_default)

        if (MusicPlayer.getTrackName().isContentEmpty() && MusicPlayer.getArtistName().isContentEmpty()) {

            play_bar_song_name.visibility = View.GONE
            play_bar_singer.visibility = View.GONE
            play_bar_img.visibility = View.GONE
            music_logan.visibility = View.VISIBLE

        } else {

            play_bar_song_name.visibility = View.VISIBLE
            play_bar_singer.visibility = View.VISIBLE
            play_bar_img.visibility = View.VISIBLE
            music_logan.visibility = View.GONE

            play_bar_song_name.text = MusicPlayer.getTrackName()
            play_bar_singer.text = MusicPlayer.getArtistName()
        }
        if (MusicPlayer.isPlaying()) {

            control.setImageResource(R.drawable.playbar_btn_pause)
            circleAnim.resume()
        } else {
            control.setImageResource(R.drawable.playbar_btn_play)
            circleAnim.pause()
        }
    }
    }