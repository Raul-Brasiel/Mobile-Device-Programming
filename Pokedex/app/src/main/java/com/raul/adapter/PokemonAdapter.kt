package com.raul.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raul.R
import com.raul.response.PokemonDetailResponse

class PokemonAdapter(private var pokemonList: List<PokemonDetailResponse>) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>(){

    class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imgPokemon: ImageView = view.findViewById(R.id.imgPokemon)
        val txtPokemonName: TextView = view.findViewById(R.id.txtPokemonName)
        val cvType1: CardView = view.findViewById(R.id.cvType1)
        val txtType1: TextView = view.findViewById(R.id.txtType1)
        val cvType2: CardView = view.findViewById(R.id.cvType2)
        val txtType2: TextView = view.findViewById(R.id.txtType2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList[position]

        val idFormatted = String.format("#%03d", pokemon.id)
        val nameFormatted = pokemon.name.replaceFirstChar { it.uppercase() }
        holder.txtPokemonName.text = "$idFormatted $nameFormatted"

        if (pokemon.types.isNotEmpty()) {
            holder.cvType1.visibility = View.VISIBLE
            holder.txtType1.text = pokemon.types[0].type.name.replaceFirstChar { it.uppercase() }
        } else {
            holder.cvType1.visibility = View.INVISIBLE
        }

        if (pokemon.types.size > 1) {
            holder.cvType2.visibility = View.VISIBLE
            holder.txtType2.text = pokemon.types[1].type.name.replaceFirstChar { it.uppercase() }
        } else {
            holder.cvType2.visibility = View.INVISIBLE
        }

        val imageUrl = pokemon.sprites.other?.officialArtwork?.frontDefault
        if (imageUrl != null) {
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .into(holder.imgPokemon)
        }
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<PokemonDetailResponse>) {
        pokemonList = newList
        notifyDataSetChanged()
    }
}