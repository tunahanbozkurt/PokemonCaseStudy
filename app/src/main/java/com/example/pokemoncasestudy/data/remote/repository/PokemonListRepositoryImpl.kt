package com.example.pokemoncasestudy.data.remote.repository

import com.example.pokemoncasestudy.data.remote.PokemonAPI
import com.example.pokemoncasestudy.data.remote.dao.getPokemons.Pokemon
import com.example.pokemoncasestudy.domain.repository.PokemonListRepository
import com.example.pokemoncasestudy.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class PokemonListRepositoryImpl(
    private val api: PokemonAPI,
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO
): PokemonListRepository {

    private var offSet = 0
    private val pokemonList = arrayListOf<Pokemon>()

    override suspend fun getPokemonList(): Flow<Resource<ArrayList<Pokemon>>> = flow {
        emit(Resource.Loading<ArrayList<Pokemon>>())
        try {
            val response = api.getPokemonList(offset = offSet)
            if (response.isSuccessful) {
                offSet += 20
                val results = response.body()?.results
                results?.let { pokeList ->
                    pokeList.forEach { pokemon ->
                        if (pokemon != null) {
                            pokemonList.add(pokemon)
                        }
                    }
                }

                emit(Resource.Success(pokemonList))
            }else {

            }
        }
        catch (e: HttpException) {
            emit(Resource.Error(e.code().toString()))
        }
        catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }

    }.flowOn(dispatcherIO)

}