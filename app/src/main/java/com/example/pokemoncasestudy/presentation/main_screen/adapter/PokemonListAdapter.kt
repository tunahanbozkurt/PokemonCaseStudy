package com.example.pokemoncasestudy.presentation.main_screen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemoncasestudy.R
import com.example.pokemoncasestudy.domain.model.Pokemon

class PokemonListAdapter(private val pokemonList: ArrayList<Pokemon>, private val onClick: (String?) -> Unit): RecyclerView.Adapter<PokemonListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val name = itemView.findViewById<TextView>(R.id.pokemon_name)
        val row = itemView.findViewById<LinearLayout>(R.id.pokemon_list_row)

        fun bind(pokemon: Pokemon, onClick: (String?) -> Unit) {
            name.text = pokemon.name
            row.setOnClickListener { onClick(pokemon.page) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_pokemon_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pokemonList[position], onClick)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    fun updateAdapter(newList: ArrayList<Pokemon?>) {
        newList.forEach {
            it?.let { pokemonList.add(it) }
        }
        notifyItemRangeInserted(pokemonList.lastIndex, newList.size)
    }
}