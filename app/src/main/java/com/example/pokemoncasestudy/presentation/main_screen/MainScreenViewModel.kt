package com.example.pokemoncasestudy.presentation.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemoncasestudy.domain.model.Pokemon
import com.example.pokemoncasestudy.domain.repository.PokemonListRepository
import com.example.pokemoncasestudy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor (
    private val listRepo: PokemonListRepository
) : ViewModel() {

    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO

    var pokemonDTOListState = MutableStateFlow<ArrayList<Pokemon?>>(arrayListOf())
        private set

    var loadingState = MutableStateFlow(true)
        private set


    fun getPokemonList() {
        viewModelScope.launch {
            listRepo.getPokemonList().collect {
                when(it) {
                    is Resource.Error -> {
                        loadingState.value = false
                    }
                    is Resource.Loading -> {
                        loadingState.value = true
                    }
                    is Resource.Success -> {
                        it.data?.let { pokemonArray ->
                            pokemonDTOListState.value = pokemonArray
                            loadingState.value = false
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks connection by sending ping to google servers.
     * Very fast and reliable
     */
     fun isOnline(): Boolean {
        var isOnline: Boolean
        runBlocking(dispatcherIO) {
             try {
                 val timeoutMs = 1500
                 val sock = Socket()
                 val sockaddress: SocketAddress = InetSocketAddress("8.8.8.8", 53)
                 sock.connect(sockaddress, timeoutMs)
                 sock.close()
                 isOnline = true

            } catch (e: IOException) {
               isOnline = false
            }
        }
        return isOnline
    }
}