package com.hardzei.firebasewithimageskotlinproject.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.hardzei.firebasewithimageskotlinproject.Location
import com.hardzei.firebasewithimageskotlinproject.R
import com.hardzei.firebasewithimageskotlinproject.Section
import com.hardzei.firebasewithimageskotlinproject.adapters.SectionsListAdapter
import kotlinx.android.synthetic.main.fragment_locations.*

private const val TAG = "TEST"
private const val RC_GET_IMAGE = 100
private const val NAME_STACK_FOR_FRAGMENT = "NAME_STACK_FOR_FRAGMENT"

class LocationsFragment : Fragment() {

    // Access a Cloud Firestore instance from your Activity
    val db = Firebase.firestore
    val storage = Firebase.storage
    private lateinit var sectionsListAdapter: SectionsListAdapter
    private lateinit var secionsRecyclerView: RecyclerView
    private var referenceWithCurentImage = "default reference"

    private lateinit var sectionWithAddableImage: Section
    private var numberOfLocationWithAddableImage = -1

    // Create a storage reference from our app
    var storageRef = storage.reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_locations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        secionsRecyclerView = sectionsRV
        sectionsListAdapter = SectionsListAdapter(context)
        secionsRecyclerView.adapter = sectionsListAdapter

        getSectionsFromFDB()
        initListeners()
    }

    private fun initListeners() {
        addSectionFAB.setOnClickListener {
            addNewSectionInFDB()
        }
        sectionsListAdapter.addNewLocationButtonClickListener =
            object : SectionsListAdapter.AddNewLocationButtonClickListener {
                override fun addNewLocationButtonClick(section: Section) {
                    // Log.d(TAG, "${section}")
                    addNewLocationIntoSectionInFDB(section)
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
                    // Log.d(TAG, "${section}   $numberOfLocation   $listWithNumbersOfImages")
                    deleteSelectedImagesFromFDB(section, numberOfLocation, listWithNumbersOfImages)
                }
            }
        sectionsListAdapter.changeNameOfSectionClickListener =
            object : SectionsListAdapter.ChangeNameOfSectionClickListener {
                override fun nameChangeButtonClick(section: Section, changedText: String) {
                    changeSectionNameInFDB(section, changedText)
                }
            }
        sectionsListAdapter.changeNameOfLocationClickListener =
            object : SectionsListAdapter.ChangeNameOfLocationClickListener {
                override fun nameChangedButtonClick(
                    section: Section,
                    numberOfLocation: Int,
                    changedText: String
                ) {
                    changeLocationNameInFDB(section, numberOfLocation, changedText)
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

    private fun changeLocationNameInFDB(
        section: Section,
        numberOfLocation: Int,
        changedText: String
    ) {
        val currentRef = db.collection("sections").document(section.id)
        currentRef
            .update(
                "id",
                section.id,
                "listWithLocations",
                section.listWithLocations.changeLocationName(numberOfLocation, changedText)
            )
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully updated!")
                getSectionsFromFDB()
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document 2001", e) }
    }

    private fun changeSectionNameInFDB(section: Section, changedText: String) {
        val currentRef = db.collection("sections").document(section.id)
        currentRef
            .update(
                "id", section.id,
                "nameOfSection", changedText
            )
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully updated!")
                getSectionsFromFDB()
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document 2002", e) }
    }

    private fun deleteSelectedImagesFromFDB(
        section: Section,
        numberOfLocation: Int,
        listWithNumbersOfImages: List<Int>
    ) {
        val currentRef = db.collection("sections").document(section.id)
        currentRef
            .update(
                "id",
                section.id,
                "listWithLocations",
                section.listWithLocations.deleteSelectedImages(
                    numberOfLocation,
                    listWithNumbersOfImages
                )
            )
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully updated!")
                getSectionsFromFDB()
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document 2003", e) }
    }

    private fun addNewPhotoIntoLocationIntoFDB(section: Section, numberOfLocation: Int) {

        val currentRef = db.collection("sections").document(section.id)
        currentRef
            .update(
                "id",
                section.id,
                "listWithLocations",
                section.listWithLocations.addnewImage(numberOfLocation, referenceWithCurentImage)
            )
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully updated!")
                getSectionsFromFDB()
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document 2004", e) }
    }

    private fun addNewLocationIntoSectionInFDB(section: Section) {
        val currentRef = db.collection("sections").document(section.id)
        currentRef
            .update(
                "id",
                section.id,
                "listWithLocations",
                section.listWithLocations.addnewLocation()
            )
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully updated!")
                getSectionsFromFDB()
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document 2005", e) }
    }

    private fun writeImageInFDB(uri: Uri?) {
        val file = uri
        val riversRef = storageRef.child("images/${file?.lastPathSegment}")
        val uploadTask = riversRef.putFile(file!!)

        // Register observers to listen for when the download is done or if it fails
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    Log.d(TAG, "${it.message}")
                }
            }
            riversRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                Log.d(TAG, "$downloadUri")
                referenceWithCurentImage = downloadUri.toString()
                addNewPhotoIntoLocationIntoFDB(
                    sectionWithAddableImage,
                    numberOfLocationWithAddableImage
                )
            } else {
                Log.d(TAG, "Dowloading uri not complete 2006")
            }
        }
    }

    private fun getSectionsFromFDB() {
        db.collection("sections")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}}")
                }
                val listWithSections = result.map {
                    Section(
                        it.id,
                        (it.data.values.toMutableList()[1] as String),
                        (it.data.values.toMutableList()[0] as ArrayList<*>).map {
                            Location(
                                ((it as HashMap<*, *>).values.toMutableList()[0]).toString(),
                                it.values.toMutableList()[1] as List<String>
                            )
                        }
                    )
                }
                // j03G54IbalNrjc1zmDqF =>  - this is our id in firebase
                // {listWithLocations=[{nameOfLication=Name oftion, listWithImages=[]}], - this is our list with locations
                // nameOfSection=Name of section1,
                // id=-1}} - this is our id into object Section
                sectionsListAdapter.setSections(listWithSections)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    private fun addNewSectionInFDB() {
        val newSection = Section("1/0", "Name of section", listOf())
        // Add a new document with a generated ID
        db.collection("sections")
            .add(newSection)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document 2007", e)
            }
        getSectionsFromFDB()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GET_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
            Toast.makeText(context, "request resived $resultCode", Toast.LENGTH_LONG).show()
            data?.let {
                writeImageInFDB(data.data)
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

private fun List<Location>.changeLocationName(
    numberOfLocation: Int,
    changedText: String
): List<Location> {
    val listLocations = this.toMutableList()
    listLocations[numberOfLocation].nameOfLication = changedText
    return listLocations.toList()
}

private fun List<Location>.deleteSelectedImages(
    numberOfLocation: Int,
    listWithNumbersOfImages: List<Int>
): List<Location> {
    val listLocations = this.toMutableList()
    val listImages = this[numberOfLocation].listWithImages.toMutableList()
    for (number in listWithNumbersOfImages.sorted().reversed()) {
        listImages.removeAt(number)
    }
    listLocations[numberOfLocation].listWithImages = listImages
    return listLocations.toList()
}

private fun List<Location>.addnewImage(
    numberOfLocation: Int,
    referenceWithCurentImage: String
): List<Location> {
    val listLocations = this.toMutableList()
    val listImages = this[numberOfLocation].listWithImages.toMutableList()
    listImages.add(referenceWithCurentImage)
    listLocations[numberOfLocation].listWithImages = listImages
    return listLocations.toList()
}

private fun List<Location>.addnewLocation(): List<Location> {
    val list = this.toMutableList()
    list.add(Location("Name of location", listOf()))
    return list.toList()
}
