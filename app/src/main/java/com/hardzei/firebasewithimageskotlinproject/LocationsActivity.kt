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
import kotlinx.android.synthetic.main.activity_locations.*

private const val TAG = "TEST"
private const val RC_GET_IMAGE = 100

class LocationsActivity : AppCompatActivity() {


    // Access a Cloud Firestore instance from your Activity
    val db = Firebase.firestore
    val storage = Firebase.storage
    private lateinit var sectionsListAdapter: SectionsListAdapter
    private lateinit var secionsRecyclerView: RecyclerView

    // Create a storage reference from our app
    var storageRef = storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locations)

        secionsRecyclerView = sectionsRV
        sectionsListAdapter = SectionsListAdapter(this)
        secionsRecyclerView.adapter = sectionsListAdapter

        // addUser2()
        getSectionsFromFDB()

        // getPhotos()
        initListeners()
    }

    private fun initListeners() {
        addSectionFAB.setOnClickListener {
            addNewSection()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GET_IMAGE && resultCode == RESULT_OK) {
            Toast.makeText(this, "request resived $resultCode", Toast.LENGTH_LONG).show()
            data?.let {
                Log.d(TAG, data.data.toString())
                writeIntoFireStor(data.data)
            }
        }
    }

    private fun writeIntoFireStor(uri: Uri?) {

        var file = uri
        val riversRef = storageRef.child("images/${file?.lastPathSegment}")
        var uploadTask = riversRef.putFile(file!!)

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
            } else {
                // Handle failures
                // ...
            }
        }

    }

    private fun getPhotos() {
        startActivityForResult(
                Intent(Intent.ACTION_GET_CONTENT).setType("image/jpeg")
                        .putExtra(Intent.EXTRA_LOCAL_ONLY, true), RC_GET_IMAGE
        )
    }

    private fun getSectionsFromFDB() {
        db.collection("sections")
                .get()
                .addOnSuccessListener { result ->
                    var list: MutableList<Section> = mutableListOf()
                    for (document in result) {
                        Log.d(TAG, "${document.id} => ${document.data}}")
//                    var b = document.data.values.toMutableList()
//                    list.add(Section(document.id, b[1] as String))
                    }
                    val locations = result.documents.map { (it.data!!.values.toMutableList()[0] as ArrayList<*>).map { Location(((it as HashMap<*, *>).values.toMutableList()[0]).toString(), listOf()) } }
                    Log.d("TEST", locations.toString())
                    val listWithSections = result.map {
                        Section(it.id,
                                // j03G54IbalNrjc1zmDqF =>  - this is our id in firebase
                                // {listWithLocations=[{nameOfLication=Name oftion, listWithImages=[]}], - this is our list with locations
                                // nameOfSection=Name of section1,
                                // id=-1}} - this is our id into object Section
                                (it.data.values.toMutableList()[1] as String),
//                                listOf(Location(((it.data.values.toMutableList()[0]
//                                        as ArrayList<*>)[0]
//                                        as HashMap<*, *>)
//                                        .values.toMutableList()[0].toString(),
//                                        listOf()))
                                (it.data.values.toMutableList()[0] as ArrayList<*>).map { Location(((it as HashMap<*, *>).values.toMutableList()[0]).toString(), listOf()) }
                                //    (it.data.values.toMutableList() as ArrayList<*>).map { (it as HashMap<*,*>).values.toMutableList().map { Location(it.toString(), listOf())  } }[0]
                        )
                        // {listWithLocations=[{nameOfLication=Name oftion,
                        // listWithImages=[]}],
                        // nameOfSection=Name of section1,
                        // id=-1}}
                    }
                    sectionsListAdapter.setSections(listWithSections)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
    }

    private fun addUser2() {

        val user = hashMapOf(
                "first" to "Alan",
                "middle" to "Mathison",
                "last" to "Turing",
                "born" to 1912
        )

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
    }

    private fun addNewSection() {

        val newSection = Section("1/0", "Name of section", listOf(Location("Name of Location1", listOf()),
                Location("Name of Location2", listOf()),
                Location("Name of Location3", listOf()),
                Location("Name of Location4", listOf())))
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
