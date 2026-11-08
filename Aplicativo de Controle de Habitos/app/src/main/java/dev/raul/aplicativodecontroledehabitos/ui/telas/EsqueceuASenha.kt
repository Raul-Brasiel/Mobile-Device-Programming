package dev.raul.aplicativodecontroledehabitos.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import dev.raul.aplicativodecontroledehabitos.ui.theme.HabitBlue
import dev.raul.aplicativodecontroledehabitos.ui.theme.HabitBlueBg
import dev.raul.aplicativodecontroledehabitos.ui.theme.HabitGreen
import dev.raul.aplicativodecontroledehabitos.ui.theme.HabitGreenBg
import dev.raul.aplicativodecontroledehabitos.ui.theme.PrimaryTeal
import dev.raul.aplicativodecontroledehabitos.ui.theme.StatusDanger
import dev.raul.aplicativodecontroledehabitos.ui.theme.StatusDangerBg
import dev.raul.aplicativodecontroledehabitos.ui.theme.TextPrimary
import dev.raul.aplicativodecontroledehabitos.ui.theme.TextSecondary

@Composable
fun EsqueceuASenha(
    viewModel: AuthViewModel,
    aoVoltar: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()

    var email by remember { mutableStateOf("") }

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
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                IconButton(onClick = aoVoltar) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(HabitBlueBg, RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = HabitBlue,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Esqueceu sua senha?",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    ),
                    color = BrandHeaderTeal
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Informe o e-mail da sua conta e enviaremos um link para você criar uma nova senha.",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(32.dp))

                if(!estado.mensagemInfo.isNullOrBlank()){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(HabitGreenBg, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ){
                        Text(
                            text = estado.mensagemInfo ?: "",
                            color = HabitGreen,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if(!estado.mensagemErro.isNullOrBlank()){
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

                Spacer(modifier = Modifier.height(28.dp))

                BotaoPrincipal(
                    texto = "Enviar link de recuperação",
                    aoClicar = {
                        viewModel.enviarEmailRecuperacao(email)
                    },
                    estaCarregando = estado.estaCarregando
                )

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Lembrou a senha? ",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Entrar",
                        color = PrimaryTeal,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable(onClick = aoVoltar)
                    )
                }
            }
        }
    }
}
