package com.hardzei.firebasewithimageskotlinproject.di.component

import android.content.Context
import com.hardzei.firebasewithimageskotlinproject.InitApplication
import com.hardzei.firebasewithimageskotlinproject.MainContract
import com.hardzei.firebasewithimageskotlinproject.di.module.AppModule
import com.hardzei.firebasewithimageskotlinproject.di.module.ContextModule
import com.hardzei.firebasewithimageskotlinproject.di.module.DataModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class, ContextModule::class])
interface AppComponent {
    fun inject(initApplication: InitApplication)
    val context: Context
    val findItemsInteractor: MainContract.BaseModel
}
