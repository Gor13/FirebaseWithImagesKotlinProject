package com.hardzei.firebasewithimageskotlinproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.hardzei.firebasewithimageskotlinproject.adapters.SectionsListAdapter
import kotlinx.android.synthetic.main.activity_locations.*

private const val TAG = "TEST"
private const val RC_GET_IMAGE = 100

class LocationsActivity : AppCompatActivity() {


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locations)

        secionsRecyclerView = sectionsRV
        sectionsListAdapter = SectionsListAdapter(this)
        secionsRecyclerView.adapter = sectionsListAdapter


        getSectionsFromFDB()
        initListeners()
    }

    private fun initListeners() {
        addSectionFAB.setOnClickListener {
            addNewSection()
        }
        sectionsListAdapter.addNewLocationButtonClickListener = object : SectionsListAdapter.AddNewLocationButtonClickListener {
            override fun addNewLocationButtonClick(section: Section) {
                //Log.d(TAG, "${section}")
                addNewLocationIntoSectionIntoFDB(section)
            }
        }
        sectionsListAdapter.addNewImageButtonClickListener = object : SectionsListAdapter.AddNewImageButtonClickListener {
            override fun addNewImageButtonClick(section: Section, numberOfLocation: Int) {
                //Log.d(TAG, "${section}   $numberOfLocation")
                sectionWithAddableImage = section
                numberOfLocationWithAddableImage = numberOfLocation
                getImageFromInternal()
            }
        }
        sectionsListAdapter.deleteImagesButtonClickListener = object : SectionsListAdapter.DeleteImagesButtonClickListener {
            override fun deleteImagesButtonClick(section: Section, numberOfLocation: Int, listWithNumbersOfImages: List<Int>) {
                Log.d(TAG, "${section}   $numberOfLocation   $listWithNumbersOfImages")
                deleteSelectedImagesFromFDB(section, numberOfLocation, listWithNumbersOfImages)
            }

        }
    }

    private fun deleteSelectedImagesFromFDB(section: Section, numberOfLocation: Int, listWithNumbersOfImages: List<Int>) {
        val currentRef = db.collection("sections").document(section.id)
        currentRef
                .update("id", section.id,
                        "listWithLocations", section.listWithLocations.deleteSelectedImages(numberOfLocation, listWithNumbersOfImages))
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully updated!")
                    getSectionsFromFDB()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }

    private fun addNewPhotoIntoLocationIntoFDB(section: Section, numberOfLocation: Int) {

        val currentRef = db.collection("sections").document(section.id)
        currentRef
                .update("id", section.id,
                        "listWithLocations", section.listWithLocations.addnewImage(numberOfLocation, referenceWithCurentImage))
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully updated!")
                    getSectionsFromFDB()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }

    private fun addNewLocationIntoSectionIntoFDB(section: Section) {
        val currentRef = db.collection("sections").document(section.id)
        currentRef
                .update("id", section.id, "listWithLocations", section.listWithLocations.addnewLocation())
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully updated!")
                    getSectionsFromFDB()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GET_IMAGE && resultCode == RESULT_OK) {
            Toast.makeText(this, "request resived $resultCode", Toast.LENGTH_LONG).show()
            data?.let {
                Log.d(TAG, data.data.toString())
                writeImageIntoFireStor(data.data)
            }
        }
    }

    private fun writeImageIntoFireStor(uri: Uri?) {
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
                addNewPhotoIntoLocationIntoFDB(sectionWithAddableImage, numberOfLocationWithAddableImage)
            } else {
                // Handle failures
                // ...
                Log.d(TAG, "Dowloading uri not complete 100")
            }
        }

    }

    private fun getImageFromInternal() {
        startActivityForResult(
                Intent(Intent.ACTION_GET_CONTENT).setType("image/jpeg")
                        .putExtra(Intent.EXTRA_LOCAL_ONLY, true), RC_GET_IMAGE
        )
    }

    private fun getSectionsFromFDB() {
        db.collection("sections")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d(TAG, "${document.id} => ${document.data}}")

                    }
                    val listWithSections = result.map {
                        Section(it.id,
                                (it.data.values.toMutableList()[1] as String),
                                (it.data.values.toMutableList()[0] as ArrayList<*>).map { Location(((it as HashMap<*, *>).values.toMutableList()[0]).toString(), it.values.toMutableList()[1] as List<String>) }
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

    private fun addNewSection() {
        val newSection = Section("1/0", "Name of section", listOf())
        // Add a new document with a generated ID
        db.collection("sections")
                .add(newSection)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        getSectionsFromFDB()
    }
}

private fun List<Location>.deleteSelectedImages(numberOfLocation: Int, listWithNumbersOfImages: List<Int>): List<Location> {
    val listLocations = this.toMutableList()
    val listImages = this[numberOfLocation].listWithImages.toMutableList()
    for (number in listWithNumbersOfImages.sorted().reversed()){
        listImages.removeAt(number)
    }
    listLocations[numberOfLocation].listWithImages = listImages
    return listLocations.toList()
}

private fun List<Location>.addnewImage(numberOfLocation: Int, referenceWithCurentImage: String): List<Location> {
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
