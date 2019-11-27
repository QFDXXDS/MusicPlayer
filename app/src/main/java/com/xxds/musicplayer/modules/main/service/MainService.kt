package com.xxds.musicplayer.modules.main.service

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.xxds.chitjishi.Common.net.ResultObserver
import com.xxds.chitjishi.Common.net.RetrofitFactory
import com.xxds.chitjishi.Common.net.RetrofitFactory.Companion.baseCRetrofit
import com.xxds.chitjishi.Common.net.RetrofitFactory.Companion.createBaseURetrofit
import com.xxds.chitjishi.Common.net.RetrofitFactory.Companion.createServie
import com.xxds.musicplayer.common.net.HTTPInterface
import com.xxds.musicplayer.modules.main.models.*
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.QueryMap
import java.util.*
import kotlin.collections.ArrayList

interface MainService {

    @GET("v1/restserver/ting")
    fun getRcdSongList(@QueryMap map: MutableMap<String,Any>): Observable<Response<RecommendListModel>>

}
interface QQ {
    @GET("musichall/fcgi-bin/fcg_yqqhomepagerecommend.fcg")
    fun getMusicHallService(): Observable<Response<HallModel>>

    @GET("cgi-bin/musicu.fcg")
    fun getRecommendList(@Query("data") data: String): Observable<Response<RecommendListModel>>

    @GET("v8/fcg-bin/v8.fcg")
    fun getSinger(@QueryMap map: Map<String, String>, @Query("pagesize") pagesize: Int, @Query("pagenum") pagenum: Int): Observable<Response<SingerModel>>

    @GET("cgi-bin/musicu.fcg")
    fun getNewSongInfo(@Query("data") data: String): Observable<Response<ExpressInfoModel>>
}


class  MainRepository {


    companion object {

        val service =  RetrofitFactory.createServie(MainService::class.java)


        fun getData(req: HTTPInterface, data: MutableLiveData<RecommendListModel>) {

            val o = service.getRcdSongList(req.parameters())

            RetrofitFactory.excultResult(o,object : ResultObserver<RecommendListModel>() {

                override fun onSuccess(result: RecommendListModel?) {

//                    val song_list = result!!["song_list"] as ArrayList<Any>
//                    val songlists = ArrayList<Any>()
//                    song_list.forEach {
//
//                        val item = it as Map<String, Any>
//                        val model = MainDataModel(item)
//                        songlists.add(model)
//                    }
                    data.value = result

                }
                override fun onFailure(e: Throwable, isNetWorkError: Boolean) {

                println(e.message)

//                    val item = mapOf(Pair("title","hh"))
//                    val model = MainDataModel(item)
//
//                    val songlists = ArrayList<Any>()
//                    songlists.add(model)
//                    songlists.add(model)
//                    data.value = songlists
                }

            })
        }

        fun getMusicHall(model: MutableLiveData<HallModel>) {

            val QQservice =  RetrofitFactory.createBaseCRetrofit(QQ::class.java)

            val o = QQservice.getMusicHallService()
            RetrofitFactory.excultResult(o,object : ResultObserver<HallModel>(){

                override fun onSuccess(result: HallModel?) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    model.value = result
                }

                override fun onFailure(e: Throwable, isNetWorkError: Boolean) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

                    println(e)
                }
            })
        }
        fun getMusicRetrofit(data: String ,model: MutableLiveData<RecommendListModel>) {

            val QQservice =  RetrofitFactory.createBaseURetrofit(QQ::class.java)

            val o = QQservice.getRecommendList(data)
            RetrofitFactory.excultResult(o,object : ResultObserver<RecommendListModel>(){

                override fun onSuccess(result: RecommendListModel?) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    model.value = result
                }

                override fun onFailure(e: Throwable, isNetWorkError: Boolean) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

                    println(e)
                }
            })
        }


        fun getNewSongInfo(data: String ,model: MutableLiveData<ExpressInfoModel>) {

            val QQservice =  RetrofitFactory.createBaseURetrofit(QQ::class.java)

            val o = QQservice.getNewSongInfo(data)
            RetrofitFactory.excultResult(o,object : ResultObserver<ExpressInfoModel>(){

                override fun onSuccess(result: ExpressInfoModel?) {
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