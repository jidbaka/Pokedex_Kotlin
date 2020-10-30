package com.jidbaka.pokedex_kotlin.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jidbaka.pokedex_kotlin.R
import com.jidbaka.pokedex_kotlin.Model.Pokemon

class PokemonListAdapter
    (internal var context: Context ,
     internal  var items : ArrayList<Pokemon>
):RecyclerView.Adapter<PokemonListAdapter.MyViewHolder>(){


    var URL:String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val itemView = LayoutInflater.from(context).inflate(R.layout.pokemon_list_item,parent,false)
        return  MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(URL+(position+1).toString()+".png").into(holder.imgPokemon)
        holder.txtPokemon.text = items[position].name
    }

    fun addPokemonList() {
        val previous = items.size
        items.addAll(items)
        notifyItemRangeChanged(previous, items.size)
    }

    override fun getItemCount(): Int {
        return items.size
    }

     class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        internal var imgPokemon:ImageView = itemView.findViewById(R.id.pokemon_image) as ImageView
        internal var txtPokemon:TextView = itemView.findViewById(R.id.txt_pokemon_name) as TextView

    }

}

