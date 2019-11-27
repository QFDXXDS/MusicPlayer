package com.xxds.chitjishi.Common.net

import android.accounts.NetworkErrorException
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.Response
import java.lang.Exception
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

abstract class ResultObserver<T>: Observer<Response<T>> {

    override fun onSubscribe(d: Disposable) {

        if (!d.isDisposed) {

            onRequestStart()
        }
    }

    override fun onNext(t: Response<T>) {

        onRequestEnd()
        if (t.isSuccessful) {

            onSuccess(t.body())
        } else {

            onInnerCodeError(t.code(),t.message())
        }
    }

    override fun onComplete() {


    }

    override fun onError(e: Throwable) {

        onRequestEnd()
        try {

            if (e is ConnectException
                    || e is TimeoutException
                    || e is NetworkErrorException
                    || e is UnknownHostException) {

                onFailure(e,true)
            } else {

                onFailure(e,false)
            }
        } catch (e1: Exception){

            e1.printStackTrace()
        }
    }


    open fun onRequestStart() {


    }

    open fun onRequestEnd() {


    }
    open fun onInnerCodeError(code: Int, message: String) {

        onCodeError(code,message)
    }
    /**
     * 返回成功了,但是code错误
     *
     * @param t
     * @throws Exception
     */
    @Throws(Exception::class)
    open fun onCodeError(code: Int, message: String) {

    }

    @Throws(Exception::class)
    abstract fun onSuccess(result: T?)
    /**
     * 返回失败
     *
     * @param e
     * @param isNetWorkError 是否是网络错误
     * @throws Exception
     */
    @Throws(Exception::class)
    abstract fun onFailure(e: Throwable,isNetWorkError: Boolean)

}