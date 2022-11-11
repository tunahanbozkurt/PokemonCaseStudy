package com.example.pokemoncasestudy.presentation.main_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemoncasestudy.databinding.FragmentMainScreenBinding
import com.example.pokemoncasestudy.domain.model.Pokemon
import com.example.pokemoncasestudy.presentation.main_screen.adapter.PokemonListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenFragment : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding
    private val viewModel: MainScreenViewModel by viewModels()

    private var pokemonList: ArrayList<Pokemon> = arrayListOf()
    lateinit var adapter: PokemonListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScreenBinding.inflate(inflater, container, false)

        prepareView()
        subscribe()

        return binding.root
    }

    private fun prepareView() {
        binding.pokemonRecycler.layoutManager = LinearLayoutManager(activity)
        adapter = PokemonListAdapter(pokemonList) {
            val action = MainScreenFragmentDirections.actionMainScreenFragmentToPokemonDetailScreenFragment(it)
            findNavController().navigate(action)


        }
        binding.pokemonRecycler.adapter = adapter
    }

    private fun subscribe() {
        viewModel.getPokemonList()
        lifecycleScope.launchWhenStarted {
            viewModel.pokemonDTOListState.collect {
               it.let {
                   adapter.updateAdapter(it)
               }
            }
        }
    }
}