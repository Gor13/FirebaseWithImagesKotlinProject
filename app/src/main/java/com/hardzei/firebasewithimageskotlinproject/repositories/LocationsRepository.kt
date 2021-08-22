package com.hardzei.firebasewithimageskotlinproject.repositories

import com.hardzei.firebasewithimageskotlinproject.MainContract
import com.hardzei.firebasewithimageskotlinproject.db.Result
import com.hardzei.firebasewithimageskotlinproject.utils.Remote
import com.hardzei.firebasewithimageskotlinproject.utils.Request

class LocationsRepository constructor(
    @Remote
    private val remoteDatabase: MainContract.BaseDatabase
) : MainContract.BaseRepository() {

    private lateinit var onReadFinishedListener: MainContract.RepositoryCallBack.OnReadFinishedListener
    private lateinit var onCreateFinishedListener: MainContract.RepositoryCallBack.OnCreateFinishedListener
    private lateinit var onUpdateFinishedListener: MainContract.RepositoryCallBack.OnUpdateFinishedListener
    private lateinit var onDeleteFinishedListener: MainContract.RepositoryCallBack.OnDeleteFinishedListener

    override fun readSectionsFromDB(onReadFinishedListener: MainContract.RepositoryCallBack.OnReadFinishedListener) {
        remoteDatabase.readSections(this)
        this.onReadFinishedListener = onReadFinishedListener
    }

    override fun onReadFinished(result: Result) {
        onReadFinishedListener.onReadSectionsFinished(result)
    }

    override fun createSectionInDB(onCreateFinishedListener: MainContract.RepositoryCallBack.OnCreateFinishedListener) {
        remoteDatabase.createSection(this)
        this.onCreateFinishedListener = onCreateFinishedListener
    }

    override fun createLocationInDB(
        onCreateFinishedListener: MainContract.RepositoryCallBack.OnCreateFinishedListener,
        request: Request
    ) {
        remoteDatabase.createLocation(this, request)
        this.onCreateFinishedListener = onCreateFinishedListener
    }

    override fun createPhotoInDB(
        onCreateFinishedListener: MainContract.RepositoryCallBack.OnCreateFinishedListener,
        request: Request
    ) {
        remoteDatabase.createPhoto(this, request)
        this.onCreateFinishedListener = onCreateFinishedListener
    }

    override fun onCreateFinished(result: Result) {
        onCreateFinishedListener.onCreateFinished(result)
    }

    override fun updateSectionNameInDB(
        onUpdateFinishedListener: MainContract.RepositoryCallBack.OnUpdateFinishedListener,
        request: Request
    ) {
        remoteDatabase.updateSectionName(this, request)
        this.onUpdateFinishedListener = onUpdateFinishedListener
    }

    override fun updateLocationNameInDB(
        onUpdateFinishedListener: MainContract.RepositoryCallBack.OnUpdateFinishedListener,
        request: Request
    ) {
        remoteDatabase.updateLocationName(this, request)
        this.onUpdateFinishedListener = onUpdateFinishedListener
    }

    override fun onUpdateFinished(result: Result) {
        onUpdateFinishedListener.onUpdateFinished(result)
    }

    override fun deletePhotosInDB(
        onDeleteFinishedListener: MainContract.RepositoryCallBack.OnDeleteFinishedListener,
        request: Request
    ) {
        remoteDatabase.deletePhotos(this, request)
        this.onDeleteFinishedListener = onDeleteFinishedListener
    }

    override fun onDeleteFinished(result: Result) {
        onDeleteFinishedListener.onDeleteFinished(result)
    }
}
