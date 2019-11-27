package com.xxds.chitjishi.Common.Config

import com.xxds.ApplicationMain
import java.io.IOException

typealias GNSuccessBlock = (Any) -> Unit
typealias GNFailBlock = (IOException) -> Unit

object AppConfig {

    const val  PORTAL_WS_PREFIX = "ws://"

    const val  PORTAL_WS_SUFFIX  = ":7010/jc-uccp/ws/uccp.do"

    const val  PORTAL_WS_SUFFIX_SUFFIX = "/jc-uccp/ws/uccp.do"

    const val PORTAL_HTTP_PREFIX = "http://"

    const val PORTAL_HTTP_SUFFIX = ":7010/jc-portal/"

    const val PORTAL_BASEURL = PORTAL_HTTP_PREFIX + "cossdev.byshang.cn" + PORTAL_HTTP_SUFFIX

    const val EMAIL = "email"

    const val PASSWORD = "password"



    const val GITHUB_BASE_URL = "https://github.com/"

    const val GITHUB_API_BASE_URL = "https://api.github.com/"

    const val GITHUB_CONTENT_BASE_URL = "https://raw.githubusercontent.com/"

    const val GRAPHIC_HOST = "https://ghchart.rshah.org/"

    const val PAGE_SIZE = 30

    const val HTTP_TIME_OUT = 20 * 1000L

    const val HTTP_MAX_CACHE_SIZE = 16 * 1024 * 1024L

    const val IMAGE_MAX_CACHE_SIZE = 16 * 1024 * 1024L

    const val CACHE_MAX_AGE = 7 * 24 * 60 * 60L

    const val ACCESS_TOKEN = "accessToken"

    const val USER_BASIC_CODE = "userBasicCode"

    const val USER_NAME = "user_name"


    const val USER_INFO = "userInfo"


    const val MS_LIST_URL = "http://tingapi.ting.baidu.com/"

    const val MS_SONG_URL = "http://ting.baidu.com/"


    const val API_BASE_C_URL = "http://c.y.qq.com/"
    const val API_BASE_U_URL = "http://u.y.qq.com/"
    const val API_LAST_FM_URL = "http://ws.audioscrobbler.com/"
//    歌词信息
    const val API_TINGAPI_BAIDU = "http://tingapi.ting.baidu.com/"
//   歌曲信息
    const val API_TING_BAIDU = "http://ting.baidu.com/"


    // 腾讯图片地址
    const val picBaseUrl_300 = "http://y.gtimg.cn/music/photo_new/T001R300x300M000%1\$s.jpg"
    const val picBaseUrl_150 = "http://y.gtimg.cn/music/photo_new/T002R150x150M000%1\$s.jpg"

    // LastFm图片地址
    const val LAST_FM_IMAGE_MEDIUM = "https://lastfm-img2.akamaized.net/i/u/64s/%s.png"
    const val LAST_FM_IMAGE_LARGE = "https://lastfm-img2.akamaized.net/i/u/174s/%s.png"
    const val LAST_FM_IMAGE_MEGA = "https://lastfm-img2.akamaized.net/i/u/300x300/%s.png"


    const val APP_NAME = "SE_MUSIC"
    const val TOAST_SHOW_DURATION = 2000L

    // 数据库
    const val DATABASE_NAME = "SEMusic.db"
    const val DATABASE_VERSION = 1



}