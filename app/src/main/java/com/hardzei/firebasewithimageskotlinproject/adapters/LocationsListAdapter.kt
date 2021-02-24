package com.hardzei.firebasewithimageskotlinproject.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hardzei.firebasewithimageskotlinproject.Location
import com.hardzei.firebasewithimageskotlinproject.R
import kotlinx.android.synthetic.main.location_item.view.*

class LocationsListAdapter(private val context: Context): RecyclerView.Adapter<LocationsListAdapter.LocationsViewHolder>() {

    private var locations: List<Location> = listOf()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    fun setLocations(locations: List<Location>) {
        this.locations = locations
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsViewHolder {
        val view = inflater.inflate(R.layout.location_item, parent, false)
        return LocationsViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) {
        with(holder){
            nameOflocationTV.text = locations[position].nameOfLication
            val imagesListAdapter = ImagesListAdapter(context)
            imagesRV.adapter = imagesListAdapter
            imagesListAdapter.setImages(locations[position].listWithImages)
        }
    }

    override fun getItemCount(): Int {
       return locations.size
    }
    class LocationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameOflocationTV = itemView.nameOflocationTV
        val imagesRV = itemView.imagesRV
    }
}