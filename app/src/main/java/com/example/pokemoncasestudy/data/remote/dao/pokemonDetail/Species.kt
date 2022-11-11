package com.example.pokemoncasestudy.data.remote.dao.pokemonDetail


import com.google.gson.annotations.SerializedName

data class Species(
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?
)