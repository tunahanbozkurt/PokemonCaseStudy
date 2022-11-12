package com.example.pokemoncasestudy.presentation.main_screen


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemoncasestudy.databinding.FragmentMainScreenBinding
import com.example.pokemoncasestudy.domain.model.Pokemon
import com.example.pokemoncasestudy.presentation.main_screen.adapter.PokemonListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainScreenFragment : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding
    private val viewModel: MainScreenViewModel by viewModels()

    private var pokemonList: ArrayList<Pokemon> = arrayListOf()
    private lateinit var adapter: PokemonListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var isPokemonListEmpty: Boolean = true

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

        binding.paginationTryAgain.setOnClickListener {
            viewModel.getPokemonList()
        }

        binding.tryAgainButton.setOnClickListener {
            viewModel.getPokemonList()
        }
    }


    private fun subscribe() {

        isPokemonListEmpty = viewModel.pokemonDTOListState.value.isEmpty()
        if (isPokemonListEmpty) {
            viewModel.getPokemonList()
        }

        // Best practice to collect multiple flows
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.pokemonDTOListState.collect {
                        it.let {
                            adapter.updateAdapter(it)
                        }
                    }
                }

                launch {
                    viewModel.mainProgressState.collect {
                        binding.mainProgressBar.visibility = if (it) View.VISIBLE else View.GONE
                    }
                }

                launch {
                    viewModel.paginationProgressState.collect {

                        binding.paginationBelow.visibility = if(it.isLoading || it.isError) View.VISIBLE
                        else View.GONE

                        binding.paginationBelowProgressBar.visibility = if (it.isLoading && !it.isError) View.VISIBLE
                        else View.GONE

                        binding.paginationBelowError.visibility = if (it.isError) View.VISIBLE
                        else View.GONE
                    }
                }

                launch {
                    viewModel.mainErrorState.collect {
                        binding.errorMessage.text = if (it.message != null) getString(it.message)
                        else ""
                        binding.connectionError.visibility = if (it.message != null) View.VISIBLE
                        else View.GONE
                    }
                }
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