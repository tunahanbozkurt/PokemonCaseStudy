package com.example.pokemoncasestudy.data.remote.dao.pokemonDetail


import com.google.gson.annotations.SerializedName

data class GenerationVii(
    @SerializedName("icons")
    val icons: Icons?,
    @SerializedName("ultra-sun-ultra-moon")
    val ultraSunUltraMoon: UltraSunUltraMoon?
)