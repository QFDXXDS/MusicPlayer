package com.xxds.musicplayer.modules.playing.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader

import com.xxds.musicplayer.R
import com.xxds.musicplayer.common.utils.GET_RELATED_SONG
import com.xxds.musicplayer.common.utils.GET_SIMILAR_SONG
import com.xxds.musicplayer.modules.bases.ui.BaseFragment
import com.xxds.musicplayer.modules.playing.ui.views.PlayingSongRelatedInfoView
import com.xxds.musicplayer.modules.service.MusicPlayer
import retrofit2.Call

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SongInfoFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SongInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SongInfoFragment : BaseFragment() {

    private lateinit var songOrigin: TextView
    private lateinit var songArtist: TextView
    private lateinit var songAlbum: TextView
    private lateinit var songInfo: TextView
    private lateinit var songInfoDetail: TextView
    private lateinit var playingSongRelated: PlayingSongRelatedInfoView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_playing_song_info, container, false)
        songOrigin = view.findViewById(R.id.song_origin)
        songArtist = view.findViewById(R.id.song_artist)
        songAlbum = view.findViewById(R.id.song_album)
        songInfo = view.findViewById(R.id.playing_song_info)
        songInfoDetail = view.findViewById(R.id.playing_song_info_detail)
        playingSongRelated = view.findViewById(R.id.related_song_block)
        view.tag = TAG


        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updateData()
    }
    private fun updateData() {
        if (MusicPlayer.isTrackLocal()) {
            songOrigin.text = "本地音乐"
        } else {
            songOrigin.text = "在线音乐"
        }
        songArtist.text = MusicPlayer.getArtistName()
        songAlbum.text = MusicPlayer.getAlbumName()
        songInfo.text = resources.getText(R.string.playing_song_info)

        if (!MusicPlayer.getTrackName().isEmpty()) {
//            mLoaderManager.run {
//                restartLoader(GET_RELATED_SONG, null, buildRelatedSongCallback(MusicPlayer.getTrackName()))
//                restartLoader(GET_SIMILAR_SONG, null, buildSimilarSongCallback(MusicPlayer.getTrackName(), MusicPlayer.getArtistName()))
//            }
        }
    }




    companion object {
        const val TAG = "SongInfoFragment"
    }
}
