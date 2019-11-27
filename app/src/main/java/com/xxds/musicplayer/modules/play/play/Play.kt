package com.xxds.musicplayer.modules.play.play

import androidx.lifecycle.Observer
import com.xxds.ApplicationMain
import com.xxds.musicplayer.modules.play.models.PlayModel
import com.xxds.musicplayer.modules.play.service.PlayResposity
import com.xxds.musicplayer.modules.play.service.PlayService
import com.xxds.musicplayer.modules.play.viewmodel.PlayViewModel

class Play {

    val viewModel = PlayViewModel()

    fun play( songIds: String) {

        viewModel.getData(songIds)
    }


}