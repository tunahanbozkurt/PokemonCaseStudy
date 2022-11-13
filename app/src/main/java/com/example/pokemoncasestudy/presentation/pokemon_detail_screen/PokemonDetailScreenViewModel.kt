package com.example.pokemoncasestudy.presentation.pokemon_detail_screen

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemoncasestudy.domain.model.PokemonDetail
import com.example.pokemoncasestudy.domain.usecase.UseCase
import com.example.pokemoncasestudy.util.ErrorState
import com.example.pokemoncasestudy.util.LOG_ERROR
import com.example.pokemoncasestudy.util.LOG_SUCCESS
import com.example.pokemoncasestudy.util.Resource
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.squareup.picasso.Picasso
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class PokemonDetailScreenViewModel @Inject constructor(
    private val useCase: UseCase,
    private val firebaseAnalytics: FirebaseAnalytics
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

    var errorState = MutableStateFlow(ErrorState())
        private set


    fun getPokemonDetail(page: String?) {
        viewModelScope.launch {
            if (page != null) {
                val result = useCase.getPokemonDetailUseCase(page)
                result.collect {
                    when(it) {
                        is Resource.Error -> {
                            errorState.value = errorState.value.copy(message = it.message)
                            loadingState.value = false
                            firebaseAnalytics.logEvent(LOG_ERROR) {
                                param(LOG_ERROR, "error")
                            }
                        }
                        is Resource.Loading -> {
                            loadingState.value = true
                        }
                        is Resource.Success -> {
                            errorState.value = errorState.value.copy(message = null)
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
                            firebaseAnalytics.logEvent(LOG_SUCCESS) {
                                param(LOG_SUCCESS, "success")
                            }
                        }
                    }
                }

            }
        }
    }
}