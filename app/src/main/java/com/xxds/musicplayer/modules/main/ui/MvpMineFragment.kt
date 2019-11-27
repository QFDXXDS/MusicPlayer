package com.xxds.musicplayer.modules.main.ui

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xxds.ApplicationMain

import com.xxds.musicplayer.R
import com.xxds.musicplayer.common.utils.*
import com.xxds.musicplayer.modules.bases.ui.BaseFragment
import com.xxds.musicplayer.modules.entity.SongListEntity
import com.xxds.musicplayer.modules.main.adapter.MineAdapter
import com.xxds.musicplayer.modules.main.ui.fragments.LovedSongFragment
import kotlinx.android.synthetic.main.fragment_mvp_mine.*
import kotlinx.android.synthetic.main.mainitemview.*
import kotlinx.android.synthetic.main.mine_header_view_layout.*
import kotlinx.android.synthetic.main.mine_song_list_title_layout.*
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent
import java.util.zip.Inflater


class MvpMineFragment : BaseFragment(),View.OnClickListener {
//    private var listener: OnFragmentInteractionListener? = null

    companion object {

        const val preFragmentTag = MainFragment.TAG
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MineAdapter

    private lateinit var personalInfoView : View

    private lateinit var  operationView: GridLayout

    private  lateinit var songListTitleView: View

    private  val mineLoader = MineLoader()

    val metrics = ApplicationMain.instance.resources.displayMetrics


    private val list = mutableListOf<SongListEntity>()

    init {

//        初始化时context为空
        val con = context

        print("")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//     context已被赋值，context当前的activity
        val con = context

        print("")
        val rootView = inflater.inflate(R.layout.fragment_mvp_mine, container, false)

        // Inflate the layout for this fragment
//        mine_recycler_view.layoutManager = LinearLayoutManager(ApplicationMain.instance)
//        mine_recycler_view.adapter = MineAdapter(ApplicationMain.instance!!, list)

        recyclerView = rootView.findViewById(R.id.mine_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

//        list.add(SongListEntity("123","11","123"))
        adapter = MineAdapter(context!!, list)
        recyclerView.adapter = adapter

        operationView = LayoutInflater.from(getContext()).inflate(R.layout.mine_func_layout, null) as GridLayout as GridLayout
        operationView.columnCount = 3
        operationView.rowCount = 2

        addViewToGridLayout(operationView)

        val dataList = listOf(DataHolder(getContext()!!.resources.getString(R.string.mine_local_music), R.drawable.ic_my_music_local_song, R.id.local_music), DataHolder(getContext()!!.resources.getString(R.string.mine_down_music), R.drawable.ic_my_music_download_song, R.id.download_music), DataHolder(getContext()!!.resources.getString(R.string.mine_recent_music), R.drawable.ic_my_music_recent_playlist, R.id.recent_music), DataHolder(getContext()!!.resources.getString(R.string.mine_love_music), R.drawable.ic_my_music_my_favorite, R.id.love_music), DataHolder(getContext()!!.resources.getString(R.string.mine_buy_music), R.drawable.ic_my_music_paid_songs, R.id.buy_music), DataHolder(getContext()!!.resources.getString(R.string.mine_running_radio), R.drawable.ic_my_music_running_radio, R.id.running_radio))
        initView(dataList)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val personal_info: View = adapter.header.findViewById(R.id.mine_personal_info)
        val fun_area: View = adapter.header.findViewById(R.id.mine_fun_area)
        val song_list_title: View = adapter.header.findViewById(R.id.mine_song_list_title)


        personalInfoView = LayoutInflater.from(context).inflate(R.layout.mine_head_layout, null)
        songListTitleView = LayoutInflater.from(context).inflate(R.layout.mine_song_list_title_layout, null)

        replace(personal_info,personalInfoView)
        replace(fun_area,operationView)
        replace(song_list_title,songListTitleView)

        var create_new_song_list = songListTitleView.findViewById<LinearLayout>(R.id.ll_create_new_song_list)
        create_new_song_list.setOnClickListener(this)


        initData()
    }

    private fun addViewToGridLayout(container: ViewGroup) {

        for (i in 0..5) {

            val itemView = LayoutInflater.from(context).inflate(R.layout.view_mine_item_view,container,false)
            val params = itemView.layoutParams as GridLayout.LayoutParams

            params.width = metrics.widthPixels / 3
            itemView.layoutParams = params
            operationView.addView(itemView)
        }



    }

    private fun initView(list: List<DataHolder>) {

        list.forEachIndexed { index, dataHolder ->

            val itemView = operationView.getChildAt(index)
            val imageView: ImageView = itemView.findViewById(R.id.img_item)
            val itemName: TextView = itemView.findViewById(R.id.tv_item_name)

            imageView.id = dataHolder.id
            imageView.setOnClickListener(this)
            itemName.text = dataHolder.itemName
//            imageView.setImageResource(dataHolder.drawablePic)
//            imageView.setImageDrawable(ContextCompat.getDrawable(context!!,dataHolder.drawablePic))
            imageView.setImageDrawable(activity!!.getDrawable(dataHolder.drawablePic))

        }
    }


    fun initData() {


        LoaderManager.getInstance(activity!!).initLoader(QUERY_SONG_LIST,null,mineLoader)
    }
    fun clickCreated() {

    }
    fun clickCollected() {


    }

    fun createNewSongList(){

        val intent = Intent(getContext(), CreateSongListActivity::class.java)
        getActivity()!!.startActivityForResult(intent, 0)
    }


    override fun onClick(p0: View?) {


        when(p0!!.id) {
            R.id.tv_title_created ->  clickCreated()
            R.id.tv_title_collected -> clickCollected()

            R.id.ll_create_new_song_list  -> createNewSongList()
            R.id.add_song_list -> createNewSongList()


            R.id.local_music -> localMusic()
            R.id.download_music -> downloadMusic()
            R.id.recent_music -> recentMusic()
            R.id.love_music -> loveMusic()
            R.id.running_radio -> runningRadio()
            R.id.buy_music -> buyMusic()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == CreateSongListActivity.resultCode) {


        }
    }


    private fun localMusic() {
        print("")
//        startFragment(this, LocalMusicContainerFragment.newInstance(0), preFragmentTag)
    }

    private fun downloadMusic() {
//        startFragment(this, DownLoadFragment.newInstance(), preFragmentTag)
    }

    private fun recentMusic() {
//        startFragment(this, RecentMusicFragment.newInstance(), preFragmentTag)
    }

    private fun loveMusic() {
        startFragment(this, LovedSongFragment.newInstance(), preFragmentTag)
}

    private fun runningRadio() {
//        startFragment(this, LocalMusicContainerFragment.newInstance(0), preFragmentTag)
    }

    private fun buyMusic() {
        Toast.makeText(getContext(), "测试", Toast.LENGTH_SHORT).show()
    }

    class DataHolder(
        var itemName: String,
        var drawablePic: Int,
        var id: Int
    )

   inner class MineLoader: LoaderManager.LoaderCallbacks<Cursor>{

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {

            return CursorLoader(ApplicationMain.instance, SONG_LIST_CONTENT_URI,null,null,null,null)
        }

        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {

            data?.let {

                list.clear()
                data.moveToFirst()
                while (!data.isAfterLast) {

                    val id = data.getString(data.getColumnIndex(SL_ID))
                    val name = data.getString(data.getColumnIndex(SL_NAME))
                    val info = data.getString(data.getColumnIndex(SL_INFO))

                    val creatTime  = data.getString(data.getColumnIndex(SL_CREATE_TIME))
                    val e = SongListEntity(id,name,creatTime)
                    e.info = info
                    list.add(e)
                    data.moveToNext()
                }

                adapter.notifyDataSetChanged()
            }

        }

        override fun onLoaderReset(loader: Loader<Cursor>) {

        }




    }


}
