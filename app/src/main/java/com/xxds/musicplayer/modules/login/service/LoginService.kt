package com.xxds.cjs.modules.login.service

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.xxds.chitjishi.Common.net.ResultObserver
import com.xxds.chitjishi.Common.net.RetrofitFactory
//import com.xxds.cjs.common.net.socket.SocketManager
import com.xxds.cjs.modules.info.managers.UserManager
import com.xxds.cjs.modules.info.models.UserInfo
import com.xxds.cjs.modules.login.models.LoginModel
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {

    @POST("rest/login")
    fun login(@Body loginModel: LoginModel): Observable<Response<Map<String,Any>>>
}


class LoginRepository {

    companion object {

        var service = RetrofitFactory.createServie<LoginService>(LoginService::class.java)
        fun login(context: Context, email: String, password: String, loginResult: MutableLiveData<Boolean>) {

//            var body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),ss)
            var o = service.login(LoginModel(email,password))

            RetrofitFactory.excultResult(o,object : ResultObserver<Map<String,Any>>(){

                override fun onSuccess(result: Map<String, Any>?) {

                    val info = UserInfo(result!!["loginUser"] as Map<String, Any> )
                    info.token = result["userToken"] as String
                    UserManager.instance.userInfo = info
                    loginResult.value = true
//                    SocketManager.instance.sendUserInfo(info.token)

                }

                override fun onFailure(e: Throwable, isNetWorkError: Boolean) {

                    println("onFailure")
                }
            })
        }
    }
}