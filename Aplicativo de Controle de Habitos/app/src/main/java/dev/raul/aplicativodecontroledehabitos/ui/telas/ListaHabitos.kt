package dev.raul.aplicativodecontroledehabitos.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.raul.aplicativodecontroledehabitos.habitos.HabitoViewModel
import dev.raul.aplicativodecontroledehabitos.ui.componentes.BarraProgressoHabito
import dev.raul.aplicativodecontroledehabitos.ui.componentes.BarraSemanal
import dev.raul.aplicativodecontroledehabitos.ui.componentes.CardHabito
import dev.raul.aplicativodecontroledehabitos.ui.theme.BackgroundMint
import dev.raul.aplicativodecontroledehabitos.ui.theme.BrandHeaderTeal
import dev.raul.aplicativodecontroledehabitos.ui.theme.PrimaryTeal
import dev.raul.aplicativodecontroledehabitos.ui.theme.StatusDanger
import dev.raul.aplicativodecontroledehabitos.ui.theme.StatusDangerBg
import dev.raul.aplicativodecontroledehabitos.ui.theme.TextSecondary

@Composable
fun ListaHabitos(
    viewModel: HabitoViewModel,
    aoIrParaNovoHabito: () -> Unit,
    aoIrParaDetalhes: (String) -> Unit,
    aoSair: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    var exibirTodosOsHabitos by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = BackgroundMint,
        floatingActionButton = {
            FloatingActionButton(
                onClick = aoIrParaNovoHabito,
                containerColor = BrandHeaderTeal,
                contentColor = Color.White,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .padding(bottom = 16.dp, end = 8.dp)
                    .size(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Novo hábito",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { paddingValores ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValores),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (estado.ehHoje) "Hoje" else "Hábitos",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp
                            ),
                            color = BrandHeaderTeal
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = estado.cabecalhoDataFormatado,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                            color = TextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(StatusDangerBg),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = aoSair) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                                contentDescription = "Sair",
                                tint = StatusDanger,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(6.dp))
                BarraSemanal(
                    dias = estado.diasDaSemana,
                    aoSelecionarDia = { dataIso ->
                        viewModel.selecionarData(dataIso)
                        exibirTodosOsHabitos = false
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(6.dp))
                BarraProgressoHabito(
                    totalConcluidos = estado.concluidosHoje,
                    totalAgendados = estado.totalHoje,
                    porcentagem = estado.porcentagemProgressoHoje,
                    titulo = if (estado.ehHoje) "Progresso de hoje" else "Progresso do dia"
                )
            }

            item {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (exibirTodosOsHabitos) "Todos os hábitos" else "Seus hábitos",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = BrandHeaderTeal
                    )

                    if (estado.todosOsHabitos.isNotEmpty()) {
                        Text(
                            text = if (exibirTodosOsHabitos) {
                                "Ver por dia (${estado.habitosDoDia.size})"
                            } else {
                                "Ver todos (${estado.todosOsHabitos.size})"
                            },
                            color = PrimaryTeal,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { exibirTodosOsHabitos = !exibirTodosOsHabitos }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            val habitosExibir = if (exibirTodosOsHabitos) estado.todosOsHabitos else estado.habitosDoDia

            if (habitosExibir.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val mensagem = if (estado.todosOsHabitos.isEmpty()) {
                            "Nenhum hábito cadastrado ainda.\nToque no botão + para criar o seu primeiro hábito!"
                        } else {
                            "Nenhum hábito programado para este dia da semana.\nToque no botão + para adicionar ou alterne para 'Ver todos'."
                        }

                        Text(
                            text = mensagem,
                            color = TextSecondary,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                items(
                    items = habitosExibir,
                    key = { it.id }
                ) { habito ->
                    val concluido = habito.foiConcluidoEm(estado.dataSelecionadaIso)
                    val sequencia = viewModel.obterSequenciaAtual(habito)

                    CardHabito(
                        habito = habito,
                        foiConcluido = concluido,
                        sequenciaDias = sequencia,
                        aoAlternarConclusao = {
                            viewModel.alternarConclusao(habito.id, estado.dataSelecionadaIso)
                        },
                        aoClicar = {
                            aoIrParaDetalhes(habito.id)
                        }
                    )
                }
            }
        }
    }
}
