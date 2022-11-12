package com.example.pokemoncasestudy.presentation.main_screen


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemoncasestudy.databinding.FragmentMainScreenBinding
import com.example.pokemoncasestudy.domain.model.Pokemon
import com.example.pokemoncasestudy.presentation.main_screen.adapter.PokemonListAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainScreenFragment : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding
    private val viewModel: MainScreenViewModel by viewModels()

    private var pokemonList: ArrayList<Pokemon> = arrayListOf()
    private lateinit var adapter: PokemonListAdapter
    private lateinit var layoutManager: LinearLayoutManager

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

        setUpRecyclerViewAdapter()

        // Contains business logic to detect whether user scrolled to the last item of recyclerview or not
        binding.pokemonRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var isScrolling = false

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisible = layoutManager.findFirstVisibleItemPosition()

                val isAtLastItem = firstVisible + visibleItemCount >= totalItemCount
                val isNotAtBeginning = firstVisible >= 0
                val isTotalMoreThanVisible = totalItemCount >= 20
                val shouldPaginate = isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

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

        binding.tryAgainButton.setOnClickListener {
            subscribe()
        }
    }


    private fun subscribe() {

        if (!viewModel.isOnline()) {
            binding.connectionError.visibility = View.VISIBLE
            // return means do not send getPokemonList() request and the others, if there is no internet connection
            return
        }else {
            binding.connectionError.visibility = View.GONE
        }

        viewModel.getPokemonList()
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

    /**
     * Setups the adapter
     */
    private fun setUpRecyclerViewAdapter() {
        layoutManager = LinearLayoutManager(activity)
        adapter = PokemonListAdapter(pokemonList) {
            val action = MainScreenFragmentDirections.actionMainScreenFragmentToPokemonDetailScreenFragment(it)
            findNavController().navigate(action)
        }
        binding.pokemonRecycler.layoutManager = layoutManager
        binding.pokemonRecycler.adapter = adapter
    }
}