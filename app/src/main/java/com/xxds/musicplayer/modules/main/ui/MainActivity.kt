package com.xxds.musicplayer.modules.main.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Rect
import android.media.tv.TvContract.Programs.Genres.MUSIC
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.xxds.cjs.modules.bases.BaseActivity
import com.xxds.musicplayer.R
import com.xxds.musicplayer.common.utils.lacksPermissions
import com.xxds.musicplayer.modules.main.adapter.MainAdapter
import com.xxds.musicplayer.modules.main.models.MainDataModel
import com.xxds.musicplayer.modules.main.viewmodel.MainViewModel
import com.xxds.musicplayer.modules.play.play.Play
import com.xxds.musicplayer.modules.service.MusicPlayer

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_app_main.*

const val MINE = 0
const val MUSIC1 = 1
const val FIND = 2

class MainActivity : BaseActivity() {

    companion object {
        const val PERMISSION_REQUEST_CODE = 0x00
        const val PACKAGE_URL_SCHEME = "package:"
        val permissions = Array(1) { Manifest.permission.READ_EXTERNAL_STORAGE }
    }

    val viewMode = MainViewModel(1,10)
    val play = Play()
    val mainFragment = MainFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
//        initToolbar()
        initView()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        showQuickControl(true)

    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()

//        checkPermission()

    }
    fun checkPermission() {

        if (lacksPermissions(this, permissions)) {

            requestPermissions(permissions, PERMISSION_REQUEST_CODE)
        } else {


        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == PERMISSION_REQUEST_CODE ) {


        } else {

            checkPermission()
        }

    }

//    private fun hasAllPermissionsGranted(grantResults: IntArray): Boolean {
//
//        return grantResults.contains(PackageManager.PERMISSION_GRANTED)
//    }


        fun  play(model: MainDataModel) {

//        play.play(model.song_id)

    }
    fun initView() {

//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.addItemDecoration(object : DividerItemDecoration(this,DividerItemDecoration.VERTICAL){
//
//            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//                super.getItemOffsets(outRect, view, parent, state)
//
//            if (parent.getChildAdapterPosition(view) != 0) {
//
//                outRect.top = 1 ;
//                outRect.left = 8 ;
//            }
//        }
//        })

        supportFragmentManager.beginTransaction().run {

            add(R.id.se_main_content,mainFragment,MainFragment.TAG)
            commit()
        }


    }


//    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
//        super.onCreateContextMenu(menu, v, menuInfo)
//
//        menuInflater.inflate(R.menu.menu_main, menu)
//
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
//
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

//  一般提供双击验证机制
    override fun onBackPressed() {

//    点击退出当前进程
//       super.onBackPressed()

//     进入后台
         moveTaskToBack(true)


    }


    private fun initToolbar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
        }
//        home_tool_bar.setTitle(R.string.app_name)
//        home_tool_bar.setOnMenuItemClickListener(this)
    }


}
