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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.pokemoncasestudy.R
import com.example.pokemoncasestudy.databinding.FragmentPokemonDetailScreenBinding
import com.example.pokemoncasestudy.service.OverlayForegroundService
import com.example.pokemoncasestudy.util.BACK_BITMAP
import com.example.pokemoncasestudy.util.FRONT_BITMAP
import com.example.pokemoncasestudy.util.POKEMON_NAME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream

const val POKEMON_DETAIL_FRAGMENT = "pokemon_detail_fragment"
@AndroidEntryPoint
class PokemonDetailScreenFragment : Fragment() {

    private lateinit var binding: FragmentPokemonDetailScreenBinding
    private val viewModel: PokemonDetailScreenViewModel by viewModels()

    private val args: PokemonDetailScreenFragmentArgs by navArgs()
    private var frontBitmap: Bitmap? = null
    private var backBitmap: Bitmap? = null
    private var url: String? = null
    private var pokemonName: String? = null

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

        // Best practice to collect multiple flows
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.pokemonDetailState.collect {
                        pokemonName = it.name
                        binding.pokemonName.text = getString(R.string.pokemon_name, pokemonName)
                        binding.pokemonHeight.text = getString(R.string.pokemon_height, it.height)
                        binding.pokemonWeight.text = getString(R.string.pokemon_weight, it.weight)
                        binding.overlayButton.text = getString(R.string.detail_screen_button, pokemonName)
                    }
                }

                launch {
                    viewModel.errorState.collect {
                        if (it.message != null) {
                            binding.errorMessage.text = getString(it.message)
                            binding.connectionError.visibility = View.VISIBLE
                        }else {
                            binding.connectionError.visibility = View.GONE
                        }
                    }
                }

                launch {
                    viewModel.frontBitmapState.collect {
                        binding.image.setImageBitmap(it)
                        frontBitmap = it
                    }
                }

                launch {
                    viewModel.backBitmapState.collect {
                        backBitmap = it
                    }
                }

                launch {
                    viewModel.loadingState.collect {
                        binding.progressBarView.visibility = if (it) View.VISIBLE else View.GONE
                    }
                }
            }
        }
    }

    private fun prepareView() {

        binding.tryAgainButton.setOnClickListener {
            viewModel.getPokemonDetail(url)
        }

        binding.overlayButton.setOnClickListener {
            val intent = Intent(activity ,OverlayForegroundService::class.java)
            intent.putExtra(POKEMON_NAME, pokemonName)
            val frontResult = addBitmapToInternalStorage(frontBitmap, FRONT_BITMAP)
            val backResult = addBitmapToInternalStorage(backBitmap, BACK_BITMAP)
            if (frontResult && backResult) {
                activity?.startService(intent)
                activity?.finish()
            }
        }
    }

    /**
     * Writes bitmap to internal storage to use later
     */
    private fun addBitmapToInternalStorage(bitmap: Bitmap?, name: String): Boolean{
        try {
            val bytes = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, bytes)
            val fo: FileOutputStream? = activity?.openFileOutput(name, Context.MODE_PRIVATE)
            fo?.write(bytes.toByteArray())
            fo?.close()
        } catch (e: Exception) {
            Log.e(POKEMON_DETAIL_FRAGMENT, e.message.toString())
            return false
        }
        return true
    }
}