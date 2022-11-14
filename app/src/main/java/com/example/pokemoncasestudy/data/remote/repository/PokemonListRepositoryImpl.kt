package com.example.pokemoncasestudy.data.remote.repository

import com.example.pokemoncasestudy.R
import com.example.pokemoncasestudy.data.remote.network.PokemonAPI
import com.example.pokemoncasestudy.domain.model.Pokemon
import com.example.pokemoncasestudy.domain.repository.PokemonListRepository
import com.example.pokemoncasestudy.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

class PokemonListRepositoryImpl(
    private val api: PokemonAPI,
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO
): PokemonListRepository {

    private var offSet = 0

    override suspend fun getPokemonList(): Flow<Resource<ArrayList<Pokemon?>>> = flow {
        emit(Resource.Loading())

        try {
            val response = api.getPokemonList(offset = offSet)

            if (response.isSuccessful) {
                offSet += 20
                val results = response.body()?.results
                val convertedResults = results?.map { it?.toPokemon() }
                val convertedResultsArray = convertedResults?.let { ArrayList(it) }
                emit(Resource.Success(convertedResultsArray))

            }else {
                emit(Resource.Error(R.string.unexpected_error))
            }
        }
        // Please check the comments in "PokemonDetailRepositoryImpl" to get detailed explanation.
        catch (e: IOException) {
           emit(Resource.Error(R.string.connection_error))
        }

    }.flowOn(dispatcherIO)

}