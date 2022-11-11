package com.example.pokemoncasestudy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemoncasestudy.data.remote.PokemonAPI
import com.example.pokemoncasestudy.databinding.ActivityMainBinding
import com.example.pokemoncasestudy.domain.repository.PokemonListRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}