package dev.raul.aplicativodecontroledehabitos.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import dev.raul.aplicativodecontroledehabitos.dados.model.ListaOpcoesDiasSemana
import dev.raul.aplicativodecontroledehabitos.ui.theme.BorderField
import dev.raul.aplicativodecontroledehabitos.ui.theme.PrimaryTeal
import dev.raul.aplicativodecontroledehabitos.ui.theme.SurfaceWhite
import dev.raul.aplicativodecontroledehabitos.ui.theme.TextPrimary

@Composable
fun SeletorDias(
    diasSelecionados: List<Int>,
    aoAlternarDia: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Repetir nos dias",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ListaOpcoesDiasSemana.forEach { opcao ->
                val selecionado = diasSelecionados.contains(opcao.diaCalendario)
                val corFundo = if (selecionado) PrimaryTeal else SurfaceWhite
                val corTexto = if (selecionado) Color.White else TextPrimary

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .then(
                            if (!selecionado) {
                                Modifier.border(1.5.dp, BorderField, RoundedCornerShape(12.dp))
                            } else Modifier
                        )
                        .background(corFundo)
                        .clickable { aoAlternarDia(opcao.diaCalendario) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = opcao.letra,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = corTexto
                    )
                }
            }
        }
    }
}
