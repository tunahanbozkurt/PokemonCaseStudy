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
import com.example.pokemoncasestudy.presentation.MainActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PermissionScreenFragment : Fragment() {

    private lateinit var binding: FragmentPermissionScreenBinding

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermission(activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPermissionScreenBinding.inflate(inflater, container, false)
        prepareView()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        job?.cancel()
        checkPermission(activity)
    }

    private fun prepareView() {
        hideToolbar()

        binding.permissionButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermission(activity)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission(activity: Activity?) {

        // If it is false VERSION_CODES >= M
        if (!isOverlayPermissionGranted(activity)){
            val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(
                        "package:$context.packageName"
                    )
            )
            startActivity(intent)
            observePermissionState(activity)
        }
    }

    private fun checkPermission(activity: Activity?) {
        if (isOverlayPermissionGranted(activity)){
           findNavController()
               .navigate(R.id.action_permissionScreenFragment_to_mainScreenFragment)
        }
    }

    private fun observePermissionState(activity: Activity?) {
        job?.cancel()
        job = lifecycleScope.launch {
            var timeOut = 0
            while (timeOut < 30000) {
                delay(500)
                timeOut += 500
                if (isOverlayPermissionGranted(activity)) break
            }
            activity?.startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    private fun isOverlayPermissionGranted(activity: Activity?): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(activity)
        }
        // returns true before api 23 by default...
        return true
    }

    private fun hideToolbar() {
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        activity?.findViewById<DrawerLayout>(R.id.drawer_layout)?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        activity?.findViewById<DrawerLayout>(R.id.drawer_layout)?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        (activity as AppCompatActivity?)?.supportActionBar?.show()
    }
}