package com.xxds.musicplayer.modules.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.xxds.musicplayer.common.net.CommonPostParams
import com.xxds.musicplayer.common.net.ExpressPostParams
import com.xxds.musicplayer.modules.main.models.ExpressInfoModel
import com.xxds.musicplayer.modules.main.models.HallModel
import com.xxds.musicplayer.modules.main.models.RecommendListModel
import com.xxds.musicplayer.modules.main.models.rcdModel
import com.xxds.musicplayer.modules.main.service.MainRepository
import com.xxds.musicplayer.modules.main.service.MainService

class MainViewModel(var page: Int = 0,var size: Int = 20) : ViewModel() {


    val data = MutableLiveData<ArrayList<Any>> ()

    init {
        getData()
    }

    fun  getData() {
        val req = rcdModel(page,size)
//        MainRepository.getData(req, data)
    }

}

class MainMusicViewModel() : ViewModel() {


    val hallModel = MutableLiveData<HallModel> ()
    val recommendList = MutableLiveData<RecommendListModel>()
    val expressInfoModel = MutableLiveData<ExpressInfoModel>()


    init {
        getData()
    }

    fun getSong(songId: Long) {


    }

    fun  getData() {

        MainRepository.getMusicHall(hallModel)

//        val params = CommonPostParams()
//        params.recomPlaylist.method = "get_hot_recommend"
//        params.recomPlaylist.module = "playlist.HotRecommendServer"
//        params.recomPlaylist.param.async = 1
//        params.recomPlaylist.param.cmd = 2
//        MainRepository.getMusicRetrofit(Gson().toJson(params),recommendList)

        val req = rcdModel(1,6)
        MainRepository.getData(req, recommendList)




        val expressPostParams = ExpressPostParams()
        expressPostParams.new_album.method = "GetNewSong"
        expressPostParams.new_album.module = "QQMusic.MusichallServer"
        expressPostParams.new_album.param.sort = 1
        expressPostParams.new_album.param.start = 0
        expressPostParams.new_album.param.end = 0

        expressPostParams.new_song.method = "GetNewAlbum"
        expressPostParams.new_song.module = "QQMusic.MusichallServer"
        expressPostParams.new_song.param.sort = 1
        expressPostParams.new_song.param.start = 0
        expressPostParams.new_song.param.end = 1

        MainRepository.getNewSongInfo(Gson().toJson(expressPostParams),expressInfoModel)

    }

}

