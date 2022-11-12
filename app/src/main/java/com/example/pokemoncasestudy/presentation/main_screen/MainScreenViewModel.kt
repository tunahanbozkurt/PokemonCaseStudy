package com.example.pokemoncasestudy.presentation.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemoncasestudy.domain.model.Pokemon
import com.example.pokemoncasestudy.domain.repository.PokemonListRepository
import com.example.pokemoncasestudy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor (
    private val listRepo: PokemonListRepository,
) : ViewModel() {

    var pokemonDTOListState = MutableStateFlow<ArrayList<Pokemon?>>(arrayListOf())
        private set

    var loadingState = MutableStateFlow(true)
        private set

    fun getPokemonList() {
        viewModelScope.launch {
            listRepo.getPokemonList().collect {
                when(it) {
                    is Resource.Error -> {
                        loadingState.value = false
                    }
                    is Resource.Loading -> {
                        loadingState.value = true
                    }
                    is Resource.Success -> {
                        it.data?.let {
                            pokemonDTOListState.value = it
                            loadingState.value = false
                        }
                    }
                }
            }
        }
    }
}