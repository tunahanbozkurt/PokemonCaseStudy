package com.example.pokemoncasestudy.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.pokemoncasestudy.R
import com.example.pokemoncasestudy.databinding.ActivityMainBinding
import com.example.pokemoncasestudy.util.POKEMON
import com.example.pokemoncasestudy.util.WELCOME_MESSAGE
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

const val MAIN_ACTIVITY = "main_activity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var welcomeMessage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupNavigationComponents()
        navigationSelectedListener()

        subscribe()

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

    private fun subscribe() {
        lifecycleScope.launch {
            fcmSubscribeToTopic()
            setupRemoteConfig()
            showWelcomeMessage()
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

    /**
     * Setups firebase remote config
     */
    private suspend fun setupRemoteConfig() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        try {
            remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults).await()
            welcomeMessage = remoteConfig.getString(WELCOME_MESSAGE)
            fetchRemoteConfig()

        }catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * Updates remote config values
     */
    private suspend fun fetchRemoteConfig() {
        remoteConfig.fetchAndActivate().await()
        welcomeMessage = remoteConfig.getString(WELCOME_MESSAGE)
    }

    /**
     * Shows welcome message to user which is come from remote config
     */
    private fun showWelcomeMessage() {
        Toast.makeText(this, welcomeMessage, Toast.LENGTH_LONG).show()
    }
}