package com.xxds.musicplayer.modules.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.xxds.musicplayer.R
import com.xxds.musicplayer.modules.main.models.MainDataModel
import org.jetbrains.anko.image
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainAdapter(val data: ArrayList<Any>): RecyclerView.Adapter<MainViewHolder>() {


    lateinit var onItemClickListener: (Int) -> Unit

    override fun getItemCount(): Int {

        return data.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        var model = data[position] as MainDataModel
        holder.updateView(model)

        holder.constraintLayout.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        var contentView = LayoutInflater.from(parent.context).inflate(R.layout.mainitemview,parent,false)
        return  MainViewHolder(contentView)
    }

//    override fun getItemViewType(position: Int): Int {
//
//
//    }
}

class MainViewHolder(var contentView: View): RecyclerView.ViewHolder(contentView) {


    val bt: ImageButton = contentView.findViewById<ImageButton>(R.id.imageButton2)
    val imgView: ImageView = contentView.findViewById<ImageView>(R.id.imageView2)

    val constraintLayout = contentView.findViewById<ConstraintLayout>(R.id.itemview)
    init {
        constraintLayout.setOnClickListener {

            it.isSelected = !it.isSelected
            bt.isSelected = it.isSelected

        }
       bt.isEnabled = false
//        imgView.isEnabled = false
    }
    fun updateView(model: MainDataModel) {

        contentView.findViewById<TextView>(R.id.songName).text = model.title
        contentView.findViewById<TextView>(R.id.singer).text = model.artist_name
//        var avater =  contentView.findViewById<ImageView>(R.id.imageView2)
//        Glide.with(contentView).load(model.pic_big).into(avater)


//        contentView.findViewById<TextView>(R.id.songName).text = model.title

    }

}

