package com.example.pokemoncasestudy.domain.usecase

import com.example.pokemoncasestudy.domain.model.Pokemon
import com.example.pokemoncasestudy.domain.repository.PokemonListRepository
import com.example.pokemoncasestudy.util.Resource
import kotlinx.coroutines.flow.Flow

// This class is over architecture for this app but I wanted to show you domain layer
class GetPokemonListUseCase(
    private val pokemonListRepository: PokemonListRepository
) {

    suspend operator fun invoke(): Flow<Resource<ArrayList<Pokemon?>>> {
        return pokemonListRepository.getPokemonList()
    }
}