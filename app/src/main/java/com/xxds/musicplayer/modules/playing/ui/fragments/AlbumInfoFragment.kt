package com.xxds.musicplayer.modules.playing.ui.fragments


import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView

import com.xxds.musicplayer.R
import com.xxds.musicplayer.common.utils.getMegaImageUrl
import com.xxds.musicplayer.common.utils.loadUrl
import com.xxds.musicplayer.modules.playing.ui.CircleImageView
import com.xxds.musicplayer.modules.service.MusicPlayer

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class AlbumInfoFragment : Fragment() {


    companion object {
        const val TAG = "AlbumInfoFragment"
        const val ANIMATION_DURATION = 30000L // 30s

    }
    private lateinit var albumView: CircleImageView
    private lateinit var artistName: TextView
    private lateinit var circleAnim: ObjectAnimator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_playing_album_info, container, false)
        view.tag = TAG

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        albumView = view.findViewById(R.id.album_pic)
        artistName = view.findViewById(R.id.artist_name)

        circleAnim = ObjectAnimator.ofFloat(albumView, "rotation", 0f, 360f).apply {
            interpolator = LinearInterpolator()
            repeatCount = -1
            duration = ANIMATION_DURATION
            start()
        }

        artistName.text = MusicPlayer.getArtistName()
        if (MusicPlayer.getAlbumPic().isEmpty()) {
            albumView.setImageResource(R.drawable.player_albumcover_default)
        } else {
            albumView.loadUrl(MusicPlayer.getAlbumPic().getMegaImageUrl(), R.drawable.player_albumcover_default)
        }


    }

    override fun onResume() {
        super.onResume()
        setAnimation()
    }

    private fun setAnimation() {

        if (MusicPlayer.isPlaying()) {
            if (circleAnim.isPaused){

                circleAnim.resume()
            }
        } else {

            if (circleAnim.isRunning){

                circleAnim.pause()
            }

        }

    }

        override fun onStop() {
        super.onStop()
        circleAnim.end()
    }



}
