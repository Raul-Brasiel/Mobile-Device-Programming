package dev.raul.aplicativodecontroledehabitos.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.raul.aplicativodecontroledehabitos.auth.AuthViewModel
import dev.raul.aplicativodecontroledehabitos.ui.componentes.BotaoPrincipal
import dev.raul.aplicativodecontroledehabitos.ui.componentes.CampoTexto
import dev.raul.aplicativodecontroledehabitos.ui.theme.BackgroundMint
import dev.raul.aplicativodecontroledehabitos.ui.theme.BrandHeaderTeal
import dev.raul.aplicativodecontroledehabitos.ui.theme.PrimaryTeal
import dev.raul.aplicativodecontroledehabitos.ui.theme.StatusDanger
import dev.raul.aplicativodecontroledehabitos.ui.theme.StatusDangerBg
import dev.raul.aplicativodecontroledehabitos.ui.theme.TextSecondary

@Composable
fun Login(
    viewModel: AuthViewModel,
    aoIrParaRegistrar: () -> Unit,
    aoIrParaEsqueceuSenha: () -> Unit,
    aoLoginSucesso: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()

    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.limparMensagens()
    }

    Scaffold(
        containerColor = BackgroundMint
    ) { paddingValores ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValores)
                .padding(horizontal = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))

                Text(
                    text = "Bem-vindo de volta",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    ),
                    color = BrandHeaderTeal
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Entre para continuar sua sequência de hábitos.",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(40.dp))

                if (!estado.mensagemErro.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(StatusDangerBg, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = estado.mensagemErro ?: "",
                            color = StatusDanger,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                CampoTexto(
                    rotulo = "E-mail",
                    valor = email,
                    aoMudarValor = {
                        email = it
                        viewModel.limparMensagens()
                    },
                    placeholder = "voce@exemplo.com",
                    tipoTeclado = KeyboardType.Email,
                    temErro = estado.erroEmail != null,
                    mensagemErro = estado.erroEmail
                )

                Spacer(modifier = Modifier.height(20.dp))

                CampoTexto(
                    rotulo = "Senha",
                    valor = senha,
                    aoMudarValor = {
                        senha = it
                        viewModel.limparMensagens()
                    },
                    placeholder = "Digite sua senha",
                    ehSenha = true,
                    temErro = estado.erroSenha != null,
                    mensagemErro = estado.erroSenha
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "Esqueci minha senha",
                        color = PrimaryTeal,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable(onClick = aoIrParaEsqueceuSenha)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                BotaoPrincipal(
                    texto = "Entrar",
                    aoClicar = {
                        viewModel.entrar(email, senha, aoLoginSucesso)
                    },
                    estaCarregando = estado.estaCarregando
                )

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.padding(bottom = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ainda não tem conta? ",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Criar conta",
                        color = PrimaryTeal,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable(onClick = aoIrParaRegistrar)
                    )
                }
            }
        }
    }
}
