package dev.raul.sistemadeautenticacao.ui.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.raul.sistemadeautenticacao.ui.theme.SistemaDeAutenticacaoTheme

@Composable
fun CampoTexto(
    valor: String,
    aoMudarValor: (String) -> Unit,
    rotulo: String,
    modifier: Modifier = Modifier,
    icone: ImageVector? = null,
    ehSenha: Boolean = false,
    mensagemErro: String? = null,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    acaoTeclado: ImeAction = ImeAction.Next,
    aoConfirmarTeclado: () -> Unit = {},
    habilitado: Boolean = true
) {
    var senhaVisivel by remember { mutableStateOf(false) }
    val temErro = mensagemErro != null

    OutlinedTextField(
        value = valor,
        onValueChange = aoMudarValor,
        modifier = modifier.fillMaxWidth(),
        label = { Text(rotulo) },
        leadingIcon = if (icone != null) {
            { Icon(imageVector = icone, contentDescription = null) }
        } else null,
        trailingIcon = if (ehSenha) {
            {
                IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                    Icon(
                        imageVector = if (senhaVisivel) Icons.Outlined.VisibilityOff
                        else Icons.Outlined.Visibility,
                        contentDescription = if (senhaVisivel) "Ocultar senha" else "Mostrar senha"
                    )
                }
            }
        } else null,
        isError = temErro,
        supportingText = if (mensagemErro != null) {
            { Text(mensagemErro) }
        } else null,
        singleLine = true,
        enabled = habilitado,
        visualTransformation = if (ehSenha && !senhaVisivel) PasswordVisualTransformation()
        else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (ehSenha) KeyboardType.Password else tipoTeclado,
            imeAction = acaoTeclado
        ),
        keyboardActions = KeyboardActions(onDone = { aoConfirmarTeclado() }),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            errorContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1115)
@Composable
private fun PreviaCampoTexto() {
    SistemaDeAutenticacaoTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            CampoTexto(
                valor = "raul@email.com",
                aoMudarValor = {},
                rotulo = "E-mail",
                icone = Icons.Outlined.Email,
                tipoTeclado = KeyboardType.Email
            )
            CampoTexto(
                valor = "123456",
                aoMudarValor = {},
                rotulo = "Senha",
                icone = Icons.Outlined.Lock,
                ehSenha = true,
                mensagemErro = "A senha deve ter pelo menos 6 caracteres",
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}