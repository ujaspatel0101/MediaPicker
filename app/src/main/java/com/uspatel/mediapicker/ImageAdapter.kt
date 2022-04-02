package com.uspatel.mediapicker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView


class ImageAdapter(
    private val context: Context,
    private val onItemSelectionListener: (Int, List<String>) -> Unit,
    private val maxCount: Int
) : RecyclerView.Adapter<ImageAdapter.MyHolder>() {

    private val list = ArrayList<Media>()
    private val requestOption = RequestOptions().override(150)

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.img)
        val card: MaterialCardView = itemView.findViewById(R.id.cardView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rv_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        Glide.with(context).asBitmap().apply(requestOption).load(list[position].mimeType)
            .placeholder(R.drawable.img_placeholder)
            .transition(withCrossFade(200))
            .into(holder.img)

        holder.card.isChecked = list[position].isChecked


        holder.card.setOnClickListener {
            val selectedCount = list.count { i -> i.isChecked }
            if (selectedCount <= maxCount) {
                list[position].isChecked = !list[position].isChecked
                notifyItemChanged(position)
                var selectedImages: List<String> = ArrayList()
                list.forEach { item ->
                    if (item.isChecked) selectedImages += item.mimeType
                }
                onItemSelectionListener(list.count { i -> i.isChecked }, selectedImages)
            }else{
                Toast.makeText(context, "Maximum images has selected.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun setImages(images: java.util.ArrayList<Media>) {
        list.clear()
        list.addAll(images)
        notifyDataSetChanged()
    }
}