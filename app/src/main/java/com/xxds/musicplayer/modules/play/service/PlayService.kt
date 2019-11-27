package com.xxds.musicplayer.modules.play.service

import androidx.lifecycle.MutableLiveData
import com.xxds.chitjishi.Common.net.ResultObserver
import com.xxds.chitjishi.Common.net.RetrofitFactory
import com.xxds.cjs.modules.login.models.LoginModel
import com.xxds.musicplayer.common.net.HTTPInterface
import com.xxds.musicplayer.modules.main.service.MainRepository
import com.xxds.musicplayer.modules.main.service.MainService
import com.xxds.musicplayer.modules.play.models.PlayModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*


interface PlayService {

    @Headers("url_name:song")
    @GET("data/music/links")
    fun getSong(@QueryMap map: MutableMap<String,Any>): Observable<Response<Map<String,Any>>>
}

class  PlayResposity {

    companion object {

        val service = RetrofitFactory.createServie(PlayService::class.java)

        fun getData(req: HTTPInterface, data: MutableLiveData<PlayModel>) {

            val o = PlayResposity.service.getSong(req.parameters())

            RetrofitFactory.excultResult(o,object : ResultObserver<Map<String,Any>>(){

                override fun onSuccess(result: Map<String, Any>?) {

                    val map = result!!["data"] as Map<String, Any>
                    val songList = map["songList"] as ArrayList<Any>
                    val map1 = songList[0] as Map<String, String>

                    val model = PlayModel(map1)
                    data.value = model
                }

                override fun onFailure(e: Throwable, isNetWorkError: Boolean) {

                    print(e)
                }
            })
        }
    }
}