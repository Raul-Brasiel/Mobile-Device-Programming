package com.raul.response

data class PokemonListResponse(
    val results: List<PokemonListItem>
)
data class PokemonListItem(
    val name: String,
    val url: String
)