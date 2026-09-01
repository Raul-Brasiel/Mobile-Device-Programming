package com.raul.api

import com.raul.response.PokemonDetailResponse
import com.raul.response.PokemonListResponse
import com.raul.response.TypeDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemons(
        @Query("limit") limit: Int = 30,
        @Query("offset") offset: Int = 0
    ): Response<PokemonListResponse>

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") idOrName: String
    ): Response<PokemonDetailResponse>

    @GET("type/{idOrName}")
    suspend fun getTypeDetail(
        @Path("idOrName") idOrName: String
    ): Response<TypeDetailResponse>
}