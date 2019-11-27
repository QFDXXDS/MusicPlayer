package com.xxds.musicplayer.modules.service.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xxds.chitjishi.Common.net.ResultObserver
import com.xxds.chitjishi.Common.net.RetrofitFactory
import com.xxds.musicplayer.R
import com.xxds.musicplayer.modules.main.service.MainService
import com.xxds.musicplayer.modules.playing.entities.LrcInfo
import com.xxds.musicplayer.modules.playing.entities.SongInfo
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import okhttp3.ResponseBody




interface Ting {

    @GET("v1/restserver/ting")
    fun getLrcInfo(
        @Query("method") method: String,
        @Query("query") query: String
    ): Observable<Response<LrcInfo>>

    @GET
    fun downloadPicFromNet(@Url fileUrl: String): Observable<Response<ResponseBody>>


    @GET("data/music/links")
    fun getSong(@Query("songIds") songIds: Long): Observable<Response<SongInfo>>


}



class ServiceViewModel: ViewModel() {

    val lrcInfoData = MutableLiveData<LrcInfo>()
    val lrcData = MutableLiveData<ResponseBody>()
    val songInfo = MutableLiveData<SongInfo>()


    fun getLrcInfo(song: String) {

        MeidaRepository.getLrcInfo(song,lrcInfoData)
    }

    fun downloadPicFromNet(fileUrl: String) {


        MeidaRepository.downloadPicFromNet(fileUrl,lrcData)

    }
    fun getSong(data: Long) {


        MeidaRepository.getSongInfo(data,songInfo)
    }


}

class MeidaRepository  {

    companion object {


        val  ting =  RetrofitFactory.LrcInfoRetrofit.create(Ting::class.java)

        fun getLrcInfo(song: String, data: MutableLiveData<LrcInfo> ) {

           val o =  ting.getLrcInfo("baidu.ting.search.lrcys",song)

            RetrofitFactory.excultResult(o, object : ResultObserver<LrcInfo>() {

                override fun onSuccess(result: LrcInfo?) {

                    data.value = result
                }

                override fun onFailure(e: Throwable, isNetWorkError: Boolean) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })

        }

        fun downloadPicFromNet(fileUrl: String,data: MutableLiveData<ResponseBody>) {

            val o =  ting.downloadPicFromNet(fileUrl)

            RetrofitFactory.excultResult(o, object : ResultObserver<ResponseBody>() {

                override fun onSuccess(result: ResponseBody?) {

                    data.value = result as ResponseBody
                }

                override fun onFailure(e: Throwable, isNetWorkError: Boolean) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

                    println()
                }

            })

        }

        val songservice =  RetrofitFactory.MusicInfoRetrofit.create(Ting::class.java)

        fun getSongInfo(data: Long ,model: MutableLiveData<SongInfo>) {


            val o = songservice.getSong(data)

            RetrofitFactory.excultResult(o,object : ResultObserver<SongInfo>(){

                override fun onSuccess(result: SongInfo?) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    model.value = result
                }

                override fun onFailure(e: Throwable, isNetWorkError: Boolean) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

                    println(e)
                }
            })
        }
    }

}
