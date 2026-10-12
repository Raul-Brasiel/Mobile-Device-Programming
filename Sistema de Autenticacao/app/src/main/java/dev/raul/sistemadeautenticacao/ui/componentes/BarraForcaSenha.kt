package dev.raul.sistemadeautenticacao.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.raul.sistemadeautenticacao.ui.theme.Aviso
import dev.raul.sistemadeautenticacao.ui.theme.Erro
import dev.raul.sistemadeautenticacao.ui.theme.Sucesso

fun calcularForcaSenha(senha: String): Int{
    var pontos = 0
    if (senha.length >= 8) pontos++
    if (senha.any { it.isUpperCase() }) pontos++
    if (senha.any { it.isDigit() }) pontos++
    if (senha.any { !it.isLetterOrDigit() }) pontos++

    return if (senha.length < 6) minOf(pontos, 1) else pontos
}

@Composable
fun BarraForcaSenha(
    senha: String,
    modifier: Modifier = Modifier
) {
    if (senha.isEmpty()) return

    val nivel = calcularForcaSenha(senha)
    val cor = when {
        nivel <= 1 -> Erro
        nivel == 2 -> Aviso
        else -> Sucesso
    }
    val texto = when (nivel){
        0, 1 -> "Senha fraca. Use 8+ caracteres, maiúscula, número e símbolo."
        2 -> "Senha média. Adicione mais variedade."
        3 -> "Senha boa."
        else -> "Senha forte."
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ){
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)){
            repeat(4){ indice ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            if (indice < nivel) cor else MaterialTheme.colorScheme.outline
                        )
                )
            }
        }
        Text(
            text = texto,
            style = MaterialTheme.typography.bodySmall,
            color = cor
        )
    }
}