package com.xxds.musicplayer.modules.main.models

import com.xxds.musicplayer.common.net.HTTPReq

data class rcdModel(var page: Int, var size:Int): HTTPReq() {

    var method = "baidu.ting.billboard.billList"
    var type = "2"
    var format = "json"

    var limits = "50"
    var order = "1"
    var page_no = "1"

}