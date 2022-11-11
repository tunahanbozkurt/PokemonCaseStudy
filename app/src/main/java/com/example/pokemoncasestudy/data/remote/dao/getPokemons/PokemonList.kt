package com.example.pokemoncasestudy.data.remote.dao.getPokemons


import com.google.gson.annotations.SerializedName

data class PokemonList(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: Any?,
    @SerializedName("results")
    val results: ArrayList<PokemonDTO?>?
)