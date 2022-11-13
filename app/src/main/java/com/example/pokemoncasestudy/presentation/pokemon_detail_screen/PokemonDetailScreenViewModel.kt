package com.example.pokemoncasestudy.presentation.pokemon_detail_screen

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemoncasestudy.R
import com.example.pokemoncasestudy.domain.model.PokemonDetail
import com.example.pokemoncasestudy.domain.usecase.UseCase
import com.example.pokemoncasestudy.util.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.squareup.picasso.Picasso
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import java.net.SocketTimeoutException
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
                                param(LOG_ERROR, ERROR)
                            }
                        }
                        is Resource.Loading -> {
                            loadingState.value = true
                        }
                        is Resource.Success -> {
                            errorState.value = errorState.value.copy(message = null)
                            val bitmapList = withContext(dispatcherIO) {
                                try {
                                    val bitmap0 = Picasso.get().load(it.data?.frontImgUrl).get()
                                    val bitmap1 = Picasso.get().load(it.data?.backImgUrl).get()
                                    return@withContext listOf(bitmap0, bitmap1)
                                }
                                catch (e: SocketTimeoutException) {
                                    return@withContext listOf<Bitmap>()
                                }
                                catch (e: Exception) {
                                    return@withContext listOf<Bitmap>()
                                }
                            }

                            if (bitmapList.isNotEmpty()) {
                                frontBitmapState.value = bitmapList[0]
                                backBitmapState.value = bitmapList[1]

                                it.data?.let { pokemonDetail ->
                                    pokemonDetailState.value = pokemonDetail
                                }

                                loadingState.value = false
                                firebaseAnalytics.logEvent(LOG_SUCCESS) {
                                    param(LOG_SUCCESS, SUCCESS)
                                }

                            }else {
                                errorState.value = errorState.value.copy(message = R.string.unexpected_error)
                                loadingState.value = false
                                firebaseAnalytics.logEvent(LOG_ERROR) {
                                    param(LOG_ERROR, ERROR)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}