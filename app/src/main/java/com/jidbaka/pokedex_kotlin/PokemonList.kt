package com.jidbaka.pokedex_kotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableBoolean
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jidbaka.pokedex_kotlin.Adapter.PokemonListAdapter
import com.jidbaka.pokedex_kotlin.Common.Common
import com.jidbaka.pokedex_kotlin.Common.ItemOffsetDecoration
import com.jidbaka.pokedex_kotlin.Retrofit.IPokemonList
import com.jidbaka.pokedex_kotlin.Retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class PokemonList : Fragment() {

    private var compositeDisposable = CompositeDisposable()
    private var iPokemonList:IPokemonList
    var condition:Boolean=false
    private lateinit var recyclerView: RecyclerView
    var offset:Int = 0

    init {
        val retrofit = RetrofitClient.instance
        iPokemonList=retrofit.create(IPokemonList::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val itemView = inflater.inflate(R.layout.fragment_blank, container, false)
        recyclerView = itemView.findViewById(R.id.pokemon_recyclerview) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(activity,3)
        val itemDecoration = ItemOffsetDecoration(activity!!,R.dimen.spacing)
        recyclerView.addItemDecoration(itemDecoration)

        recyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager
                layoutManager?.let{
                    val visibleItemCount:Int = it.childCount
                    val totalItemCount = it.itemCount
                    val firstVisibleItemPosition = when (layoutManager) {
                        is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
                        is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
                        else -> return
                    }

                    if (condition) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                            offset += 40
                            condition = false
                            fetchData(offset)
                        }
                    }
                }
            }
        })
        condition = true
        fetchData(offset)
        return itemView
    }

    private fun fetchData(offset:Int) {
        compositeDisposable.add(iPokemonList.listPokemon(40,offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{pokemonDex ->
                Common.pokemonList = pokemonDex.results!!
                val adapter = PokemonListAdapter(activity!!, Common.pokemonList)
                condition = true
                recyclerView.adapter =adapter
                adapter.addPokemonList()

            }
        )
    }


}