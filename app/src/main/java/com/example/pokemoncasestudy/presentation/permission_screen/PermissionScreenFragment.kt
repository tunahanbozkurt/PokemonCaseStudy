package com.example.pokemoncasestudy.presentation.permission_screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pokemoncasestudy.R
import com.example.pokemoncasestudy.databinding.FragmentPermissionScreenBinding
import com.example.pokemoncasestudy.util.HALF_SECONDS_IN_MILLIS
import com.example.pokemoncasestudy.util.THIRTY_SECONDS_IN_MILLIS
import com.example.pokemoncasestudy.util.bringToFront
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PermissionScreenFragment : Fragment() {

    private lateinit var binding: FragmentPermissionScreenBinding

    // To observe permission state. It needs to be cancelled in onDestroy method
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissionAndNavigate(activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPermissionScreenBinding.inflate(inflater, container, false)

        prepareView()

        return binding.root
    }

    /**
     * Preparing views here
     */
    private fun prepareView() {
        hideToolbarAndDrawer()

        binding.permissionButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermission(activity)
            }
        }
    }

    /**
     * Requests DrawOverlay permission and checks for results
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission(activity: Activity?) {

        // It is false if VERSION_CODES >= M
        if (!isOverlayPermissionGranted(activity)){
            val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(
                        "package:${context?.packageName}"
                    )
            )
            startActivity(intent)
            observePermissionStateAndBringTheActivity(activity)
        }
    }

    /**
     * Checks for DrawOverlays permission and navigates to main screen if it is granted
     */
    private fun checkPermissionAndNavigate(activity: Activity?) {
        if (isOverlayPermissionGranted(activity)){
           findNavController()
               .navigate(R.id.action_permissionScreenFragment_to_mainScreenFragment)
        }
    }

    /**
     * Observes DrawOverlays permission state for 30 seconds.
     * Brings the activity related to
     */
    private fun observePermissionStateAndBringTheActivity(activity: Activity?) {
        job?.cancel()
        job = lifecycleScope.launch {
            var timeOut = 0
            while (timeOut < THIRTY_SECONDS_IN_MILLIS) {
                delay(HALF_SECONDS_IN_MILLIS)
                timeOut += HALF_SECONDS_IN_MILLIS.toInt()
                if (isOverlayPermissionGranted(activity)) break
            }
            activity?.bringToFront()
        }
    }

    /**
     * Checks for DrawOverlays permission and returns true if is granted
     */
    private fun isOverlayPermissionGranted(activity: Activity?): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(activity)
        }
        // returns true before api 23 by default...
        return true
    }

    /**
     * Hides toolbar and drawer components
     */
    private fun hideToolbarAndDrawer() {
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        activity?.findViewById<DrawerLayout>(R.id.drawer_layout)?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun onResume() {
        super.onResume()
        job?.cancel()
        checkPermissionAndNavigate(activity)
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
        activity?.findViewById<DrawerLayout>(R.id.drawer_layout)?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }
}