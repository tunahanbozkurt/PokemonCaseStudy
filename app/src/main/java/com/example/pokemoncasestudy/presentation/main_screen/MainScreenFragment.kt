package com.example.pokemoncasestudy.presentation.main_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemoncasestudy.databinding.FragmentMainScreenBinding
import com.example.pokemoncasestudy.domain.model.Pokemon
import com.example.pokemoncasestudy.presentation.main_screen.adapter.PokemonListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.concurrent.fixedRateTimer

@AndroidEntryPoint
class MainScreenFragment : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding
    private val viewModel: MainScreenViewModel by viewModels()

    private var pokemonList: ArrayList<Pokemon> = arrayListOf()
    lateinit var adapter: PokemonListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    var isScrolling: Boolean = false

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
        layoutManager = LinearLayoutManager(activity)
        adapter = PokemonListAdapter(pokemonList) {
            val action = MainScreenFragmentDirections.actionMainScreenFragmentToPokemonDetailScreenFragment(it)
            findNavController().navigate(action)
        }
        binding.pokemonRecycler.layoutManager = layoutManager
        binding.pokemonRecycler.adapter = adapter

        binding.pokemonRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisible = layoutManager.findFirstVisibleItemPosition()

                val isAtLastItem = firstVisible + visibleItemCount >= totalItemCount
                val isNotAtBeginnig = firstVisible >= 0
                val isTotalMoreThanVisible = totalItemCount >= 20
                val shouldPaginate = isAtLastItem && isNotAtBeginnig && isTotalMoreThanVisible && isScrolling
                if (shouldPaginate){
                    viewModel.getPokemonList()
                    isScrolling = false
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isScrolling = true
            }
        })
    }


    private fun subscribe() {
        if (layoutManager.itemCount == 0){
            viewModel.getPokemonList()
        }
        lifecycleScope.launchWhenStarted {
            viewModel.pokemonDTOListState.collect {
               it.let {
                   adapter.updateAdapter(it)
               }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loadingState.collect {
                binding.progressBar.visibility = if (it && layoutManager.itemCount == 0) View.VISIBLE else View.GONE
                binding.paginationProgress.visibility = if (it && layoutManager.itemCount != 0) View.VISIBLE else View.GONE
            }
        }
    }
}