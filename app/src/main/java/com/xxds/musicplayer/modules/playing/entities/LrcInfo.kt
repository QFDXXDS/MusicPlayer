package com.xxds.musicplayer.modules.playing.entities

import java.util.*

class LrcInfo {

    var lrcys_list: List<LrcysListBean>? = null

    var query: String? = null
    var pages: PagesBean? = null

    class PagesBean {
        /**
         * total : 12
         * rn_num : 10
         */

        var total: Int = 0
        var rn_num: Int = 0
    }

    class LrcysListBean {
        /**
         * lrclink : http://qukufile2.qianqian.com/data2/lrc/70682806/70682806.lrc
         */
        var lrclink: String? = null
    }

    override fun toString(): String {
        return Arrays.toString(lrcys_list!!.toTypedArray())
    }
}