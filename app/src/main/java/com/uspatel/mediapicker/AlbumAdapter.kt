package com.uspatel.mediapicker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView


class AlbumAdapter(private val context: Context, private val onItemSelectionListener: (Long) -> Unit) : RecyclerView.Adapter<AlbumAdapter.MyHolder>() {

    private val list = ArrayList<Album>()
    private val requestOption = RequestOptions().override(150)

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.img)
        val tv: TextView = itemView.findViewById(R.id.tv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rv_album, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        Glide.with(context).asBitmap().apply(requestOption).load(list[position].imgPath)
            .placeholder(R.drawable.img_placeholder)
            .transition(withCrossFade(200))
            .into(holder.img)

        holder.tv.text = list[position].name
        holder.itemView.setOnClickListener {
            onItemSelectionListener(list[position].id)
        }
    }

    override fun getItemCount(): Int = list.size

    fun setImages(images: ArrayList<Album>) {
        list.clear()
        list.addAll(images)
        notifyDataSetChanged()
    }
}