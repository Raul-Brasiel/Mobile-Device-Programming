package dev.raul.aplicativodecontroledehabitos.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.raul.aplicativodecontroledehabitos.dados.model.DiaCalendario
import dev.raul.aplicativodecontroledehabitos.ui.theme.BorderField
import dev.raul.aplicativodecontroledehabitos.ui.theme.BrandHeaderTeal
import dev.raul.aplicativodecontroledehabitos.ui.theme.SurfaceWhite
import dev.raul.aplicativodecontroledehabitos.ui.theme.TextPrimary
import dev.raul.aplicativodecontroledehabitos.ui.theme.TextSecondary

@Composable
fun BarraSemanal(
    dias: List<DiaCalendario>,
    aoSelecionarDia: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        dias.forEach { dia ->
            val selecionado = dia.estaSelecionado
            val corFundo = if (selecionado) BrandHeaderTeal else SurfaceWhite
            val corTexto = if (selecionado) Color.White else TextPrimary
            val corSubtexto = if (selecionado) Color.White.copy(alpha = 0.8f) else TextSecondary

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 2.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .then(
                        if (!selecionado) {
                            Modifier.border(1.dp, BorderField.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                        } else Modifier
                    )
                    .background(corFundo)
                    .clickable { aoSelecionarDia(dia.dataIso) }
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = dia.diaSemanaAbreviado,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = corSubtexto
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${dia.diaDoMes}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = corTexto
                )
            }
        }
    }
}
