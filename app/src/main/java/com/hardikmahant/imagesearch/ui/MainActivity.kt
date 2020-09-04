package com.hardikmahant.imagesearch.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.hardikmahant.imagesearch.R
import com.hardikmahant.imagesearch.db.ImageDatabase
import com.hardikmahant.imagesearch.repository.ImagesRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: ImagesViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = ImagesRepository(ImageDatabase(this))
        val viewModelProviderFactory = ImageSearchViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(ImagesViewModel::class.java)

        navController = navHostFragment.findNavController()
        setupActionBarWithNavController(navController)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            title = when (destination.id) {
                R.id.savedImagesFragment -> getString(R.string.title_favourites)
                else -> getString(R.string.app_name)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}