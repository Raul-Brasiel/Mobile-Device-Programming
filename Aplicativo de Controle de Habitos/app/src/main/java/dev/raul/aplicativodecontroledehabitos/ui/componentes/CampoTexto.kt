package dev.raul.aplicativodecontroledehabitos.ui.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.raul.aplicativodecontroledehabitos.ui.theme.BorderField
import dev.raul.aplicativodecontroledehabitos.ui.theme.PrimaryTeal
import dev.raul.aplicativodecontroledehabitos.ui.theme.StatusDanger
import dev.raul.aplicativodecontroledehabitos.ui.theme.SurfaceWhite
import dev.raul.aplicativodecontroledehabitos.ui.theme.TextPlaceholder
import dev.raul.aplicativodecontroledehabitos.ui.theme.TextPrimary
import dev.raul.aplicativodecontroledehabitos.ui.theme.TextSecondary

@Composable
fun CampoTexto(
    rotulo: String,
    valor: String,
    aoMudarValor: (String) -> Unit,
    placeholder: String = "",
    modifier: Modifier = Modifier,
    ehSenha: Boolean = false,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    temErro: Boolean = false,
    mensagemErro: String? = null,
    textoAjuda: String? = null,
    linhaUnica: Boolean = true,
    linhasMinimas: Int = 1,
    linhasMaximas: Int = if (linhaUnica) 1 else 4
) {
    var senhaVisivel by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = rotulo,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            ),
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = valor,
            onValueChange = aoMudarValor,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    color = TextPlaceholder,
                    fontSize = 15.sp
                )
            },
            singleLine = linhaUnica,
            minLines = linhasMinimas,
            maxLines = linhasMaximas,
            isError = temErro,
            shape = RoundedCornerShape(14.dp),
            visualTransformation = if (ehSenha && !senhaVisivel) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (ehSenha) KeyboardType.Password else tipoTeclado
            ),
            trailingIcon = if (ehSenha) {
                {
                    val icone = if (senhaVisivel) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val descricao = if (senhaVisivel) "Ocultar senha" else "Mostrar senha"
                    IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                        Icon(imageVector = icone, contentDescription = descricao, tint = TextPlaceholder)
                    }
                }
            } else null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceWhite,
                unfocusedContainerColor = SurfaceWhite,
                focusedBorderColor = PrimaryTeal,
                unfocusedBorderColor = BorderField,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                errorBorderColor = StatusDanger,
                cursorColor = PrimaryTeal
            )
        )

        if (temErro && !mensagemErro.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = mensagemErro,
                color = StatusDanger,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        } else if (!textoAjuda.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = textoAjuda,
                color = TextSecondary,
                fontSize = 12.sp
            )
        }
    }
}
