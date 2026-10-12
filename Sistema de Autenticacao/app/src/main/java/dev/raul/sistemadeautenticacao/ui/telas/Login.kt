package dev.raul.sistemadeautenticacao.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Shield
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
fun Login(
    viewModel: AuthViewModel,
    aoIrParaRegistrar: () -> Unit,
    aoIrParaEsqueceuSenha: () -> Unit
){
    val estado by viewModel.estado.collectAsState()
    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    val gerenciadorFoco = LocalFocusManager.current

    val entrar = {
        gerenciadorFoco.clearFocus()
        viewModel.entrar(email, senha)
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
        Spacer(modifier = Modifier.height(32.dp))
        IconeDestaque(icone = Icons.Outlined.Shield)
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Bem-vindo de volta",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Entre com sua conta para continuar",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoTexto(
            valor = email,
            aoMudarValor = { email = it },
            rotulo = "E-mail",
            icone = Icons.Outlined.Email,
            tipoTeclado = KeyboardType.Email,
            mensagemErro = estado.erroEmail,
            habilitado = !estado.estaCarregando
        )

        CampoTexto(
            valor = senha,
            aoMudarValor = { senha = it },
            rotulo = "Senha",
            icone = Icons.Outlined.Lock,
            ehSenha = true,
            mensagemErro = estado.erroSenha,
            acaoTeclado = ImeAction.Done,
            aoConfirmarTeclado = { entrar() },
            habilitado = !estado.estaCarregando
        )

        estado.mensagemErro?.let{
            MensagemBanner(texto = it, tipo = TipoMensagem.ERRO)
        }

        TextButton(
            onClick = aoIrParaEsqueceuSenha,
            modifier = Modifier.align(Alignment.End),
            enabled = !estado.estaCarregando
        ){
            Text("Esqueci minha senha")
        }

        BotaoComLoading(
            texto = "Entrar",
            textoCarregando = "Entrando…",
            aoClicar = { entrar() },
            estaCarregando = estado.estaCarregando
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Não tem conta?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(onClick = aoIrParaRegistrar, enabled = !estado.estaCarregando){
                Text("Cadastre-se")
            }
        }
    }
}