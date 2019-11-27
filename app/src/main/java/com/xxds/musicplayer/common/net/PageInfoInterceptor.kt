package com.xxds.chitjishi.Common.net

import okhttp3.Interceptor
import okhttp3.Response

//class PageInfoInterceptor: Interceptor {
//
//    override fun intercept(chain: Interceptor.Chain): Response {
//
//        var request = chain.request()
//        var response = chain.proceed(request)
//        var linkString = response.headers()["Link"]
//        var pageInfo = PageInfo()
//
//    }
//
//
//}
//
//data class PageInfo(
//        var prev: Int = -1,
//        var next: Int = -1,
//        var last: Int = -1,
//        var first: Int = -1
//)