package com.example.pokemoncasestudy.data.remote.dao.getPokemons


import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?
)