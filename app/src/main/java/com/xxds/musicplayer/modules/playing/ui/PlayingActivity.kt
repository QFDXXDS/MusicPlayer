package com.xxds.musicplayer.modules.playing.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.xxds.cjs.modules.bases.BaseActivity
import com.xxds.musicplayer.R
import com.xxds.musicplayer.common.utils.blurBitmap
import com.xxds.musicplayer.common.utils.getMegaImageUrl
import com.xxds.musicplayer.modules.playing.adapters.PlayerPagerAdapter
import com.xxds.musicplayer.modules.playing.others.PlayingAlbumPageTransformer
import com.xxds.musicplayer.modules.service.MusicPlayer
import kotlinx.android.synthetic.main.activity_playing.*

class PlayingActivity : BaseActivity(), ViewPager.OnPageChangeListener {


    private lateinit var activityBg: View
    private lateinit var toolbar: Toolbar

    private lateinit var songTitle: TextView
    private lateinit var viewPager: ViewPager
    private lateinit var multiButtonLayout: MultiButtonLayout
    private lateinit var playingBottomView: PlayingBottomView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_playing)
        showQuickControl(false)
        toolbarInit()
        initView()

    }

    fun initView(){

        activityBg = findViewById(R.id.player_activity_bg)
        songTitle = findViewById(R.id.playing_song_title)
        viewPager = findViewById(R.id.content_view_pager)
        multiButtonLayout = findViewById(R.id.playing_select_radio)
        playingBottomView = findViewById(R.id.playing_bottom_block)


        viewPager.run {

            adapter = PlayerPagerAdapter(supportFragmentManager)
            currentItem = PlayerPagerAdapter.ALBUM_INFO
            offscreenPageLimit = 2
            addOnPageChangeListener(this@PlayingActivity)
            setPageTransformer(false, PlayingAlbumPageTransformer())

        }

    }



    private fun toolbarInit() {
        toolbar = findViewById(R.id.playing_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    override fun onResume() {
        super.onResume()

        songTitle.text = MusicPlayer.getTrackName()
        playingBottomView.updateBlock()
        setBackground()
    }


    override fun musicChanged() {
        super.musicChanged()

        songTitle.text = MusicPlayer.getTrackName()
        playingBottomView.musicChanged()
        setBackground()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {


        menuInflater.inflate(R.menu.menu_setting, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_setting -> Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show()
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {

        multiButtonLayout.setSelectedChild(position)
    }

    override fun finish() {
        super.finish()

        overridePendingTransition(R.anim.push_up_in,R.anim.push_down_out)

    }

    private fun setBackground() {
        if (MusicPlayer.getAlbumPic().isEmpty()) {
            activityBg.background = getDrawable(R.drawable.player_background_real)
        } else {
            Glide.with(this)
                .asBitmap()
                .load(MusicPlayer.getAlbumPic().getMegaImageUrl())
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val blurBitmap = blurBitmap(resource, 100)
                        if (blurBitmap != null) {
                            activityBg.background = getAlphaDrawable(blurBitmap)
                        }
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        activityBg.background = getDrawable(R.drawable.player_background_real)
                    }
                })
        }
    }

    private fun getAlphaDrawable(bg: Bitmap): Drawable {

        val newBitmap = Bitmap.createBitmap(bg.copy(Bitmap.Config.ARGB_8888, true))
        val canvas = Canvas(newBitmap)
        val paint = Paint()
        paint.color = ContextCompat.getColor(this, R.color.hex_33000000)
        canvas.drawRect(0f, 0f, bg.width.toFloat(), bg.height.toFloat(), paint)
        canvas.save()
        canvas.restore()
        return BitmapDrawable(resources, newBitmap)
    }

}
