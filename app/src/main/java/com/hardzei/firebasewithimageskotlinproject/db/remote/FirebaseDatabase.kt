package com.hardzei.firebasewithimageskotlinproject.db.remote

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.hardzei.firebasewithimageskotlinproject.MainContract
import com.hardzei.firebasewithimageskotlinproject.TAG
import com.hardzei.firebasewithimageskotlinproject.db.Failed
import com.hardzei.firebasewithimageskotlinproject.db.Success
import com.hardzei.firebasewithimageskotlinproject.pojo.Location
import com.hardzei.firebasewithimageskotlinproject.pojo.Section
import com.hardzei.firebasewithimageskotlinproject.utils.Create
import com.hardzei.firebasewithimageskotlinproject.utils.Delete
import com.hardzei.firebasewithimageskotlinproject.utils.Request
import com.hardzei.firebasewithimageskotlinproject.utils.Update
import com.hardzei.firebasewithimageskotlinproject.utils.addNewImage
import com.hardzei.firebasewithimageskotlinproject.utils.addNewLocation
import com.hardzei.firebasewithimageskotlinproject.utils.changeLocationName
import com.hardzei.firebasewithimageskotlinproject.utils.deleteSelectedImages

class FirebaseDatabase : MainContract.BaseDatabase() {

    private val firebaseDB = Firebase.firestore
    private val storageRef = Firebase.storage.reference

