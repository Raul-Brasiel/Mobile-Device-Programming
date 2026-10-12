package dev.raul.sistemadeautenticacao.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.raul.sistemadeautenticacao.ui.theme.Erro
import dev.raul.sistemadeautenticacao.ui.theme.Sucesso

enum class TipoMensagem{ ERRO, SUCESSO }

@Composable
fun MensagemBanner(
    texto: String,
    tipo: TipoMensagem,
    modifier: Modifier = Modifier
){
    val cor = if (tipo == TipoMensagem.ERRO) Erro else Sucesso
    val icone = if (tipo == TipoMensagem.ERRO) Icons.Outlined.ErrorOutline
    else Icons.Outlined.CheckCircle

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(cor.copy(alpha = 0.12f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Icon(
            imageVector = icone,
            contentDescription = null,
            tint = cor,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium,
            color = cor
        )
    }
}