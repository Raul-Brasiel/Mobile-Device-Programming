package dev.raul.aplicativodecontroledehabitos.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.raul.aplicativodecontroledehabitos.dados.model.Habito
import dev.raul.aplicativodecontroledehabitos.ui.theme.BorderField
import dev.raul.aplicativodecontroledehabitos.ui.theme.PrimaryTeal
import dev.raul.aplicativodecontroledehabitos.ui.theme.SurfaceWhite
import dev.raul.aplicativodecontroledehabitos.ui.theme.TextPrimary
import dev.raul.aplicativodecontroledehabitos.ui.theme.TextSecondary

@Composable
fun CardHabito(
    habito: Habito,
    foiConcluido: Boolean,
    sequenciaDias: Int,
    aoAlternarConclusao: () -> Unit,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = aoClicar),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = CardDefaults.outlinedCardBorder(enabled = true)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconeHabito(
                nomeIcone = habito.nomeIcone,
                corHex = habito.corHex,
                tamanhoContainer = 52.dp,
                tamanhoIcone = 26.dp
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habito.nome,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                val subtitulo = buildString {
                    append(habito.frequenciaFormatada())
                    if (sequenciaDias > 0) {
                        append(" • $sequenciaDias dia${if (sequenciaDias > 1) "s" else ""}")
                    }
                }

                Text(
                    text = subtitulo,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable(onClick = aoAlternarConclusao)
                    .then(
                        if (foiConcluido) {
                            Modifier.background(PrimaryTeal, CircleShape)
                        } else {
                            Modifier.border(2.dp, BorderField, CircleShape)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (foiConcluido) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Concluído",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
