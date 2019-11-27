package com.xxds.musicplayer.common.net

import java.io.Serializable

class Common : Serializable {
    var ct: Int = 24
}
class CategoryBean : Serializable {
    var method: String? = null
    var param: ParamsBean = ParamsBean()
    var module: String? = null
}
class ParamsBean : Serializable {
    var async: Int = 0
    var cmd: Int = 0
    var sort: Int = 0
    var start: Int = 0
    var end: Int = 0
}
class CommonPostParams: Serializable {

    var comm: Common = Common()
    var recomPlaylist: CategoryBean = CategoryBean()
}