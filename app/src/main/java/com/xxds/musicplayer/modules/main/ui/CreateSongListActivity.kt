package com.xxds.musicplayer.modules.main.ui

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import com.xxds.musicplayer.R
import com.xxds.musicplayer.common.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.mine_song_list_title_layout.*
import java.time.Year
import java.util.*

class CreateSongListActivity : ToolBarActivity() {

    companion object {

        val resultCode = generateLoaderId()
    }

    private lateinit var mBinding: com.xxds.musicplayer.databinding.ActivityCreateSongListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = resources.getString(R.string.create_new_song_list)
        mBinding.etListInfo.setText(R.string.create_new_song_list)
        mBinding.etListInfo.setSelectAllOnFocus(true)
        mBinding.etListInfo.requestFocus()
    }



    override fun createContentView(inflater: LayoutInflater, rootView: ViewGroup): View {

        mBinding = DataBindingUtil.inflate(inflater,R.layout.activity_create_song_list,rootView,false)
        return mBinding.root
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId) {

            R.id.menu_save -> save()

        }
        return  super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_save,menu)
        return true
    }
    private fun save() {

        contentResolver.insert(SONG_LIST_CONTENT_URI, ContentValues().apply {
            put(SL_ID, UUID.randomUUID().toString())
            put(SL_NAME, mBinding.etListName.text.toString())
            put(SL_CREATE_TIME, System.currentTimeMillis())
            put(SL_INFO, mBinding.etListInfo.text.toString())
        })
        setResult(resultCode)
        finish()
    }
}
