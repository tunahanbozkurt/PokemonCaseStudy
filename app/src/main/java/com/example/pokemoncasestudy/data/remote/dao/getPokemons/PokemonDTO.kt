package com.example.pokemoncasestudy.data.remote.dao.getPokemons


import com.example.pokemoncasestudy.domain.model.Pokemon
import com.google.gson.annotations.SerializedName

data class PokemonDTO(
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?
) {

    fun toPokemon(): Pokemon {
        val page = url?.substring(url.indexOf("pokemon") + 8, url.length - 1)
        return Pokemon(
            name = name,
            page = page
        )
    }
}