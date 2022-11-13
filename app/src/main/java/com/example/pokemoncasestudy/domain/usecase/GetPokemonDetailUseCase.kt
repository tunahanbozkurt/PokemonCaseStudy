package com.example.pokemoncasestudy.domain.usecase

import com.example.pokemoncasestudy.domain.model.PokemonDetail
import com.example.pokemoncasestudy.domain.repository.PokemonDetailRepository
import com.example.pokemoncasestudy.util.Resource
import kotlinx.coroutines.flow.Flow

// This class is over architecture for this app but I wanted to show you domain layer
class GetPokemonDetailUseCase(
    private val pokemonDetailRepository: PokemonDetailRepository
) {

    suspend operator fun invoke(page: String): Flow<Resource<PokemonDetail>> {
        return pokemonDetailRepository.getPokemonDetail(page)
    }
}