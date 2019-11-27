package com.xxds.cjs.modules.login.viewmodel

import android.app.Application
import android.content.Context
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xxds.chitjishi.Common.Config.AppConfig
import com.xxds.cjs.common.utils.Preference
import com.xxds.cjs.modules.login.service.LoginRepository
import io.reactivex.annotations.NonNull
import org.jetbrains.anko.toast

class LoginViewMode: ViewModel() {

    var emailStorage : String by Preference<String>("")

    var passwordStorage : String by Preference<String>("")

    var email = ObservableField<String>()

    var password = ObservableField<String>()

    var loginResult = MutableLiveData<Boolean>()

    init {
        //读取本地存储的用户名和密码
        email.set(emailStorage)
        password.set(passwordStorage)
    }

    fun login(context: Context) {


        emailStorage = email.get()!!.trim()
        passwordStorage = password.get()!!.trim()
        LoginRepository.login(context,emailStorage,passwordStorage,loginResult)
    }

    fun onSubmitClick(view: View) {

        val memail = email.get()
        val mpassword = password.get()

        memail?.apply {
            if (this.isEmpty()) {
                view.context.toast("邮箱不能为空")
                return
            }

        }
        mpassword?.apply {
            if (this.isEmpty()) {
                view.context.toast("密码不能为空")
                return
            }
        }
//返回视图运行的上下文，它可以通过上下文运行访问当前主题、资源等。
        login(view.context)

    }
}