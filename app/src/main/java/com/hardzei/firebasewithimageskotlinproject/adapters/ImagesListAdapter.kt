package com.hardzei.firebasewithimageskotlinproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hardzei.firebasewithimageskotlinproject.R
import kotlinx.android.synthetic.main.image_item.view.*

class ImagesListAdapter(private val context: Context) : RecyclerView.Adapter<ImagesListAdapter.ImagesViewHolder>() {

    private var images: List<String> = listOf()
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var currentVisibility = View.GONE
    private val listWithNumbersOfImages = mutableListOf<Int>()

    fun setImages(images: List<String>) {
        this.images = images
        notifyDataSetChanged()
    }

    interface DeleteButtonOnClickListener {
        fun deleteButtonOnClick()
    }

    var deleteButtonOnClickListener: DeleteButtonOnClickListener? = null

    interface CheckBoxImageButtonOnClickListener {
        fun addImageIntoListOnClick(numberOfImage: Int)
        fun deleteImageFromListOnClick(numberOfImage: Int)
    }

    var checkBoxImageButtonOnClickListener: CheckBoxImageButtonOnClickListener? = null

    interface OnImageClickListener {
        fun onImageClick(url: String)
    }

    var onImageClickListener: OnImageClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val view = inflater.inflate(R.layout.image_item, parent, false)
        return ImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        with(holder) {
            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
            Glide
                .with(context)
                .load(images[position])
                .apply(options)
                .into(imageOfLocationIV)
            imageOfLocationIV.isLongClickable = true
            imageOfLocationIV.isClickable = true
            checkBoxImageButton.visibility = currentVisibility
            listWithNumbersOfImages.clear()

            imageOfLocationIV.setOnLongClickListener {
                currentVisibility = View.VISIBLE
                deleteButtonOnClickListener?.deleteButtonOnClick()
                notifyDataSetChanged()
                true
            }
            imageOfLocationIV.setOnClickListener {
                onImageClickListener?.onImageClick(images[position])
            }
            checkBoxImageButton.setOnClickListener {
                if (listWithNumbersOfImages.contains(position)) {
                    listWithNumbersOfImages.remove(position)
                    checkBoxImageButtonOnClickListener?.deleteImageFromListOnClick(position)
                    checkBoxImageButton.setImageResource(R.drawable.red_circle_outline_24)
                } else {
                    checkBoxImageButtonOnClickListener?.addImageIntoListOnClick(position)
                    listWithNumbersOfImages.add(position)
                    checkBoxImageButton.setImageResource(R.drawable.red_x_outline_24)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class ImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageOfLocationIV = itemView.imageOfLocationIV
        val checkBoxImageButton = itemView.checkBoxImageButton
    }
}
