package com.example.pokemoncasestudy.data.remote.dao.pokemonDetail


import com.google.gson.annotations.SerializedName

data class Generationiii(
    @SerializedName("emerald")
    val emerald: Emerald?,
    @SerializedName("firered-leafgreen")
    val fireredLeafgreen: FireredLeafgreen?,
    @SerializedName("ruby-sapphire")
    val rubySapphire: RubySapphire?
)