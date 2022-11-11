package com.example.pokemoncasestudy.domain.repository

import com.example.pokemoncasestudy.domain.model.Pokemon
import com.example.pokemoncasestudy.util.Resource
import kotlinx.coroutines.flow.Flow

interface PokemonListRepository {

    suspend fun getPokemonList(): Flow<Resource<ArrayList<Pokemon?>>>
}