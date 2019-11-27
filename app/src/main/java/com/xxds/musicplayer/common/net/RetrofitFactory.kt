package com.xxds.chitjishi.Common.net

import android.database.Observable
import android.text.TextUtils
import com.bumptech.glide.Glide.init
import com.xxds.chitjishi.Common.Config.AppConfig
import com.xxds.musicplayer.BuildConfig
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.reactivestreams.Subscriber
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.HttpUrl



class RetrofitFactory {

    val retrofit: Retrofit
    val mOkHttpClient: OkHttpClient by lazy {

        val logging = HttpLoggingInterceptor()
        logging.level = if(BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY else
            HttpLoggingInterceptor.Level.NONE
        var okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(headerInterceptor())
//                .addInterceptor(null)
            .connectTimeout(TIMEOUT.toLong(),TimeUnit.SECONDS)
            .readTimeout(TIMEOUT.toLong(),TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT.toLong(),TimeUnit.SECONDS)
            .build()

        return@lazy okHttpClient
    }
    val baseCRetrofit: Retrofit by lazy {

         createRetrofit(AppConfig.API_BASE_C_URL)
    }
    val baseURetrofit: Retrofit by lazy {

        createRetrofit(AppConfig.API_BASE_U_URL)
    }
    val lrcInfoRetrofit: Retrofit by lazy {

        createRetrofit(AppConfig.API_TINGAPI_BAIDU)
    }
    val musicInfoRetrofit: Retrofit by lazy {

        createRetrofit(AppConfig.API_TING_BAIDU)
    }



//        get() {
//            synchronized(RetrofitFactory::class) {
//
//                if (mBaseCRetrofit == null) {
//                    mBaseCRetrofit = instance.createRetrofit(AppConfig.API_BASE_C_URL)
//                }
//            }
//            return mBaseCRetrofit!!
//        }

    init {

        val logging = HttpLoggingInterceptor()
        logging.level = if(BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY else
            HttpLoggingInterceptor.Level.NONE
        var mOkHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(headerInterceptor())
//                .addInterceptor(null)
                .connectTimeout(TIMEOUT.toLong(),TimeUnit.SECONDS)
                .readTimeout(TIMEOUT.toLong(),TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT.toLong(),TimeUnit.SECONDS)
                .build()
        retrofit = Retrofit.Builder()
                .baseUrl(AppConfig.API_TINGAPI_BAIDU)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mOkHttpClient)
                .build()

    }


    /**
     * 拦截头部增加Content-Type,Accept-Encoding,User-Agent
     */
    private fun headerInterceptor(): Interceptor {

        return Interceptor {

//            var newBaseUrl: String = AppConfig.MS_LIST_URL
//
            var request = it.request()
            val builder = request.newBuilder()
//
//            val headerValues = request.headers("url_name")
//            if (headerValues != null && headerValues.size > 0) {
//
//                builder.removeHeader("url_name")
//                val headerValue = headerValues[0]
//                if ("song" == headerValue) {
//                    newBaseUrl = AppConfig.MS_SONG_URL
//
//                }
//            }
            var url = request.url().toString()
//
//            var urlcomponents = TextUtils.split(url,"com/")
//            urlcomponents[0] = newBaseUrl
//            url = TextUtils.join("",urlcomponents)
            request = builder
                    .addHeader("Content-Type","application/json;charset=utf-8" )
                    .addHeader("Accept-Encoding","application/json")
                    .addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                    .url(url)
                    .build()

            it.proceed(request)
        }
    }



    companion object {
        const val TIMEOUT = 30;

        @Volatile
        private var  mRetrofitFactory: RetrofitFactory? = null
        val instance: RetrofitFactory
            get() {
                synchronized(RetrofitFactory::class) {

                    if (mRetrofitFactory == null) {
                        mRetrofitFactory = RetrofitFactory()
                    }
                }
                return mRetrofitFactory!!
            }

        fun <T>createBaseCRetrofit(t: Class<T>): T {

            return  baseCRetrofit.create(t)
        }
        fun <T>createBaseURetrofit(t: Class<T>): T {

            return  baseURetrofit.create(t)
        }
        val baseCRetrofit: Retrofit
            get() {

            return instance.baseCRetrofit
            }
        val baseURetrofit: Retrofit
            get() {

                return instance.baseURetrofit
            }

        val LrcInfoRetrofit: Retrofit
        get() {

            return instance.lrcInfoRetrofit
        }
        val MusicInfoRetrofit: Retrofit
            get() {

                return instance.musicInfoRetrofit
            }


        fun <T> createServie(service: Class<T>): T {

           return instance.retrofit.create(service)
        }

        fun <T> excultResult(observable: io.reactivex.Observable<Response<T>>,subscriber: ResultObserver<T>){

             observable.subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(subscriber)
        }

        fun createRetrofit(url: String): Retrofit {

            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(instance.mOkHttpClient)
                .build()
        }
    }

}