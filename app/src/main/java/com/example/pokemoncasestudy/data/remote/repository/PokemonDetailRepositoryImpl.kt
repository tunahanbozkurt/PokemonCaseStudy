package com.example.pokemoncasestudy.data.remote.repository

import com.example.pokemoncasestudy.R
import com.example.pokemoncasestudy.data.remote.network.PokemonAPI
import com.example.pokemoncasestudy.domain.model.PokemonDetail
import com.example.pokemoncasestudy.domain.repository.PokemonDetailRepository
import com.example.pokemoncasestudy.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

class PokemonDetailRepositoryImpl(
    private val api: PokemonAPI,
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO
): PokemonDetailRepository {

    override suspend fun getPokemonDetail(page: String): Flow<Resource<PokemonDetail>> {
        return flow {
            emit(Resource.Loading())

            try {
                val result =  api.getPokemonDetails(page)
                if (result.isSuccessful) {
                  emit(Resource.Success(result.body()?.toPokemonDetail()))
                }else {
                    emit(Resource.Error(R.string.unexpected_error))
                }
            }
            catch (e: IOException) {
                emit(Resource.Error(R.string.connection_error))
            }

        }.flowOn(dispatcherIO)
    }
}