package com.hardzei.firebasewithimageskotlinproject.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import androidx.recyclerview.widget.RecyclerView
import com.hardzei.firebasewithimageskotlinproject.R
import com.hardzei.firebasewithimageskotlinproject.pojo.Section
import kotlinx.android.synthetic.main.section_item.view.*

class SectionsListAdapter(private val context: Context) :
    RecyclerView.Adapter<SectionsListAdapter.SectionViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var sections: List<Section> = listOf()
    private lateinit var locationsListAdapter: LocationsListAdapter

    interface AddNewLocationButtonClickListener {
        fun addNewLocationButtonClick(section: Section)
    }

    var addNewLocationButtonClickListener: AddNewLocationButtonClickListener? = null

    interface AddNewImageButtonClickListener {
        fun addNewImageButtonClick(section: Section, numberOfLocation: Int)
    }

    var addNewImageButtonClickListener: AddNewImageButtonClickListener? = null

    interface DeleteImagesButtonClickListener {
        fun deleteImagesButtonClick(
            section: Section,
            numberOfLocation: Int,
            listWithNumbersOfImages: List<Int>
        )
    }

    var deleteImagesButtonClickListener: DeleteImagesButtonClickListener? = null

    interface ChangeNameOfSectionClickListener {
        fun nameChangeButtonClick(section: Section, changedText: String)
    }

    var changeNameOfSectionClickListener: ChangeNameOfSectionClickListener? = null

    interface ChangeNameOfLocationClickListener {
        fun nameChangedButtonClick(section: Section, numberOfLocation: Int, changedText: String)
    }

    var changeNameOfLocationClickListener: ChangeNameOfLocationClickListener? = null

    interface OnImageClickListener {
        fun onImageClick(url: String)
    }

    var onImageClickListener: OnImageClickListener? = null

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

            nameOfSectionET.setText(sections[position].nameOfSection)
            nameOfSectionET.setOnEditorActionListener(
                OnEditorActionListener { v, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        changeNameOfSectionClickListener?.nameChangeButtonClick(
                            sections[position],
                            nameOfSectionET.text.toString()
                        )
                        nameOfSectionET.clearFocus()
                        val imm: InputMethodManager =
                            v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.windowToken, 0)
                        return@OnEditorActionListener true
                    }
                    false
                }
            )
            locationsListAdapter = LocationsListAdapter(context)
            locationsRV.adapter = locationsListAdapter
            locationsListAdapter.setLocations(
                sections[position].listWithLocations,
                sections[position]
            )
            locationsListAdapter.addNewImageButtonClickListener =
                object : LocationsListAdapter.AddNewImageButtonClickListener {
                    override fun addNewImageButtonClick(section: Section, numberOfLocation: Int) {
                        addNewImageButtonClickListener?.addNewImageButtonClick(
                            section,
                            numberOfLocation
                        )
                    }
                }
            locationsListAdapter.deleteImagesButtonClickListener =
                object : LocationsListAdapter.DeleteImagesButtonClickListener {
                    override fun deleteImagesButtonClick(
                        section: Section,
                        numberOfLocation: Int,
                        listWithNumbersOfImages: List<Int>
                    ) {
                        deleteImagesButtonClickListener?.deleteImagesButtonClick(
                            section,
                            numberOfLocation,
                            listWithNumbersOfImages
                        )
                    }
                }
            locationsListAdapter.changeNameOfLocationClickListener =
                object : LocationsListAdapter.ChangeNameOfLocationClickListener {
                    override fun nameChangedButtonClick(
                        section: Section,
                        numberOfLocation: Int,
                        changedText: String
                    ) {
                        changeNameOfLocationClickListener?.nameChangedButtonClick(
                            section,
                            numberOfLocation,
                            changedText
                        )
                    }
                }
            locationsListAdapter.onImageClickListener =
                object : LocationsListAdapter.OnImageClickListener {
                    override fun onImageClick(url: String) {
                        onImageClickListener?.onImageClick(url)
                    }
                }
        }
    }

    override fun getItemCount(): Int {
        return sections.size
    }

    class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameOfSectionET = itemView.nameOfSectionET
        val locationsRV = itemView.locationsRV
        val addNewLocationButton = itemView.addNewLocationButton
    }
}
