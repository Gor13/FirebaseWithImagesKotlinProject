package com.hardzei.firebasewithimageskotlinproject.di.component

import com.hardzei.firebasewithimageskotlinproject.MainContract
import com.hardzei.firebasewithimageskotlinproject.di.module.VMModule
import com.hardzei.firebasewithimageskotlinproject.di.scope.ActivityScope
import com.hardzei.firebasewithimageskotlinproject.view.fragments.LocationsFragment
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [VMModule::class])
interface ActivityComponent {
    fun inject(locationsFragment: LocationsFragment)
    val locationsFragmentViewModel: MainContract.BaseViewModel
}
