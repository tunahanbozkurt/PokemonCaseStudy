package com.example.pokemoncasestudy.presentation.main_screen

import androidx.lifecycle.ViewModel
import com.example.pokemoncasestudy.data.remote.dao.getPokemons.Pokemon
import com.example.pokemoncasestudy.domain.repository.PokemonListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor (
    private val repo: PokemonListRepository
) : ViewModel() {

    var pokemonListState = MutableStateFlow(arrayListOf<Pokemon>())
        private set

    fun collectPokemons() {

    }

}