package com.example.pokemoncasestudy.presentation.pokemon_detail_screen

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemoncasestudy.domain.model.PokemonDetail
import com.example.pokemoncasestudy.domain.repository.PokemonDetailRepository
import com.example.pokemoncasestudy.util.Resource
import com.squareup.picasso.Picasso
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class PokemonDetailScreenViewModel @Inject constructor(
    private val detailRepo: PokemonDetailRepository
) : ViewModel() {

    var pokemonDetailState = MutableStateFlow(PokemonDetail())
        private set

    var frontBitmapState: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
        private set

    var backBitmapState: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
        private set

    var loadingState = MutableStateFlow(true)
        private set

    fun getPokemonDetail(url: String?) {
        viewModelScope.launch {
            if (url != null) {
                val result = detailRepo.getPokemonDetail(url)
                result.collect {
                    when(it) {
                        is Resource.Error -> {
                            loadingState.value = false
                        }
                        is Resource.Loading -> {
                            loadingState.value = true
                        }
                        is Resource.Success -> {
                            val frontBitmap = async(Dispatchers.IO) { return@async Picasso.get().load(it.data?.frontImgUrl).get() }.await()
                            val backBitmap = async(Dispatchers.IO) { return@async Picasso.get().load(it.data?.backImgUrl).get() }.await()
                            frontBitmapState.value = frontBitmap
                            backBitmapState.value = backBitmap
                            it.data?.let { pokemonDetailState.value = it }
                            loadingState.value = false
                        }
                    }
                }

            }
        }
    }
}