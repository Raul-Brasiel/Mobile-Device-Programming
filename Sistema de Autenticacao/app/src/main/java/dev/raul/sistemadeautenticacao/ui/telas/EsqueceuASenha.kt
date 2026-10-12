package dev.raul.sistemadeautenticacao.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.raul.sistemadeautenticacao.auth.AuthViewModel
import dev.raul.sistemadeautenticacao.ui.componentes.BotaoComLoading
import dev.raul.sistemadeautenticacao.ui.componentes.CampoTexto
import dev.raul.sistemadeautenticacao.ui.componentes.IconeDestaque
import dev.raul.sistemadeautenticacao.ui.componentes.MensagemBanner
import dev.raul.sistemadeautenticacao.ui.componentes.TipoMensagem

@Composable
fun EsqueceuASenha(
    viewModel: AuthViewModel,
    aoVoltar: () -> Unit
){
    val estado by viewModel.estado.collectAsState()
    var email by rememberSaveable { mutableStateOf("") }
    val gerenciadorFoco = LocalFocusManager.current
    val enviar = {
        gerenciadorFoco.clearFocus()
        viewModel.recuperarSenha(email)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        IconButton(
            onClick = aoVoltar,
            enabled = !estado.estaCarregando
        ){
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Voltar",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconeDestaque(icone = Icons.Outlined.Key)

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Recuperar senha",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Enviaremos um link de redefinição para o seu e-mail.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        CampoTexto(
            valor = email,
            aoMudarValor = { email = it },
            rotulo = "E-mail",
            icone = Icons.Outlined.Email,
            tipoTeclado = KeyboardType.Email,
            mensagemErro = estado.erroEmail,
            acaoTeclado = ImeAction.Done,
            aoConfirmarTeclado = { enviar() },
            habilitado = !estado.estaCarregando
        )

        estado.mensagemErro?.let{
            MensagemBanner(texto = it, tipo = TipoMensagem.ERRO)
        }
        estado.mensagemInfo?.let{
            MensagemBanner(texto = it, tipo = TipoMensagem.SUCESSO)
        }

        BotaoComLoading(
            texto = "Enviar link",
            textoCarregando = "Enviando…",
            aoClicar = { enviar() },
            estaCarregando = estado.estaCarregando
        )

        TextButton(
            onClick = aoVoltar,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            enabled = !estado.estaCarregando
        ){
            Text("Voltar ao login")
        }
    }
}