package com.example.pokemoncasestudy.domain.repository

import com.example.pokemoncasestudy.data.remote.dao.getPokemons.Pokemon
import com.example.pokemoncasestudy.data.remote.dao.getPokemons.PokemonListDAO
import com.example.pokemoncasestudy.util.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface PokemonListRepository {

    suspend fun getPokemonList(): Flow<Resource<ArrayList<Pokemon>>>
}