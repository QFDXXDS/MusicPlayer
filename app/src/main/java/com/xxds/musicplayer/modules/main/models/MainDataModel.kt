package com.xxds.musicplayer.modules.main.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class  MainDataModel(val map: Map<String,Any>
)
{

val title: String by map
val song_id: String  by map
val artist_name: String by map
val pic_big: String by map
val hot: String by map
val file_duration: String by map
val displayName:String by map
val phone:String by map



}

class HallModel {
    var code: Int = 0
    var data: Data? = null

    class Data {
        var slider: List<Slider>? = null
        var radioList: List<RadioList>? = null

        class Slider {
            var linkUrl: String? = null
            var picUrl: String? = null
            var id: Int = 0
        }

        class RadioList {
            var picUrl: String? = null
            var ftitle: String? = null
            var radioid: Int = 0
        }
    }
}
class RecommendListModel {
    var recomPlaylist: RecomPlaylistBean? = null
    var code: Int = 0
    var ts: Long = 0

    inner class RecomPlaylistBean {
        var data: DataBean? = null
        var code: Int = 0
    }

    inner class DataBean {
        var v_hot: List<VHotBean>? = null
    }

    inner class VHotBean {
        var album_pic_mid: String? = null
        var content_id: Long = 0
        var cover: String? = null
        var creator: Long = 0
        var edge_mark: String? = null
        var id: Int = 0
        var is_dj: Boolean = false
        var is_vip: Boolean = false
        var jump_url: String? = null
        var listen_num: Int = 0
        var pic_mid: String? = null
        var rcmdcontent: String? = null
        var rcmdtemplate: String? = null
        var rcmdtype: Int = 0
        var singerid: Int = 0
        var title: String? = null
        var tjreport: String? = null
        var type: Int = 0
        var username: String? = null
    }

    var song_list: List<songList>? = null
//                    val songlists = ArrayList<Any>()
//                    song_list.forEach {
//
//                        val item = it as Map<String, Any>
//                        val model = MainDataModel(item)
//                        songlists.add(model)
//                    }
//                    data.value = songlists

    class  songList {

        var title: String? = null
        val song_id: Long? = 0
        val artist_name: String? = null
        val pic_big: String? = null
        val hot: String? = null
        val file_duration: String? = null
        val displayName:String? = null
        val phone:String? = null


    }
}
class SingerModel {

    var code: Int = 0
    var data: Data? = null
    var message: String? = null
    var subcode: Int = 0

    class Data {
        var per_page: Int = 0
        var total: Int = 0
        var total_page: Int = 0
        var list: List<Singer>? = null

        class Singer {
            var Farea: String? = null
            var Fattribute_3: String? = null
            var Fattribute_4: String? = null
            var Fgenre: String? = null
            var Findex: String? = null
            var Fother_name: String? = null
            var Fsinger_id: String? = null
            var Fsinger_mid: String? = null
            var Fsinger_name: String? = null
            var Fsinger_tag: String? = null
            var Fsort: String? = null
            var Ftrend: String? = null
            var Ftype: String? = null
            var voc: String? = null
        }
    }
}

class ExpressInfoModel : Serializable {
    var new_song: NewSong? = null
    var new_album: NewAlbum? = null
    var code: Int = 0
    var ts: Long = 0
}

class NewSong {
    var data: DataBean? = null
    var code: Int = 0

    class DataBean {
        var size: Int = 0
        var type: Int = 0
        var album_list: List<AlbumListBean>? = null
        var category_info: List<CategoryInfoBean>? = null
        var company_info: List<CompanyInfoBean>? = null
        var genre_info: List<GenreInfoBean>? = null
        var type_info: List<TypeInfoBean>? = null
        var year_info: List<YearInfoBean>? = null

        class AlbumListBean {
            var album: AlbumBean? = null
            var author: List<AuthorBean>? = null

