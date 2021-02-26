package com.hardzei.firebasewithimageskotlinproject.adapters

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hardzei.firebasewithimageskotlinproject.R
import com.hardzei.firebasewithimageskotlinproject.Section
import kotlinx.android.synthetic.main.section_item.view.*

class SectionsListAdapter(private val context: Context?) :
        RecyclerView.Adapter<SectionsListAdapter.SectionViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var sections: List<Section> = listOf()

    interface AddNewLocationButtonClickListener {
        fun addNewLocationButtonClick(section: Section)
    }

    var addNewLocationButtonClickListener: AddNewLocationButtonClickListener? = null

    interface AddNewImageButtonClickListener {
        fun addNewImageButtonClick(section: Section, numberOfLocation: Int)
    }

    var addNewImageButtonClickListener: AddNewImageButtonClickListener? = null

    interface DeleteImagesButtonClickListener {
        fun deleteImagesButtonClick(section: Section, numberOfLocation: Int, listWithNumbersOfImages: List<Int>)
    }

    var deleteImagesButtonClickListener: DeleteImagesButtonClickListener? = null

    fun setSections(sections: List<Section>) {
        this.sections = sections
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val view = inflater.inflate(R.layout.section_item, parent, false)
        return SectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {

        with(holder) {
            addNewLocationButton.setOnClickListener {
                addNewLocationButtonClickListener?.addNewLocationButtonClick(sections[position])
            }


            nameOfSectionTV.text = sections[position].nameOfSection
            if (context == null) return
            val locationsListAdapter = LocationsListAdapter(context)
            locationsRV.adapter = locationsListAdapter
            locationsListAdapter.setLocations(sections[position].listWithLocations, sections[position])
            locationsListAdapter.addNewImageButtonClickListener = object : LocationsListAdapter.AddNewImageButtonClickListener {
                override fun addNewImageButtonClick(section: Section, numberOfLocation: Int) {
                    addNewImageButtonClickListener?.addNewImageButtonClick(section, numberOfLocation)
                }
            }
            locationsListAdapter.deleteImagesButtonClickListener = object : LocationsListAdapter.DeleteImagesButtonClickListener {
                override fun deleteImagesButtonClick(section: Section, numberOfLocation: Int, listWithNumbersOfImages: List<Int>) {
                    deleteImagesButtonClickListener?.deleteImagesButtonClick(section, numberOfLocation, listWithNumbersOfImages)
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return sections.size
    }

    class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameOfSectionTV = itemView.nameOfSectionTV
        val locationsRV = itemView.locationsRV
        val addNewLocationButton = itemView.addNewLocationButton
    }
}