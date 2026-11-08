package dev.raul.aplicativodecontroledehabitos.ui.telas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import dev.raul.aplicativodecontroledehabitos.dados.model.StatusDia
import dev.raul.aplicativodecontroledehabitos.habitos.HabitoViewModel
import dev.raul.aplicativodecontroledehabitos.ui.componentes.IconeHabito
import dev.raul.aplicativodecontroledehabitos.ui.theme.BackgroundMint
import dev.raul.aplicativodecontroledehabitos.ui.theme.BorderField
import dev.raul.aplicativodecontroledehabitos.ui.theme.BrandHeaderTeal
import dev.raul.aplicativodecontroledehabitos.ui.theme.PrimaryTeal
import dev.raul.aplicativodecontroledehabitos.ui.theme.StatusDanger
import dev.raul.aplicativodecontroledehabitos.ui.theme.StatusMissed
import dev.raul.aplicativodecontroledehabitos.ui.theme.SurfaceWhite
import dev.raul.aplicativodecontroledehabitos.ui.theme.TextPrimary
import dev.raul.aplicativodecontroledehabitos.ui.theme.TextSecondary
import dev.raul.aplicativodecontroledehabitos.util.UtilitarioData

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetalhesHabito(
    habitoId: String,
    viewModel: HabitoViewModel,
    aoVoltar: () -> Unit,
    aoEditar: (String) -> Unit
) {
    LaunchedEffect(habitoId) {
        viewModel.selecionarHabito(habitoId)
    }

    val estado by viewModel.estado.collectAsState()
    val habito = estado.todosOsHabitos.find { it.id == habitoId }

    var exibirDialogoExclusao by remember { mutableStateOf(false) }

    if (habito == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Hábito não encontrado.", color = TextSecondary)
        }
        return
    }

    val hojeIso = UtilitarioData.dataAtualIso()
    val concluidoHoje = habito.foiConcluidoEm(hojeIso)
    val sequencia = viewModel.obterSequenciaAtual(habito)
    val melhorSequencia = viewModel.obterMelhorSequencia(habito)
    val taxaConclusao = viewModel.obterTaxaConclusao(habito)
    val ultimos14Dias = viewModel.obterUltimos14Dias(habito)
    val concluidos14Contagem = ultimos14Dias.count { it.foiConcluido }

    if(exibirDialogoExclusao){
        AlertDialog(
            onDismissRequest = { exibirDialogoExclusao = false },
            title = {
                Text(
                    text = "Excluir hábito?",
                    fontWeight = FontWeight.Bold,
                    color = BrandHeaderTeal
                )
            },
            text = {
                Text(
                    text = "Tem certeza de que deseja excluir o hábito \"${habito.nome}\"? Esta ação não pode ser desfeita.",
                    color = TextSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        exibirDialogoExclusao = false
                        viewModel.excluirHabito(habito.id)
                        aoVoltar()
                    }
                ) {
                    Text(text = "Excluir", color = StatusDanger, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { exibirDialogoExclusao = false }) {
                    Text(text = "Cancelar", color = TextSecondary)
                }
            },
            containerColor = SurfaceWhite
        )
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = aoVoltar) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = TextPrimary
                    )
                }

                Text(
                    text = "Detalhes",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = BrandHeaderTeal
                )

                Text(
                    text = "Editar",
                    color = PrimaryTeal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .clickable { aoEditar(habito.id) }
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                border = CardDefaults.outlinedCardBorder(enabled = true)
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    IconeHabito(
                        nomeIcone = habito.nomeIcone,
                        corHex = habito.corHex,
                        tamanhoContainer = 64.dp,
                        tamanhoIcone = 32.dp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = habito.nome,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        ),
                        color = BrandHeaderTeal
                    )

                    if (habito.descricao.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = habito.descricao,
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(BackgroundMint, RoundedCornerShape(20.dp))
                                .border(1.dp, BorderField, RoundedCornerShape(20.dp))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = habito.frequenciaFormatada(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = BrandHeaderTeal
                            )
                        }

                        if (habito.lembreteAtivo) {
                            Box(
                                modifier = Modifier
                                    .background(BackgroundMint, RoundedCornerShape(20.dp))
                                    .border(1.dp, BorderField, RoundedCornerShape(20.dp))
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Lembrete às ${habito.horarioLembrete}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = BrandHeaderTeal
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CardEstatistica(
                    valor = "$sequencia dias",
                    rotulo = "Sequência\natual",
                    modifier = Modifier.weight(1f)
                )
                CardEstatistica(
                    valor = "$melhorSequencia dias",
                    rotulo = "Melhor\nsequência",
                    modifier = Modifier.weight(1f)
                )
                CardEstatistica(
                    valor = "$taxaConclusao%",
                    rotulo = "Conclusão\n ",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                border = CardDefaults.outlinedCardBorder(enabled = true)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Últimos 14 dias",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = BrandHeaderTeal
                        )

                        Text(
                            text = "$concluidos14Contagem de 14",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val linha1 = ultimos14Dias.take(7)
                    val linha2 = ultimos14Dias.drop(7)

                    LinhaGradeDias(linha1)
                    Spacer(modifier = Modifier.height(10.dp))
                    LinhaGradeDias(linha2)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(PrimaryTeal)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Feito", fontSize = 12.sp, color = TextSecondary)

                        Spacer(modifier = Modifier.width(16.dp))

                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(StatusMissed)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Perdido", fontSize = 12.sp, color = TextSecondary)

                        Spacer(modifier = Modifier.width(16.dp))

                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .border(1.5.dp, PrimaryTeal, RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Hoje", fontSize = 12.sp, color = TextSecondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            OutlinedButton(
                onClick = {
                    viewModel.alternarConclusao(habito.id, hojeIso)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (concluidoHoje) PrimaryTeal.copy(alpha = 0.08f) else SurfaceWhite,
                    contentColor = PrimaryTeal
                ),
                border = BorderStroke(2.dp, PrimaryTeal)
            ) {
                if (concluidoHoje) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = PrimaryTeal,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Concluído hoje",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = PrimaryTeal
                    )
                } else {
                    Text(
                        text = "Marcar como feito hoje",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = PrimaryTeal
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Excluir hábito",
                    color = StatusDanger,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { exibirDialogoExclusao = true }
                        .padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun CardEstatistica(
    valor: String,
    rotulo: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = CardDefaults.outlinedCardBorder(enabled = true)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = valor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BrandHeaderTeal
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = rotulo,
                fontSize = 12.sp,
                lineHeight = 15.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun LinhaGradeDias(dias: List<StatusDia>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        dias.forEach { dia ->
            val cor = if (dia.foiConcluido) PrimaryTeal else StatusMissed

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(cor)
                    .then(
                        if (dia.ehHoje) {
                            Modifier.border(2.dp, BrandHeaderTeal, RoundedCornerShape(10.dp))
                        } else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (dia.foiConcluido) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
