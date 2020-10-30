package com.jidbaka.pokedex_kotlin.Retrofit

import com.jidbaka.pokedex_kotlin.Model.Pokedex
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface IPokemonList {
    @GET("pokemon")
    fun listPokemon(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0)
    : Observable<Pokedex>
}