package com.example.pokemoncasestudy.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.pokemoncasestudy.R
import com.example.pokemoncasestudy.databinding.ActivityMainBinding
import com.example.pokemoncasestudy.util.POKEMON
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

const val MAIN_ACTIVITY = "main_activity"

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
        fcmSubscribeToTopic()
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

    /**
     * Subscribes to pokemon topic to be able to get firebase notification messages
     */
    private fun fcmSubscribeToTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(POKEMON).addOnSuccessListener {
            Log.i(MAIN_ACTIVITY, "subscribeToTopicIsSuccess")
        }.addOnFailureListener {
            Log.i(MAIN_ACTIVITY, "subscribeToTopicIsFailure")
        }
    }
}