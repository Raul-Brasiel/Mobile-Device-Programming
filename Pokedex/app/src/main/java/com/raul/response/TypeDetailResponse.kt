package com.raul.response

data class TypeDetailResponse(
    val pokemon: List<TypePokemon>
)

data class TypePokemon(
    val pokemon: PokemonListItem
)
