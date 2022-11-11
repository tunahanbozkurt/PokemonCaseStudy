package com.example.pokemoncasestudy.domain.repository

import com.example.pokemoncasestudy.domain.model.PokemonDetail
import com.example.pokemoncasestudy.util.Resource
import kotlinx.coroutines.flow.Flow

interface PokemonDetailRepository {

    suspend fun getPokemonDetail(page: String): Flow<Resource<PokemonDetail>>
}