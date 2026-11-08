package dev.raul.aplicativodecontroledehabitos.ui.componentes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.raul.aplicativodecontroledehabitos.ui.theme.PrimaryTeal

@Composable
fun BotaoPrincipal(
    texto: String,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier,
    estaCarregando: Boolean = false,
    habilitado: Boolean = true
) {
    Button(
        onClick = aoClicar,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = habilitado && !estaCarregando,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryTeal,
            contentColor = Color.White,
            disabledContainerColor = PrimaryTeal.copy(alpha = 0.6f),
            disabledContentColor = Color.White.copy(alpha = 0.8f)
        )
    ) {
        if (estaCarregando) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.White,
                strokeWidth = 2.5.dp
            )
        } else {
            Text(
                text = texto,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
