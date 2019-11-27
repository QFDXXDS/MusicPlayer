package com.xxds.musicplayer.modules.play.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xxds.musicplayer.modules.main.models.rcdModel
import com.xxds.musicplayer.modules.main.service.MainRepository
import com.xxds.musicplayer.modules.play.models.PlayModel
import com.xxds.musicplayer.modules.play.models.PlayReqModel
import com.xxds.musicplayer.modules.play.service.PlayResposity

class PlayViewModel : ViewModel() {


    val data = MutableLiveData<PlayModel> ()

    init {

    }

    fun  getData(songIds: String) {
        val  req = PlayReqModel(songIds)
        PlayResposity.getData(req,data)
    }

}