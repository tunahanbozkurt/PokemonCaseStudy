package com.example.pokemoncasestudy.data.remote.dao.pokemonDetail


import com.google.gson.annotations.SerializedName

data class Versions(
    @SerializedName("generation-v")
    val generationV: GenerationV?,
    @SerializedName("generation-vi")
    val generationVi: GenerationVi?,
    @SerializedName("generation-vii")
    val generationVii: GenerationVii?,
    @SerializedName("generation-viii")
    val generationViii: GenerationViii?,
    @SerializedName("generation-i")
    val generationİ: Generationi?,
    @SerializedName("generation-ii")
    val generationİi: Generationii?,
    @SerializedName("generation-iii")
    val generationİii: Generationiii?,
    @SerializedName("generation-iv")
    val generationİv: Generationİv?
)