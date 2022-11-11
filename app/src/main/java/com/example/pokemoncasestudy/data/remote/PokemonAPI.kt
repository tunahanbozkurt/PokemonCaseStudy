package com.example.pokemoncasestudy.data.remote

import com.example.pokemoncasestudy.data.remote.dao.getPokemons.PokemonListDAO
import com.example.pokemoncasestudy.data.remote.dao.pokemonDetail.PokemonDetailDAO
import com.example.pokemoncasestudy.util.POKEMON_ENDPOINT
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonAPI {

    @GET(POKEMON_ENDPOINT)
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int
    ): Response<PokemonListDAO>

    @GET("$POKEMON_ENDPOINT/{page}/")
    suspend fun getPokemonDetails(
        @Path("page") page: Int
    ): Response<PokemonDetailDAO>
}