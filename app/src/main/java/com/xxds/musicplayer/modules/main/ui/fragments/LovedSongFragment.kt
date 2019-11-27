package com.xxds.musicplayer.modules.main.ui.fragments

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.xxds.musicplayer.R
import com.xxds.musicplayer.common.utils.LOVE_SONG_CONTENT_URI
import com.xxds.musicplayer.common.utils.QUERY_LOVE_SONG
import com.xxds.musicplayer.common.utils.parseLoveSongCursorToMusicEntityList
import com.xxds.musicplayer.modules.bases.ui.BasePageFragment
import com.xxds.musicplayer.modules.entity.MusicEntity
import com.xxds.musicplayer.modules.main.adapter.MusicListAdapter


class LovedSongFragment : BasePageFragment(),LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        fun newInstance(): LovedSongFragment {
            return LovedSongFragment()
        }
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MusicListAdapter
    private val musicList = mutableListOf<MusicEntity>()


    override fun createContentView(inflater: LayoutInflater, container: ViewGroup?): View {

        recyclerView = inflater.inflate(R.layout.fragment_loved_song, container, false) as RecyclerView
        return  recyclerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setTitle("我喜欢的")
        adapter = MusicListAdapter(context!!,musicList)

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        mLoaderManager.initLoader(QUERY_LOVE_SONG, null, this)

    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(context!!, LOVE_SONG_CONTENT_URI, null, null, null, null)

    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {

        parseLoveSongCursorToMusicEntityList(QUERY_LOVE_SONG, data, musicList)
        adapter.notifyDataSetChanged()
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {

    }




}
