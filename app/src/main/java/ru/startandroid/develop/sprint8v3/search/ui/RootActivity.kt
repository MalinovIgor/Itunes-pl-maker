package ru.startandroid.develop.sprint8v3.search.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.startandroid.develop.sprint8v3.R
import ru.startandroid.develop.sprint8v3.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("CurrentActivity", "Current destination: ${destination.label}")
            when (destination.id) {
                R.id.playlistCreationFragment -> {
                    Log.d("CurrentActivity", "Destination ID: ${destination.id}")
                    bottomNavigationView.visibility = View.GONE
                    Log.d("CurrentActivity", "and now current visibility: ${bottomNavigationView.isVisible}")
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                    Log.d("CurrentActivity", "and now current visibility: ${bottomNavigationView.isVisible}")

                }
            }
        }
    }
}