package dev.raul.aplicativodecontroledehabitos.dados.model

import android.graphics.Color.parseColor
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

data class CorHabito(
    val nome: String,
    val hex: String,
    val hexFundo: String
) {
    val cor: Color get() = Color(hex.toColorInt())
    val corFundo: Color get() = Color(hexFundo.toColorInt())
}

val CoresHabitoDisponiveis = listOf(
    CorHabito("Azul", "#0088A9", "#E0F2FE"),
    CorHabito("Roxo", "#6D28D9", "#EDE9FE"),
    CorHabito("Laranja", "#C2410C", "#FFEDD5"),
    CorHabito("Verde", "#15803D", "#DCFCE7"),
    CorHabito("Mostarda", "#A16207", "#FEF9C3")
)
