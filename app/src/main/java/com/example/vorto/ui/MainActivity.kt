package com.example.vorto.ui

import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.vorto.BaseFragment
import com.example.vorto.R
import com.example.vorto.delegates.IBackHandlerDelegate

class MainActivity : AppCompatActivity() ,IBackHandlerDelegate{
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private var currentFragment: BaseFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavController()

    }

    override fun onBackPressed() {
        if (currentFragment == null || !currentFragment!!.onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_container).navigateUp(appBarConfiguration)
    }


    private fun setupNavController() {
        val host: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
                ?: return
        navController = host.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, _, _ ->
            //hidekeyboard()
        }
    }

    override fun setCurrentFragment(baseFragment: BaseFragment) {
        currentFragment=baseFragment
    }
}