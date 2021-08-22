package com.hardzei.firebasewithimageskotlinproject.view.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.hardzei.firebasewithimageskotlinproject.InitApplication
import com.hardzei.firebasewithimageskotlinproject.MainContract
import com.hardzei.firebasewithimageskotlinproject.NAME_STACK_FOR_FRAGMENT
import com.hardzei.firebasewithimageskotlinproject.R
import com.hardzei.firebasewithimageskotlinproject.RC_GET_IMAGE
import com.hardzei.firebasewithimageskotlinproject.TAG
import com.hardzei.firebasewithimageskotlinproject.di.component.DaggerActivityComponent
import com.hardzei.firebasewithimageskotlinproject.di.module.VMModule
import com.hardzei.firebasewithimageskotlinproject.pojo.Section
import com.hardzei.firebasewithimageskotlinproject.utils.Create
import com.hardzei.firebasewithimageskotlinproject.utils.Delete
import com.hardzei.firebasewithimageskotlinproject.utils.Read
import com.hardzei.firebasewithimageskotlinproject.utils.Update
import com.hardzei.firebasewithimageskotlinproject.view.adapters.SectionsListAdapter
import com.hardzei.firebasewithimageskotlinproject.viewmodel.LocationsViewModel
import kotlinx.android.synthetic.main.fragment_locations.*
import javax.inject.Inject

class LocationsFragment : Fragment(), MainContract.ViewCallBack {

    private lateinit var sectionsListAdapter: SectionsListAdapter
    private lateinit var secionsRecyclerView: RecyclerView

    private lateinit var sectionWithAddableImage: Section
    private var numberOfLocationWithAddableImage = -1

    @Inject
    lateinit var locationsViewModel: LocationsViewModel

    @Inject
    lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        DaggerActivityComponent.builder()
            .appComponent(InitApplication().get(requireContext()).component())
            .vMModule(VMModule(this))
            .build()
            .inject(this)

        return inflater.inflate(R.layout.fragment_locations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        secionsRecyclerView = sectionsRV
        sectionsListAdapter = SectionsListAdapter(requireContext())
        secionsRecyclerView.adapter = sectionsListAdapter

        initObservers()
        initListeners()
        locationsViewModel.readListWithSections(Read())
    }

    private fun initObservers() {
        locationsViewModel.listWithSections.observe(
            viewLifecycleOwner,
            Observer {
                sectionsListAdapter.setSections(it)
            }
        )
        locationsViewModel.successMessage.observe(
            viewLifecycleOwner,
            Observer {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        )
        locationsViewModel.errorMessage.observe(
            viewLifecycleOwner,
            Observer {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun initListeners() {

        addSectionFAB.setOnClickListener {
            locationsViewModel.createNewSection(Create(null, null, null))
        }

        sectionsListAdapter.addNewLocationButtonClickListener =
            object : SectionsListAdapter.AddNewLocationButtonClickListener {
                override fun addNewLocationButtonClick(section: Section) {
                    locationsViewModel.createNewLocation(Create(section, null, null))
                }
            }
        sectionsListAdapter.addNewImageButtonClickListener =
            object : SectionsListAdapter.AddNewImageButtonClickListener {
                override fun addNewImageButtonClick(section: Section, numberOfLocation: Int) {
                    // Log.d(TAG, "${section}   $numberOfLocation")
                    sectionWithAddableImage = section
                    numberOfLocationWithAddableImage = numberOfLocation
                    getImageFromInternal()
                }
            }
        sectionsListAdapter.deleteImagesButtonClickListener =
            object : SectionsListAdapter.DeleteImagesButtonClickListener {
                override fun deleteImagesButtonClick(
                    section: Section,
                    numberOfLocation: Int,
                    listWithNumbersOfImages: List<Int>
                ) {
                    Log.d(TAG, "$section   $numberOfLocation   $listWithNumbersOfImages")
                    locationsViewModel.deletePhotos(
                        Delete(
                            section,
                            numberOfLocation,
                            listWithNumbersOfImages
                        )
                    )
                }
            }
        sectionsListAdapter.changeNameOfSectionClickListener =
            object : SectionsListAdapter.ChangeNameOfSectionClickListener {
                override fun nameChangeButtonClick(section: Section, changedText: String) {
                    locationsViewModel.updateSectionName(Update(section, null, changedText))
                }
            }
        sectionsListAdapter.changeNameOfLocationClickListener =
            object : SectionsListAdapter.ChangeNameOfLocationClickListener {
                override fun nameChangedButtonClick(
                    section: Section,
                    numberOfLocation: Int,
                    changedText: String
                ) {
                    locationsViewModel.updateLocationName(
                        Update(
                            section,
                            numberOfLocation,
                            changedText
                        )
                    )
                }
            }
        sectionsListAdapter.onImageClickListener =
            object : SectionsListAdapter.OnImageClickListener {
                override fun onImageClick(url: String) {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.animator.back_animator, R.animator.front_animator)
                        .replace(
                            R.id.activityLocationsFrameLayout,
                            FullScreenImageFragment.newInstance(url)
                        )
                        .addToBackStack(NAME_STACK_FOR_FRAGMENT)
                        .commit()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GET_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
            Toast.makeText(context, "request resived $resultCode", Toast.LENGTH_LONG).show()
            data?.let {
                locationsViewModel.createNewPhoto(
                    Create(
                        sectionWithAddableImage,
                        numberOfLocationWithAddableImage,
                        data.data
                    )
                )
            }
        }
    }

    private fun getImageFromInternal() {
        startActivityForResult(
            Intent(Intent.ACTION_GET_CONTENT).setType("image/jpeg")
                .putExtra(Intent.EXTRA_LOCAL_ONLY, true),
            RC_GET_IMAGE
        )
    }
}
