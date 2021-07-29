package com.hardzei.firebasewithimageskotlinproject

import androidx.lifecycle.ViewModel
import com.hardzei.firebasewithimageskotlinproject.db.Result
import com.hardzei.firebasewithimageskotlinproject.utils.Request

interface MainContract {
    interface ViewCallBack

    abstract class BaseViewModel :
        ViewModel(),
        ViewModelCallBack,
        ModelCallBack.OnReadFinishedListener,
        ModelCallBack.OnCreateFinishedListener,
        ModelCallBack.OnUpdateFinishedListener,
        ModelCallBack.OnDeleteFinishedListener

    interface ViewModelCallBack :
        ViewModelReadRequest,
        ViewModelCreateRequest,
        ViewModelUpdateRequest,
        ViewModelDeleteRequest

    interface ViewModelReadRequest {
        fun readListWithSections(request: Request)
    }

    interface ViewModelCreateRequest {
        fun createNewSection(request: Request)
        fun createNewPhoto(request: Request)
        fun createNewLocation(request: Request)
    }

    interface ViewModelUpdateRequest {
        fun updateSectionName(request: Request)
        fun updateLocationName(request: Request)
    }

    interface ViewModelDeleteRequest {
        fun deletePhotos(request: Request)
    }

    abstract class BaseModel :
        ModelCallBack,
        RepositoryCallBack.OnReadFinishedListener,
        RepositoryCallBack.OnCreateFinishedListener,
        RepositoryCallBack.OnUpdateFinishedListener,
        RepositoryCallBack.OnDeleteFinishedListener

    interface ModelCallBack : ReadableModel, WritableModel, UpdatableModel, DeletableModel {
        interface OnReadFinishedListener {
            fun onReadSectionsFinished(result: Result)
        }

        interface OnCreateFinishedListener {
            fun onCreateFinished(result: Result)
        }

        interface OnUpdateFinishedListener {
            fun onUpdateFinished(result: Result)
        }

        interface OnDeleteFinishedListener {
            fun onDeleteFinished(result: Result)
        }
    }

    interface ReadableModel {
        fun readSectionsFromRepository(
            onReadFinishedListener: ModelCallBack.OnReadFinishedListener,
            request: Request
        )
    }

    interface WritableModel {
        fun createSectionInRepository(
            onCreateFinishedListener: ModelCallBack.OnCreateFinishedListener,
            request: Request
        )

        fun createLocationInRepository(
            onCreateFinishedListener: ModelCallBack.OnCreateFinishedListener,
            request: Request
        )

        fun createPhotoInRepository(
            onCreateFinishedListener: ModelCallBack.OnCreateFinishedListener,
            request: Request
        )
    }

    interface UpdatableModel {
        fun updateSectionNameInRepository(
            onUpdateFinishedListener: ModelCallBack.OnUpdateFinishedListener,
            request: Request
        )

        fun updateLocationNameInRepository(
            onUpdateFinishedListener: ModelCallBack.OnUpdateFinishedListener,
            request: Request
        )
    }

    interface DeletableModel {
        fun deletePhotosInRepository(
            onDeleteFinishedListener: ModelCallBack.OnDeleteFinishedListener,
            request: Request
        )
    }

    abstract class BaseRepository :
        RepositoryCallBack,
        DatabaseCallBack.OnReadFinishedListener,
        DatabaseCallBack.OnCreateFinishedListener,
        DatabaseCallBack.OnUpdateFinishedListener,
        DatabaseCallBack.OnDeleteFinishedListener

    interface RepositoryCallBack :
        ReadableRepository,
        WritableRepository,
        UpdatableRepository,
        DeletableRepository {
        interface OnReadFinishedListener {
            fun onReadSectionsFinished(result: Result)
        }

        interface OnCreateFinishedListener {
            fun onCreateFinished(result: Result)
        }

        interface OnUpdateFinishedListener {
            fun onUpdateFinished(result: Result)
        }

        interface OnDeleteFinishedListener {
            fun onDeleteFinished(result: Result)
        }
    }

    interface ReadableRepository {
        fun readSectionsFromDB(onReadFinishedListener: RepositoryCallBack.OnReadFinishedListener)
    }

    interface WritableRepository {
        fun createSectionInDB(onCreateFinishedListener: RepositoryCallBack.OnCreateFinishedListener)
        fun createLocationInDB(
            onCreateFinishedListener: RepositoryCallBack.OnCreateFinishedListener,
            request: Request
        )

        fun createPhotoInDB(
            onCreateFinishedListener: RepositoryCallBack.OnCreateFinishedListener,
            request: Request
        )
    }

    interface UpdatableRepository {
        fun updateSectionNameInDB(
            onUpdateFinishedListener: RepositoryCallBack.OnUpdateFinishedListener,
            request: Request
        )

        fun updateLocationNameInDB(
            onUpdateFinishedListener: RepositoryCallBack.OnUpdateFinishedListener,
            request: Request
        )
    }

    interface DeletableRepository {
        fun deletePhotosInDB(
            onDeleteFinishedListener: RepositoryCallBack.OnDeleteFinishedListener,
            request: Request
        )
    }

    abstract class BaseDatabase :
        DatabaseCallBack

    interface DatabaseCallBack :
        ReadableDatabase,
        WritableDatabase,
        UpdatableDatabase,
        DeletableDatabase {
        interface OnReadFinishedListener {
            fun onReadFinished(result: Result)
        }

        interface OnCreateFinishedListener {
            fun onCreateFinished(result: Result)
        }

        interface OnUpdateFinishedListener {
            fun onUpdateFinished(result: Result)
        }

        interface OnDeleteFinishedListener {
            fun onDeleteFinished(result: Result)
        }
    }

    interface ReadableDatabase {
        fun readSections(onReadFinishedListener: DatabaseCallBack.OnReadFinishedListener)
    }

    interface WritableDatabase {
        fun createSection(onCreateFinishedListener: DatabaseCallBack.OnCreateFinishedListener)
        fun createLocation(
            onCreateFinishedListener: DatabaseCallBack.OnCreateFinishedListener,
            request: Request
        )

        fun createPhoto(
            onCreateFinishedListener: DatabaseCallBack.OnCreateFinishedListener,
            request: Request
        )
    }

    interface UpdatableDatabase {
        fun updateSectionName(
            onUpdateFinishedListener: DatabaseCallBack.OnUpdateFinishedListener,
            request: Request
        )

        fun updateLocationName(
            onUpdateFinishedListener: DatabaseCallBack.OnUpdateFinishedListener,
            request: Request
        )
    }

    interface DeletableDatabase {
        fun deletePhotos(
            onDeleteFinishedListener: DatabaseCallBack.OnDeleteFinishedListener,
            request: Request
        )
    }
}