    override fun readSections(onReadFinishedListener: MainContract.DatabaseCallBack.OnReadFinishedListener) {

        firebaseDB.collection("sections")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG + "${this.javaClass.name}: ", "${document.id} => ${document.data}}")
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
                onReadFinishedListener.onReadFinished(Success(listWithSections))
            }
            .addOnFailureListener { exception ->
                Log.d(TAG + "${this.javaClass.name}: ", "Error getting documents.", exception)
                exception.message?.let {
                    onReadFinishedListener.onReadFinished(Failed(it))
                }
            }
    }

    override fun createSection(onCreateFinishedListener: MainContract.DatabaseCallBack.OnCreateFinishedListener) {
        val newSection = Section("1/0", "Name of section", listOf())
        // Add a new document with a generated ID
        firebaseDB.collection("sections")
            .add(newSection)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    TAG + "${this.javaClass.name}: ",
                    "DocumentSnapshot added with ID: ${documentReference.id}"
                )
                onCreateFinishedListener.onCreateFinished(Success(listOf()))
            }
            .addOnFailureListener { exception ->
                Log.d(TAG + "${this.javaClass.name}: ", "Error adding document: ", exception)
                exception.message?.let {
                    onCreateFinishedListener.onCreateFinished(Failed(it))
                }
            }
    }

    override fun createLocation(
        onCreateFinishedListener: MainContract.DatabaseCallBack.OnCreateFinishedListener,
        request: Request
    ) {
        request.let {
            if (request is Create) {
                val section = request.section
                section?.let {
                    val currentRef = firebaseDB.collection("sections").document(section.id)
                    currentRef
                        .update(
                            "id",
                            section.id,
                            "listWithLocations",
                            section.listWithLocations.addNewLocation()
                        )
                        .addOnSuccessListener {
                            Log.d(
                                TAG + "${this.javaClass.name}: ",
                                "DocumentSnapshot successfully updated!"
                            )
                            //        getSectionsFromFDB()
                            onCreateFinishedListener.onCreateFinished(Success(listOf()))
                        }
                        .addOnFailureListener { exception ->
                            Log.d(
                                TAG + "${this.javaClass.name}: ",
                                "Error adding document: ",
                                exception
                            )
                            exception.message?.let {
                                onCreateFinishedListener.onCreateFinished(Failed(it))
                            }
                        }
                }
            }
        }
    }

    override fun createPhoto(
        onCreateFinishedListener: MainContract.DatabaseCallBack.OnCreateFinishedListener,
        request: Request
    ) {
        createPhotoInStorRef(onCreateFinishedListener, request)
    }

    private fun createPhotoInFirestore(
        onCreateFinishedListener: MainContract.DatabaseCallBack.OnCreateFinishedListener,
        request: Request,
        referenceWithCurrentImage: String
    ) {
        if (request !is Create)
            return
        if (request.section == null || request.numberOfLocation == null)
            return
        val section = request.section
        val numberOfLocation = request.numberOfLocation

        firebaseDB.collection("sections")
            .document(section.id)
            .update(
                "id",
                section.id,
                "listWithLocations",
                section.listWithLocations.addNewImage(numberOfLocation, referenceWithCurrentImage)
            )
            .addOnSuccessListener {
                Log.d(TAG + "${this.javaClass.name}: ", "DocumentSnapshot successfully updated!")
                onCreateFinishedListener.onCreateFinished(Success(listOf()))
            }
            .addOnFailureListener { exception ->
                Log.d(
                    TAG + "${this.javaClass.name}: ",
                    "Error updating document 2004",
                    exception
                )
                exception.message?.let {
                    onCreateFinishedListener.onCreateFinished(Failed(it))
                }
            }
    }

    private fun createPhotoInStorRef(
        onCreateFinishedListener: MainContract.DatabaseCallBack.OnCreateFinishedListener,
        request: Request
    ) {
        if (request !is Create)
            return
        val file = request.uri
        val riversRef = storageRef.child("images/${file?.lastPathSegment}")
        val uploadTask = riversRef.putFile(file!!)

        // Register observers to listen for when the download is done or if it fails
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    Log.d(TAG + "${this.javaClass.name}: ", "${it.message}")
                }
            }
            riversRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                Log.d(TAG + "${this.javaClass.name}: ", "$downloadUri")
                createPhotoInFirestore(onCreateFinishedListener, request, downloadUri.toString())
            } else {
                Log.d(TAG + "${this.javaClass.name}: ", "Dowloading uri not complete 2006")
                onCreateFinishedListener.onCreateFinished(Failed("Dowloading uri not complete 2006"))
            }
        }
    }

    override fun updateSectionName(
        onUpdateFinishedListener: MainContract.DatabaseCallBack.OnUpdateFinishedListener,
        request: Request
    ) {
        if (request !is Update)
            return
        val changedText = request.newText
        val section = request.section
        firebaseDB.collection("sections").document(section.id)
            .update(
                "id", section.id,
                "nameOfSection", changedText
            )
            .addOnSuccessListener {
                Log.d(TAG + "${this.javaClass.name}: ", "DocumentSnapshot successfully updated!")
                onUpdateFinishedListener.onUpdateFinished(Success(listOf()))
            }
            .addOnFailureListener { exception ->
                Log.d(
                    TAG + "${this.javaClass.name}: ",
                    "Error updating document 2002",
                    exception
                )
                exception.message?.let {
                    onUpdateFinishedListener.onUpdateFinished(Failed(it))
                }
            }
    }

    override fun updateLocationName(
        onUpdateFinishedListener: MainContract.DatabaseCallBack.OnUpdateFinishedListener,
        request: Request
    ) {
        if (request !is Update)
            return
        val changedText = request.newText
        val section = request.section
        val numberOfLocation = request.numberOfLocation ?: return

        val currentRef = firebaseDB.collection("sections").document(section.id)
        currentRef
            .update(
                "id",
                section.id,
                "listWithLocations",
                section.listWithLocations.changeLocationName(numberOfLocation, changedText)
            )
            .addOnSuccessListener {
                Log.d(TAG + "${this.javaClass.name}: ", "DocumentSnapshot successfully updated!")
                onUpdateFinishedListener.onUpdateFinished(Success(listOf()))
            }
            .addOnFailureListener { exception ->
                Log.d(
                    TAG + "${this.javaClass.name}: ",
                    "Error updating document 2001",
                    exception
                )
                exception.message?.let {
                    onUpdateFinishedListener.onUpdateFinished(Failed(it))
                }
            }
    }

    override fun deletePhotos(
        onDeleteFinishedListener: MainContract.DatabaseCallBack.OnDeleteFinishedListener,
        request: Request
    ) {
        if (request !is Delete)
            return
        val listWithNumbersOfImages = request.listWithNumbersOfImages
        val section = request.section
        val numberOfLocation = request.numberOfLocation

        val currentRef = firebaseDB.collection("sections").document(section.id)
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
                Log.d(TAG + "${this.javaClass.name}: ", "DocumentSnapshot successfully updated!")
                onDeleteFinishedListener.onDeleteFinished(Success(listOf()))
            }
            .addOnFailureListener { exception ->
                Log.d(
                    TAG + "${this.javaClass.name}: ",
                    "Error updating document 2003",
                    exception
                )
                exception.message?.let {
                    onDeleteFinishedListener.onDeleteFinished(Failed(it))
                }
            }
    }
}
