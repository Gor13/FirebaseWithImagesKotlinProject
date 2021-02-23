package com.hardzei.firebasewithimageskotlinproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.section_item.view.*

class SectionsListAdapter(context: Context?) :
        RecyclerView.Adapter<SectionsListAdapter.SectionViewHolder>() {

    private val context = context
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var sections: List<Section> = listOf()

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
            nameOfSectionTextureView.text = sections[position].nameOfSection
            if (context == null) return
            val locationsAdapter: ArrayAdapter<Location> = ArrayAdapter(context, android.R.layout.simple_list_item_1, sections[position].listWithLocations)
            locationsListView.adapter = locationsAdapter
        }
    }

    override fun getItemCount(): Int {
        return sections.size
    }

    class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameOfSectionTextureView = itemView.nameOfSectionTV
       // val nameOfLocationTextureView = itemView.nameOflocationTV
        val locationsListView = itemView.locationsListView
    }
}