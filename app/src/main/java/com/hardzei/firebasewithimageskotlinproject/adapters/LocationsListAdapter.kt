package com.hardzei.firebasewithimageskotlinproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hardzei.firebasewithimageskotlinproject.Location
import com.hardzei.firebasewithimageskotlinproject.R
import com.hardzei.firebasewithimageskotlinproject.Section
import kotlinx.android.synthetic.main.location_item.view.*

class LocationsListAdapter(private val context: Context) : RecyclerView.Adapter<LocationsListAdapter.LocationsViewHolder>() {

    private var locations: List<Location> = listOf()
    private lateinit var section: Section
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val listWithNumbersOfImages = mutableListOf<Int>()

    interface AddNewImageButtonClickListener {
        fun addNewImageButtonClick(section: Section, numberOfLocation: Int)
    }

    var addNewImageButtonClickListener: AddNewImageButtonClickListener? = null

    interface DeleteImagesButtonClickListener {
        fun deleteImagesButtonClick(section: Section, numberOfLocation: Int, listWithNumbersOfImages: List<Int>)
    }

    var deleteImagesButtonClickListener: DeleteImagesButtonClickListener? = null

    fun setLocations(locations: List<Location>, section: Section) {
        this.section = section
        this.locations = locations
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsViewHolder {
        val view = inflater.inflate(R.layout.location_item, parent, false)
        return LocationsViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) {
        with(holder) {
            addNewImageButton.setOnClickListener {
                addNewImageButtonClickListener?.addNewImageButtonClick(section, position)
            }
            nameOflocationTV.text = locations[position].nameOfLication
            val imagesListAdapter = ImagesListAdapter(context)
            imagesRV.adapter = imagesListAdapter
            imagesListAdapter.setImages(locations[position].listWithImages)

            deleteImagesButton.setOnClickListener {
                deleteImagesButtonClickListener?.deleteImagesButtonClick(section, position, listWithNumbersOfImages.toList())
                deleteImagesButton.visibility = View.GONE
                imagesListAdapter.apply {
                    currentVisibility = View.GONE
                    notifyDataSetChanged()
                }
                listWithNumbersOfImages.clear()
            }

            imagesListAdapter.deleteButtonOnClickListener = object : ImagesListAdapter.DeleteButtonOnClickListener {
                override fun deleteButtonOnClick() {
                    deleteImagesButton.visibility = View.VISIBLE
                }
            }
            imagesListAdapter.checkBoxImageButtonOnClickListener = object : ImagesListAdapter.CheckBoxImageButtonOnClickListener {
                override fun addImageIntoListOnClick(numberOfImage: Int) {
                    listWithNumbersOfImages.add(numberOfImage)
                }

                override fun deleteImageFromListOnClick(numberOfImage: Int) {
                    listWithNumbersOfImages.remove(numberOfImage)
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    class LocationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameOflocationTV = itemView.nameOflocationTV
        val imagesRV = itemView.imagesRV
        val addNewImageButton = itemView.addNewImageButton
        val deleteImagesButton = itemView.deleteImagesButton
    }
}