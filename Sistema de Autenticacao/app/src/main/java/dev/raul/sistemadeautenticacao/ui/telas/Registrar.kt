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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
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
import dev.raul.sistemadeautenticacao.ui.componentes.BarraForcaSenha
import dev.raul.sistemadeautenticacao.ui.componentes.BotaoComLoading
import dev.raul.sistemadeautenticacao.ui.componentes.CampoTexto
import dev.raul.sistemadeautenticacao.ui.componentes.MensagemBanner
import dev.raul.sistemadeautenticacao.ui.componentes.TipoMensagem

@Composable
fun Registrar(
    viewModel: AuthViewModel,
    aoVoltar: () -> Unit
){
    val estado by viewModel.estado.collectAsState()
    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var confirmacao by rememberSaveable { mutableStateOf("") }
    val gerenciadorFoco = LocalFocusManager.current

    val cadastrar = {
        gerenciadorFoco.clearFocus()
        viewModel.cadastrar(email, senha, confirmacao)
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

        Text(
            text = "Criar conta",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Leva menos de um minuto",
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
            habilitado = !estado.estaCarregando
        )

        CampoTexto(
            valor = senha,
            aoMudarValor = { senha = it },
            rotulo = "Senha",
            icone = Icons.Outlined.Lock,
            ehSenha = true,
            mensagemErro = estado.erroSenha,
            habilitado = !estado.estaCarregando
        )

        BarraForcaSenha(senha = senha)

        CampoTexto(
            valor = confirmacao,
            aoMudarValor = { confirmacao = it },
            rotulo = "Confirmar senha",
            icone = Icons.Outlined.Lock,
            ehSenha = true,
            mensagemErro = estado.erroConfirmacaoSenha,
            acaoTeclado = ImeAction.Done,
            aoConfirmarTeclado = { cadastrar() },
            habilitado = !estado.estaCarregando
        )

        estado.mensagemErro?.let{
            MensagemBanner(texto = it, tipo = TipoMensagem.ERRO)
        }

        Spacer(modifier = Modifier.height(4.dp))

        BotaoComLoading(
            texto = "Cadastrar",
            textoCarregando = "Criando conta…",
            aoClicar = { cadastrar() },
            estaCarregando = estado.estaCarregando
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Já tem conta?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(onClick = aoVoltar, enabled = !estado.estaCarregando){
                Text("Entrar")
            }
        }
    }
}