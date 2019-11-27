package com.xxds.musicplayer.common.net

import java.io.Serializable

class ExpressPostParams: Serializable {


    var comm: Common = Common()
    var new_album: CategoryBean = CategoryBean()
    var new_song: CategoryBean = CategoryBean()


}