package com.hardzei.firebasewithimageskotlinproject.di.module

import com.hardzei.firebasewithimageskotlinproject.MainContract
import com.hardzei.firebasewithimageskotlinproject.model.LocationsModel
import dagger.Module
import dagger.Provides

@Module(includes = [RepositoryModule::class])
class DataModule {
    @Provides
    fun provideLocationsModel(locationsRepository: MainContract.BaseRepository): MainContract.BaseModel {
        return LocationsModel(locationsRepository)
    }
}
