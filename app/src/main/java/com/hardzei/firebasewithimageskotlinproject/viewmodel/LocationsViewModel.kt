package com.hardzei.firebasewithimageskotlinproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hardzei.firebasewithimageskotlinproject.MainContract
import com.hardzei.firebasewithimageskotlinproject.db.Failed
import com.hardzei.firebasewithimageskotlinproject.db.Result
import com.hardzei.firebasewithimageskotlinproject.db.Success
import com.hardzei.firebasewithimageskotlinproject.pojo.Section
import com.hardzei.firebasewithimageskotlinproject.utils.Request
import javax.inject.Inject

class LocationsViewModel @Inject constructor(
    private val model: MainContract.BaseModel
) : MainContract.BaseViewModel() {

    private var _listWithSections: MutableLiveData<List<Section>> = MutableLiveData()
    val listWithSections: LiveData<List<Section>>
        get() = _listWithSections

    private var _successMessage: MutableLiveData<String> = MutableLiveData()
    val successMessage: LiveData<String>
        get() = _successMessage

    private var _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    override fun readListWithSections(request: Request) {
        model.readSectionsFromRepository(this, request)
    }

    override fun createNewSection(request: Request) {
        model.createSectionInRepository(this, request)
    }

    override fun createNewPhoto(request: Request) {
        model.createPhotoInRepository(this, request)
    }

    override fun createNewLocation(request: Request) {
        model.createLocationInRepository(this, request)
    }

    override fun updateSectionName(request: Request) {
        model.updateSectionNameInRepository(this, request)
    }

    override fun updateLocationName(request: Request) {
        model.updateLocationNameInRepository(this, request)
    }

    override fun deletePhotos(request: Request) {
        model.deletePhotosInRepository(this, request)
    }

    override fun onReadSectionsFinished(result: Result) {
        when (result) {
            is Success -> _listWithSections.value = result.listWithSections
            is Failed -> _errorMessage.value = result.errorMessage
        }
    }

    override fun onCreateFinished(result: Result) {
        when (result) {
            is Success -> _successMessage.value = "Success"
            is Failed -> _successMessage.value = "Failed ${result.errorMessage}"
        }
    }

    override fun onUpdateFinished(result: Result) {
        when (result) {
            is Success -> _successMessage.value = "Success"
            is Failed -> _successMessage.value = "Failed ${result.errorMessage}"
        }
    }

    override fun onDeleteFinished(result: Result) {
        when (result) {
            is Success -> _successMessage.value = "Success"
            is Failed -> _successMessage.value = "Failed ${result.errorMessage}"
        }
    }

    class Factory(
        private val model: MainContract.BaseModel
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == LocationsViewModel::class)
            return LocationsViewModel(model) as T
        }
    }
}
