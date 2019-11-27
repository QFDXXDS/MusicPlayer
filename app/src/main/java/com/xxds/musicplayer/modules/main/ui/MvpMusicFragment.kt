package com.xxds.musicplayer.modules.main.ui


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.loader.content.Loader
import com.xxds.ApplicationMain

import com.xxds.musicplayer.R
import com.xxds.musicplayer.common.utils.GlideImageLoader
import com.xxds.musicplayer.common.utils.ScrollEvent
import com.xxds.musicplayer.modules.bases.ui.BaseFragment
import com.xxds.musicplayer.modules.main.viewmodel.MainMusicViewModel
import com.youth.banner.Banner
import com.youth.banner.listener.OnBannerListener

import kotlinx.android.synthetic.main.fragment_mvp_music.*
import java.lang.IllegalStateException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MvpMusicFragment : BaseFragment(), OnBannerListener {

    val viewModel = MainMusicViewModel()
    val  recommendView = RecommendSongListBlock(ApplicationMain.instance)
    val  newSongView = NewSongExpressBlock(ApplicationMain.instance)


    private val images = ArrayList<String>()

    private val scrollEvent: ScrollEvent = ScrollEvent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.hallModel.observe(this, Observer {

            it.data?.slider?.forEach {

                images.add(it.picUrl!!)
            }

            initBanner()
        })

        viewModel.recommendList.observe(this, Observer {

            recommendView.dataChange(it!!)
        })


        viewModel.expressInfoModel.observe(this, Observer {

            newSongView.dataChanged(it)
        })



    }

    private fun initBanner() {
        banner.run {
            setImageLoader(GlideImageLoader())
            setImages(images)
            setOnBannerListener(this@MvpMusicFragment)
            start()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mvp_music, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scrollView.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->

            scrollEvent.dy = scrollY.toFloat()
        }
        banner.background

        replace(online_recommend,recommendView)
        replace(online_express,newSongView)
    }

    override fun OnBannerClick(position: Int) {

        position
    }




    }
