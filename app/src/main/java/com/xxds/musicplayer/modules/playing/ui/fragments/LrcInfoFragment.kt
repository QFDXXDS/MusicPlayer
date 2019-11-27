package com.xxds.musicplayer.modules.playing.ui.fragments


import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.xxds.musicplayer.R
import com.xxds.musicplayer.common.utils.PlayTimeListener
import com.xxds.musicplayer.modules.bases.ui.BaseFragment
import com.xxds.musicplayer.modules.playing.manager.GlobalPlayTimeManager
import com.xxds.musicplayer.modules.playing.ui.views.LrcView
import com.xxds.musicplayer.modules.service.MediaService
import com.xxds.musicplayer.modules.service.MusicPlayer
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class LrcInfoFragment : BaseFragment(), PlayTimeListener {

    private lateinit var lrcView: LrcView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalPlayTimeManager.registerListener(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_playing_lrc_info, container, false)
        lrcView = view.findViewById(R.id.playing_lrc_view)
        view.tag = TAG
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lrcView.updateTime(0)
    }

    override fun onResume() {
        super.onResume()

        updateLrc()
    }


    override fun onDestroy() {
        super.onDestroy()

        GlobalPlayTimeManager.unregisterListener(this)
    }


    override fun playedTime(position: Long, duration: Long) {

        lrcView.updateTime(position)
    }


    companion object {
        const val TAG = "LrcInfoFragment"

    }

    override fun updateLrc() {


        val file = File(Environment.getExternalStorageDirectory().absolutePath +MediaService.LRC_PATH + MusicPlayer.getAudioId() + ".lrc")
        if (file.exists()) {

            lrcView.loadLrc(file)
        }
    }



}
