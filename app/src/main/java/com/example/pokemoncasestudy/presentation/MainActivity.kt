package com.example.pokemoncasestudy.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.pokemoncasestudy.R
import com.example.pokemoncasestudy.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupNavigationComponents()
        navigationSelectedListener()
    }

    /**
     * Setups navigation structure for the rest of the app
     */
    private fun setupNavigationComponents() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration.Builder(setOf(R.id.mainScreenFragment))
            .setOpenableLayout(binding.drawerLayout)
            .build()

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    /**
     * Listens for navigation drawer menu items click activity
     */
    private fun navigationSelectedListener() {
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.close_app -> {
                    this.finish()
                }
            }
            true
        }
    }
}