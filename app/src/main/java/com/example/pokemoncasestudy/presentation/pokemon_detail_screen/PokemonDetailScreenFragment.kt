package com.example.pokemoncasestudy.presentation.pokemon_detail_screen

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.pokemoncasestudy.databinding.FragmentPokemonDetailScreenBinding
import com.example.pokemoncasestudy.service.OverlayForegroundService
import com.example.pokemoncasestudy.util.POKEMON_DETAIL_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream


@AndroidEntryPoint
class PokemonDetailScreenFragment : Fragment() {

    private lateinit var binding: FragmentPokemonDetailScreenBinding
    private val viewModel: PokemonDetailScreenViewModel by viewModels()

    private val args: PokemonDetailScreenFragmentArgs by navArgs()
    private var frontBitmap: Bitmap? = null
    private var backBitmap: Bitmap? = null
    private var url: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonDetailScreenBinding.inflate(inflater, container, false)

        prepareView()
        subscribe()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            url = args.url
        }catch (e: Exception) {
            Log.e(POKEMON_DETAIL_FRAGMENT, e.message.toString())
        }
    }

    private fun subscribe() {
        viewModel.getPokemonDetail(url)
        lifecycleScope.launchWhenStarted {
            viewModel.pokemonDetailState.collect {
                binding.pokemonName.text = it.name
                binding.pokemonHeight.text = it.height
                binding.pokemonWeight.text = it.weight
            }
        }

        lifecycleScope.launchWhenStarted {
             viewModel.frontBitmapState.collect {
                 binding.image.setImageBitmap(it)
                 frontBitmap = it
             }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.backBitmapState.collect {
                backBitmap = it
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loadingState.collect {
                binding.progressBarView.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
    }

    private fun prepareView() {
        binding.overlayButton.setOnClickListener {
            val intent = Intent(activity ,OverlayForegroundService::class.java)
            createImageFromBitmap(frontBitmap, "frontBitmap")
            createImageFromBitmap(backBitmap, "backBitmap")
            activity?.startService(intent)
        }
    }

    fun createImageFromBitmap(bitmap: Bitmap?, name: String): String? {
        var fileName: String? = name //no .png or .jpg needed
        try {
            val bytes = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, bytes)
            val fo: FileOutputStream? = activity?.openFileOutput(fileName, Context.MODE_PRIVATE)
            fo?.write(bytes.toByteArray())
            // remember close file output
            fo?.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            fileName = null
        }
        return fileName
    }


}