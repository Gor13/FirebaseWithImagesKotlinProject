package com.hardzei.firebasewithimageskotlinproject.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hardzei.firebasewithimageskotlinproject.R
import com.hardzei.firebasewithimageskotlinproject.view.fragments.LocationsFragment

private const val NAME_STACK_FOR_ACTIVITY = "NAME_STACK_FOR_ACTIVITY"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.activityLocationsFrameLayout, LocationsFragment())
            .addToBackStack(NAME_STACK_FOR_ACTIVITY)
            .commit()
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount

        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}
