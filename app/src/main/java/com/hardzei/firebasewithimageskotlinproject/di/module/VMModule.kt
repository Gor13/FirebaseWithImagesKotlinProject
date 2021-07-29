package com.hardzei.firebasewithimageskotlinproject.di.module

import com.hardzei.firebasewithimageskotlinproject.MainContract
import com.hardzei.firebasewithimageskotlinproject.viewmodel.LocationsViewModel
import dagger.Module
import dagger.Provides

@Module
class VMModule constructor(private val viewCallBack: MainContract.ViewCallBack) {

    @Provides
    fun provideView(): MainContract.ViewCallBack {
        return viewCallBack
    }

    @Provides
    fun provideViewModel(
        locationsModel: MainContract.BaseModel
    ): MainContract.BaseViewModel {
        return LocationsViewModel(locationsModel)
    }
}
