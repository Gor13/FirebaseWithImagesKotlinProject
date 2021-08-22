package com.hardzei.firebasewithimageskotlinproject.model

import com.hardzei.firebasewithimageskotlinproject.MainContract
import com.hardzei.firebasewithimageskotlinproject.db.Result
import com.hardzei.firebasewithimageskotlinproject.db.Success
import com.hardzei.firebasewithimageskotlinproject.utils.Request

class LocationsModel constructor(
    private val repository: MainContract.BaseRepository,
) : MainContract.BaseModel() {

    private lateinit var onReadFinishedListener: MainContract.ModelCallBack.OnReadFinishedListener
    private lateinit var onCreateFinishedListener: MainContract.ModelCallBack.OnCreateFinishedListener
    private lateinit var onUpdateFinishedListener: MainContract.ModelCallBack.OnUpdateFinishedListener
    private lateinit var onDeleteFinishedListener: MainContract.ModelCallBack.OnDeleteFinishedListener

    override fun readSectionsFromRepository(
        onReadFinishedListener: MainContract.ModelCallBack.OnReadFinishedListener,
        request: Request
    ) {
        repository.readSectionsFromDB(this)
        this.onReadFinishedListener = onReadFinishedListener
    }

    override fun onReadSectionsFinished(result: Result) {
        onReadFinishedListener.onReadSectionsFinished(result)
    }

    override fun createSectionInRepository(
        onCreateFinishedListener: MainContract.ModelCallBack.OnCreateFinishedListener,
        request: Request
    ) {
        repository.createSectionInDB(this)
        this.onCreateFinishedListener = onCreateFinishedListener
    }

    override fun createLocationInRepository(
        onCreateFinishedListener: MainContract.ModelCallBack.OnCreateFinishedListener,
        request: Request
    ) {
        repository.createLocationInDB(this, request)
        this.onCreateFinishedListener = onCreateFinishedListener
    }

    override fun createPhotoInRepository(
        onCreateFinishedListener: MainContract.ModelCallBack.OnCreateFinishedListener,
        request: Request
    ) {
        repository.createPhotoInDB(this, request)
        this.onCreateFinishedListener = onCreateFinishedListener
    }

    override fun onCreateFinished(result: Result) {
        onCreateFinishedListener.onCreateFinished(result)
        if (result is Success) {
            repository.readSectionsFromDB(this)
        }
    }

    override fun updateSectionNameInRepository(
        onUpdateFinishedListener: MainContract.ModelCallBack.OnUpdateFinishedListener,
        request: Request
    ) {
        repository.updateSectionNameInDB(this, request)
        this.onUpdateFinishedListener = onUpdateFinishedListener
    }

    override fun updateLocationNameInRepository(
        onUpdateFinishedListener: MainContract.ModelCallBack.OnUpdateFinishedListener,
        request: Request
    ) {
        repository.updateLocationNameInDB(this, request)
        this.onUpdateFinishedListener = onUpdateFinishedListener
    }

    override fun onUpdateFinished(result: Result) {
        onUpdateFinishedListener.onUpdateFinished(result)
    }

    override fun deletePhotosInRepository(
        onDeleteFinishedListener: MainContract.ModelCallBack.OnDeleteFinishedListener,
        request: Request
    ) {
        repository.deletePhotosInDB(this, request)
        this.onDeleteFinishedListener = onDeleteFinishedListener
    }

    override fun onDeleteFinished(result: Result) {
        onDeleteFinishedListener.onDeleteFinished(result)
        if (result is Success) {
            repository.readSectionsFromDB(this)
        }
    }
}
