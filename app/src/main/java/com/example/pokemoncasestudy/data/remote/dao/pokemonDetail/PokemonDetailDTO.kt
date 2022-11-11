package com.example.pokemoncasestudy.data.remote.dao.pokemonDetail


import com.example.pokemoncasestudy.domain.model.PokemonDetail
import com.google.gson.annotations.SerializedName

data class PokemonDetailDTO(
    @SerializedName("abilities")
    val abilities: List<Ability>?,
    @SerializedName("base_experience")
    val baseExperience: Int?,
    @SerializedName("forms")
    val forms: List<Form>?,
    @SerializedName("game_indices")
    val gameIndices: List<Gameİndice>?,
    @SerializedName("height")
    val height: Int?,
    @SerializedName("held_items")
    val heldItems: List<Any>?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("is_default")
    val isDefault: Boolean?,
    @SerializedName("location_area_encounters")
    val locationAreaEncounters: String?,
    @SerializedName("moves")
    val moves: List<Move>?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("order")
    val order: Int?,
    @SerializedName("past_types")
    val pastTypes: List<Any>?,
    @SerializedName("species")
    val species: Species?,
    @SerializedName("sprites")
    val sprites: Sprites?,
    @SerializedName("stats")
    val stats: List<Stat>?,
    @SerializedName("types")
    val types: List<Type>?,
    @SerializedName("weight")
    val weight: Int?
) {

    fun toPokemonDetail(): PokemonDetail {
        return PokemonDetail(
            name = name,
            height = height.toString(),
            weight = weight.toString(),
            backImgUrl = sprites?.backDefault,
            frontImgUrl = sprites?.frontDefault
        )
    }
}