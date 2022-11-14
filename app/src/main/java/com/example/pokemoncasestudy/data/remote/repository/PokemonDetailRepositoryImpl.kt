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
            // Connection failure
            // Of course I could use connectivity manager or a function that ping to google dns to check...
            // connection but I think IOException is sufficient for the app.
            catch (e: IOException) {
                emit(Resource.Error(R.string.connection_error))
            }

        }.flowOn(dispatcherIO)
    }
}

// This method can not work stable with this situation;
// If user is connected to wifi or cellular and there is no internet connection
/*
private fun isNetworkAvailable(): Boolean {
    val connectivityManager =
        getSystemService<Any>(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    val activeNetworkInfo = connectivityManager?.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}
 */

// This method is safer than the function above
// can not work on main thread but it is stable and fast
/*
fun isOnline(): Boolean {
    return try {
        val timeoutMs = 1500
        val sock = Socket()
        val sockaddr: SocketAddress = InetSocketAddress("8.8.8.8", 53)
        sock.connect(sockaddr, timeoutMs)
        sock.close()
        true
    } catch (e: IOException) {
        false
    }
}
 */