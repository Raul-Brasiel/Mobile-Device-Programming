package dev.raul.aplicativodecontroledehabitos.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.raul.aplicativodecontroledehabitos.dados.model.CoresHabitoDisponiveis

fun obterVetorIconeHabito(nomeIcone: String): ImageVector {
    return when (nomeIcone.lowercase()) {
        "water" -> Icons.Default.WaterDrop
        "book" -> Icons.AutoMirrored.Filled.MenuBook
        "fitness" -> Icons.Default.FitnessCenter
        "study" -> Icons.Default.Description
        else -> Icons.Default.Star
    }
}

fun obterCorFundoHabito(corHex: String): Color {
    val encontrada = CoresHabitoDisponiveis.find { it.hex.equals(corHex, ignoreCase = true) }
    return encontrada?.corFundo ?: Color(0xFFE0F2FE)
}

fun obterCorPrincipalHabito(corHex: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(corHex))
    } catch (_: Exception) {
        Color(0xFF0088A9)
    }
}

@Composable
fun IconeHabito(
    nomeIcone: String,
    corHex: String,
    modifier: Modifier = Modifier,
    tamanhoContainer: Dp = 48.dp,
    tamanhoIcone: Dp = 24.dp
) {
    val corFundo = obterCorFundoHabito(corHex)
    val corIcone = obterCorPrincipalHabito(corHex)
    val vetorIcone = obterVetorIconeHabito(nomeIcone)

    Box(
        modifier = modifier
            .size(tamanhoContainer)
            .background(color = corFundo, shape = RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = vetorIcone,
            contentDescription = null,
            tint = corIcone,
            modifier = Modifier.size(tamanhoIcone)
        )
    }
}
