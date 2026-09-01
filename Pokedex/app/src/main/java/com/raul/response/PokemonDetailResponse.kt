package com.raul.response

import com.google.gson.annotations.SerializedName

data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val types: List<TypeSlot>,
    val sprites: Sprites
)
data class TypeSlot(
    val slot: Int,
    val type: TypeInfo
)
data class TypeInfo(
    val name: String
)
data class Sprites(
    val other: OtherSprites
)
data class OtherSprites(
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork
)
data class OfficialArtwork(
    @SerializedName("front_default")
    val frontDefault: String
)