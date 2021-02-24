package com.hardzei.firebasewithimageskotlinproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hardzei.firebasewithimageskotlinproject.Location
import com.hardzei.firebasewithimageskotlinproject.R
import kotlinx.android.synthetic.main.image_item.view.*

class ImagesListAdapter(context: Context): RecyclerView.Adapter<ImagesListAdapter.ImagesViewHolder>() {

    private var images: List<String> = listOf()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    fun setImages(images: List<String>) {
        this.images = images
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val view = inflater.inflate(R.layout.image_item, parent, false)
        return ImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.imageOfLocationIV.setImageResource(R.drawable.default_image)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class ImagesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageOfLocationIV = itemView.imageOfLocationIV
    }
}