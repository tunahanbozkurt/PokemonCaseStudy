package com.example.pokemoncasestudy.presentation.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemoncasestudy.domain.model.Pokemon
import com.example.pokemoncasestudy.domain.usecase.UseCase
import com.example.pokemoncasestudy.util.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor (
    private val useCase: UseCase,
    private val firebaseAnalytics: FirebaseAnalytics
) : ViewModel() {


    var pokemonDTOListState = MutableStateFlow<ArrayList<Pokemon?>>(arrayListOf())
        private set

    var paginationProgressState = MutableStateFlow(PaginationProgressState())
        private set

    var mainProgressState = MutableStateFlow(true)
        private set

    var mainErrorState = MutableStateFlow(ErrorState())
        private set

    fun getPokemonList() {
        viewModelScope.launch {
            useCase.getPokemonListUseCase().collect {
                when(it) {
                    is Resource.Error -> {
                        if (pokemonDTOListState.value.isNotEmpty()){
                            paginationProgressState.value = paginationProgressState.value.copy(
                                isLoading = false,
                                isError = true,
                                errorMessage = it.message
                            )
                        }else {
                            mainErrorState.value = mainErrorState.value.copy(message = it.message)
                            mainProgressState.value = false
                        }

                        firebaseAnalytics.logEvent(LOG_ERROR) {
                            param(LOG_ERROR, "error")
                        }
                    }

                    is Resource.Loading -> {
                        if (pokemonDTOListState.value.isEmpty()) {
                            mainProgressState.value = true
                        }else {
                            paginationProgressState.value = paginationProgressState.value.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        if (it.data != null) {
                            pokemonDTOListState.value = it.data
                        }
                        mainErrorState.value = mainErrorState.value.copy(message = null)
                        mainProgressState.value = false
                        paginationProgressState.value = paginationProgressState.value.copy(
                            isLoading = false,
                            isError = false,
                            errorMessage = null
                        )

                        firebaseAnalytics.logEvent(LOG_SUCCESS) {
                            param(LOG_SUCCESS, "success")
                        }
                    }
                }
            }
        }
    }
}