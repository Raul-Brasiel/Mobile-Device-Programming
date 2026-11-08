package dev.raul.aplicativodecontroledehabitos.ui.telas

import android.app.TimePickerDialog
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.raul.aplicativodecontroledehabitos.habitos.HabitoViewModel
import dev.raul.aplicativodecontroledehabitos.ui.componentes.BotaoPrincipal
import dev.raul.aplicativodecontroledehabitos.ui.componentes.CampoTexto
import dev.raul.aplicativodecontroledehabitos.ui.componentes.SeletorCor
import dev.raul.aplicativodecontroledehabitos.ui.componentes.SeletorDias
import dev.raul.aplicativodecontroledehabitos.ui.theme.BackgroundMint
import dev.raul.aplicativodecontroledehabitos.ui.theme.BorderField
import dev.raul.aplicativodecontroledehabitos.ui.theme.BrandHeaderTeal
import dev.raul.aplicativodecontroledehabitos.ui.theme.PrimaryTeal
import dev.raul.aplicativodecontroledehabitos.ui.theme.SurfaceWhite
import dev.raul.aplicativodecontroledehabitos.ui.theme.TextPrimary
import dev.raul.aplicativodecontroledehabitos.ui.theme.TextSecondary

@Composable
fun FormularioHabito(
    habitoId: String? = null,
    viewModel: HabitoViewModel,
    aoVoltar: () -> Unit
) {
    val contexto = LocalContext.current
    val estaEditando = habitoId != null

    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var corHexSelecionada by remember { mutableStateOf("#0088A9") }
    var nomeIcone by remember { mutableStateOf("water") }
    var diasSelecionados by remember { mutableStateOf(listOf(2, 3, 4, 5, 6)) }
    var lembreteAtivo by remember { mutableStateOf(false) }
    var horarioLembrete by remember { mutableStateOf("08:00") }
    var erroNome by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(habitoId) {
        if (habitoId != null) {
            val habito = viewModel.estado.value.todosOsHabitos.find { it.id == habitoId }
            if (habito != null) {
                nome = habito.nome
                descricao = habito.descricao
                corHexSelecionada = habito.corHex
                nomeIcone = habito.nomeIcone
                diasSelecionados = habito.diasDaSemana
                lembreteAtivo = habito.lembreteAtivo
                horarioLembrete = habito.horarioLembrete
            }
        }
    }

    Scaffold(
        containerColor = BackgroundMint
    ) { paddingValores ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValores)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = aoVoltar) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = if (estaEditando) "Editar hábito" else "Novo hábito",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
                    color = BrandHeaderTeal
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            CampoTexto(
                rotulo = "Nome do hábito",
                valor = nome,
                aoMudarValor = {
                    nome = it
                    erroNome = null
                },
                placeholder = "Ex.: Meditar 10 minutos",
                temErro = erroNome != null,
                mensagemErro = erroNome
            )

            Spacer(modifier = Modifier.height(20.dp))

            CampoTexto(
                rotulo = "Descrição (opcional)",
                valor = descricao,
                aoMudarValor = { descricao = it },
                placeholder = "Por que esse hábito importa para você?",
                linhaUnica = false,
                linhasMinimas = 3,
                linhasMaximas = 4
            )

            Spacer(modifier = Modifier.height(24.dp))

            SeletorCor(
                corHexSelecionada = corHexSelecionada,
                aoSelecionarCor = { corHexSelecionada = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SeletorDias(
                diasSelecionados = diasSelecionados,
                aoAlternarDia = { dia ->
                    diasSelecionados = if (diasSelecionados.contains(dia)) {
                        if (diasSelecionados.size > 1) diasSelecionados - dia else diasSelecionados
                    } else {
                        diasSelecionados + dia
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                border = CardDefaults.outlinedCardBorder(enabled = true)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Lembrete diário",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandHeaderTeal
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "Receba um aviso no horário escolhido",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }

                        Switch(
                            checked = lembreteAtivo,
                            onCheckedChange = { lembreteAtivo = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = PrimaryTeal,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = BorderField
                            )
                        )
                    }

                    if (lembreteAtivo) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Horário",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary
                            )

                            Box(
                                modifier = Modifier
                                    .border(1.dp, BorderField, RoundedCornerShape(12.dp))
                                    .clickable {
                                        val partes = horarioLembrete.split(":")
                                        val horaAtual = partes.getOrNull(0)?.toIntOrNull() ?: 8
                                        val minutoAtual = partes.getOrNull(1)?.toIntOrNull() ?: 0

                                        TimePickerDialog(
                                            contexto,
                                            { _, horaDoDia, minuto ->
                                                horarioLembrete = String.format("%02d:%02d", horaDoDia, minuto)
                                            },
                                            horaAtual,
                                            minutoAtual,
                                            true
                                        ).show()
                                    }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = horarioLembrete,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.Outlined.Schedule,
                                        contentDescription = "Selecionar horário",
                                        tint = TextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            BotaoPrincipal(
                texto = if (estaEditando) "Salvar alterações" else "Criar hábito",
                aoClicar = {
                    if (nome.isBlank()) {
                        erroNome = "Informe o nome do hábito"
                        return@BotaoPrincipal
                    }

                    val iconeDerivado = when {
                        nome.contains("água", ignoreCase = true) || nome.contains("beber", ignoreCase = true) -> "water"
                        nome.contains("livro", ignoreCase = true) || nome.contains("ler", ignoreCase = true) || nome.contains("página", ignoreCase = true) -> "book"
                        nome.contains("treino", ignoreCase = true) || nome.contains("academia", ignoreCase = true) || nome.contains("exercício", ignoreCase = true) -> "fitness"
                        nome.contains("estudar", ignoreCase = true) || nome.contains("estudo", ignoreCase = true) || nome.contains("aula", ignoreCase = true) -> "study"
                        else -> nomeIcone
                    }

                    if (estaEditando && habitoId != null) {
                        viewModel.atualizarHabito(
                            habitoId = habitoId,
                            nome = nome,
                            descricao = descricao,
                            corHex = corHexSelecionada,
                            nomeIcone = iconeDerivado,
                            diasSemana = diasSelecionados,
                            lembreteAtivo = lembreteAtivo,
                            horarioLembrete = horarioLembrete
                        )
                    } else {
                        viewModel.criarHabito(
                            nome = nome,
                            descricao = descricao,
                            corHex = corHexSelecionada,
                            nomeIcone = iconeDerivado,
                            diasSemana = diasSelecionados,
                            lembreteAtivo = lembreteAtivo,
                            horarioLembrete = horarioLembrete
                        )
                    }
                    aoVoltar()
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Cancelar",
                    color = TextSecondary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable(onClick = aoVoltar)
                        .padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
