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

    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO

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
                            val bitmapList = withContext(dispatcherIO) {
                                return@withContext listOf<Bitmap?>(
                                    Picasso.get().load(it.data?.frontImgUrl).get(),
                                    Picasso.get().load(it.data?.backImgUrl).get()
                                )
                            }
                            frontBitmapState.value = bitmapList[0]
                            backBitmapState.value = bitmapList[1]
                            it.data?.let { pokemonDetail ->
                                pokemonDetailState.value = pokemonDetail
                            }
                            loadingState.value = false
                        }
                    }
                }

            }
        }
    }
}