            class AlbumBean {
                var id: Int = 0
                var mid: String? = null
                var name: String? = null
                var subtitle: String? = null
                var time_public: String? = null
                var title: String? = null
            }

            class AuthorBean {

                var id: Int = 0
                var mid: String? = null
                var name: String? = null
                var title: String? = null
                var type: Int = 0
                var uin: Int = 0
            }
        }

        class CategoryInfoBean {

            var id: Int = 0
            var title: String? = null
        }

        class CompanyInfoBean {

            var id: Int = 0
            var title: String? = null
        }

        class GenreInfoBean {

            var id: Int = 0
            var title: String? = null
        }

        class TypeInfoBean {

            var id: Int = 0
            var report: String? = null
            var title: String? = null
        }

        class YearInfoBean {

            var id: Int = 0
            var title: String? = null
        }
    }
}

class NewAlbum : Serializable {
    var data: DataBean? = null
    var code: Int = 0

    class DataBean {
        var size: Int = 0
        var type: Int = 0
        var song_list: List<SongListBean>? = null
        var type_info: List<TypeInfoBean>? = null

        class SongListBean {
            var action: ActionBean? = null
            var album: AlbumBean? = null
            var bpm: Int = 0
            var data_type: Int = 0
            var file: FileBean? = null
            var fnote: Int = 0
            var genre: Int = 0
            var id: Int = 0
            var index_album: Int = 0
            var index_cd: Int = 0
            var interval: Int = 0
            var isonly: Int = 0
            var ksong: KsongBean? = null
            var label: String? = null
            var language: Int = 0
            var mid: String? = null
            var modify_stamp: Int = 0
            var mv: MvBean? = null
            var name: String? = null
            var pay: PayBean? = null
            var status: Int = 0
            var subtitle: String? = null
            var time_public: String? = null
            var title: String? = null
            var trace: String? = null
            var type: Int = 0
            var url: String? = null
            var version: Int = 0
            var volume: VolumeBean? = null
            var singer: List<SingerBean>? = null

            class ActionBean {
                var alert: Int = 0
                var icons: Int = 0
                var msgdown: Int = 0
                var msgfav: Int = 0
                var msgid: Int = 0
                var msgpay: Int = 0
                var msgshare: Int = 0
                @SerializedName("switch")
                var switchX: Int = 0
            }

            class AlbumBean {

                var id: Int = 0
                var mid: String? = null
                var name: String? = null
                var subtitle: String? = null
                var time_public: String? = null
                var title: String? = null
            }

            class FileBean {

                var media_mid: String? = null
                var size_128mp3: Int = 0
                var size_192aac: Int = 0
                var size_192ogg: Int = 0
                var size_24aac: Int = 0
                var size_320mp3: Int = 0
                var size_48aac: Int = 0
                var size_96aac: Int = 0
                var size_ape: Int = 0
                var size_dts: Int = 0
                var size_flac: Int = 0
                var size_try: Int = 0
                var try_begin: Int = 0
                var try_end: Int = 0
            }

            class KsongBean {
                var id: Int = 0
                var mid: String? = null
            }

            class MvBean {
                var id: Int = 0
                var name: String? = null
                var title: String? = null
                var vid: String? = null
            }

            class PayBean {
                var pay_down: Int = 0
                var pay_month: Int = 0
                var pay_play: Int = 0
                var pay_status: Int = 0
                var price_album: Int = 0
                var price_track: Int = 0
                var time_free: Int = 0
            }

            class VolumeBean {
                var gain: Double = 0.toDouble()
                var lra: Double = 0.toDouble()
                var peak: Double = 0.toDouble()
            }

            class SingerBean {

                var id: Int = 0
                var mid: String? = null
                var name: String? = null
                var title: String? = null
                var type: Int = 0
                var uin: Int = 0
            }
        }

        class TypeInfoBean {
            var id: Int = 0
            var report: String? = null
            var title: String? = null
        }
    }
}