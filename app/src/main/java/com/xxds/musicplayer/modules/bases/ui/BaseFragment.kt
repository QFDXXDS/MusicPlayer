package com.xxds.musicplayer.modules.bases.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import com.xxds.cjs.modules.bases.BaseActivity
import com.xxds.musicplayer.common.listioner.MusicStateListener
import com.xxds.musicplayer.modules.main.ui.MvpMusicFragment
import java.lang.IllegalStateException

open class BaseFragment: Fragment(), MusicStateListener {

    lateinit var mLoaderManager: LoaderManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mLoaderManager = LoaderManager.getInstance(this)

    }

    override fun onResume() {
        super.onResume()

        (activity as BaseActivity).setMusicStateListenerListener(this)
    }
    override fun onStop() {
        super.onStop()
    }

    override fun musciChanged() {

    }

    override fun reloadAdapter() {

    }

    override fun updateLrc() {

    }

    override fun updatePlayInfo() {

    }

    override fun updateTime() {

    }
    protected fun replace(target: View?, source: View?) {

        if (target == null) {
            return
        }
        if (source == null) {
            return
        }
        var parent = target.parent

        if (parent != null && parent is ViewGroup) {

            val index = parent.indexOfChild(target)
            parent.removeViewInLayout(target)
            source.id = target.id
            val layoutParams = target.layoutParams
            if (layoutParams != null) {
                parent.addView(source,index,layoutParams)

            } else {

                parent.addView(source,index)
            }


        } else {

            throw IllegalStateException("ViewStub must have a non-null ViewGroup viewParent")
        }
    }

}