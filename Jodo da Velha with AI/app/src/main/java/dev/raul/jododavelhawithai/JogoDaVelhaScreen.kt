package dev.raul.jododavelhawithai

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.raul.jododavelhawithai.ui.theme.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun JogoDaVelhaScreen(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    var aiJob by remember { mutableStateOf<Job?>(null) }

    // Estados do Modo e Dificuldade
    var modoJogo by rememberSaveable { mutableStateOf(ModoJogo.CONTRA_IA) }
    var dificuldadeIA by rememberSaveable { mutableStateOf(DificuldadeIA.FACIL) }

    // Estado do Tabuleiro (9 posições)
    var tabuleiro by remember { mutableStateOf(List(9) { "" }) }
    var vezDoX by rememberSaveable { mutableStateOf(true) }
    var isAiThinking by remember { mutableStateOf(false) }

    // Estado de Fim de Jogo
    var vencedor by remember { mutableStateOf<String?>(null) }
    var linhaVitoria by remember { mutableStateOf<List<Int>>(emptyList()) }
    var isEmpate by remember { mutableStateOf(false) }

    // Diálogos
    var mostrarDialogoReset by remember { mutableStateOf(false) }
    var mostrarDialogoZerarPlacar by remember { mutableStateOf(false) }

    // Placar persistente na sessão
    var vitoriasJogadorIA by rememberSaveable { mutableIntStateOf(0) }
    var vitoriasIA by rememberSaveable { mutableIntStateOf(0) }
    var empatesIA by rememberSaveable { mutableIntStateOf(0) }

    var vitoriasPlayerX by rememberSaveable { mutableIntStateOf(0) }
    var vitoriasPlayerO by rememberSaveable { mutableIntStateOf(0) }
    var empatesPvP by rememberSaveable { mutableIntStateOf(0) }

    fun reiniciarPartida() {
        aiJob?.cancel()
        tabuleiro = List(9) { "" }
        vezDoX = true
        isAiThinking = false
        vencedor = null
        linhaVitoria = emptyList()
        isEmpate = false
    }

    fun onCellClick(index: Int) {
        if (tabuleiro[index].isNotEmpty() || vencedor != null || isEmpate || isAiThinking) {
            return
        }

        if (modoJogo == ModoJogo.DOIS_JOGADORES) {
            val novoSimbolo = if (vezDoX) "X" else "O"
            val novoTabuleiro = tabuleiro.toMutableList()
            novoTabuleiro[index] = novoSimbolo
            tabuleiro = novoTabuleiro

            val resultado = JogoDaVelhaLogic.verificarResultado(novoTabuleiro)
            if (resultado.temVencedor) {
                vencedor = resultado.vencedor
                linhaVitoria = resultado.linhaVitoria
                if (resultado.vencedor == "X") vitoriasPlayerX++ else vitoriasPlayerO++
            } else if (resultado.isEmpate) {
                isEmpate = true
                empatesPvP++
            } else {
                vezDoX = !vezDoX
            }
        } else {
            // Modo Contra IA: Jogador joga como X
            val novoTabuleiro = tabuleiro.toMutableList()
            novoTabuleiro[index] = "X"
            tabuleiro = novoTabuleiro

            val resultadoJogador = JogoDaVelhaLogic.verificarResultado(novoTabuleiro)
            if (resultadoJogador.temVencedor) {
                vencedor = resultadoJogador.vencedor
                linhaVitoria = resultadoJogador.linhaVitoria
                vitoriasJogadorIA++
            } else if (resultadoJogador.isEmpate) {
                isEmpate = true
                empatesIA++
            } else {
                vezDoX = false
                isAiThinking = true

                aiJob = coroutineScope.launch {
                    delay(450)
                    val jogadaIA = JogoDaVelhaLogic.obterJogadaIA(tabuleiro, dificuldadeIA)
                    if (jogadaIA in 0..8 && tabuleiro[jogadaIA].isEmpty()) {
                        val tabuleiroAposIA = tabuleiro.toMutableList()
                        tabuleiroAposIA[jogadaIA] = "O"
                        tabuleiro = tabuleiroAposIA

                        val resultadoIA = JogoDaVelhaLogic.verificarResultado(tabuleiroAposIA)
                        if (resultadoIA.temVencedor) {
                            vencedor = resultadoIA.vencedor
                            linhaVitoria = resultadoIA.linhaVitoria
                            vitoriasIA++
                        } else if (resultadoIA.isEmpate) {
                            isEmpate = true
                            empatesIA++
                        } else {
                            vezDoX = true
                        }
                    }
                    isAiThinking = false
                }
            }
        }
    }

    val (statusTexto, statusCor) = when {
        vencedor != null -> {
            val texto = if (modoJogo == ModoJogo.CONTRA_IA) {
                if (vencedor == "X") "O jogador X venceu!" else "A IA venceu!"
            } else {
                "O jogador $vencedor venceu!"
            }
            val cor = if (vencedor == "X") PlayerXColor else PlayerOColor
            Pair(texto, cor)
        }
        isEmpate -> Pair("Empate!", DrawColor)
        isAiThinking -> Pair("IA pensando...", PlayerOColor)
        vezDoX -> Pair("Vez do jogador X", PlayerXColor)
        else -> Pair("Vez do jogador O", PlayerOColor)
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título Principal (30sp conforme original)
        Text(
            text = "Jogo da Velha",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Seletor de Modo de Jogo (Desafio 2)
        SeletorModoJogo(
            modoAtual = modoJogo,
            onModoSelected = { novoModo ->
                if (modoJogo != novoModo) {
                    modoJogo = novoModo
                    reiniciarPartida()
                }
            }
        )

        // Seletor de Dificuldade da IA (Desafio 1 - visível apenas no modo contra IA)
        if (modoJogo == ModoJogo.CONTRA_IA) {
            Spacer(modifier = Modifier.height(10.dp))
            SeletorDificuldade(
                dificuldadeAtual = dificuldadeIA,
                onDificuldadeSelected = { novaDificuldade ->
                    dificuldadeIA = novaDificuldade
                }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Painel de Status da Partida (30sp conforme original)
        Text(
            text = statusTexto,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = statusCor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Placar Persistente da Sessão (20sp conforme original)
        if (modoJogo == ModoJogo.CONTRA_IA) {
            Text(
                text = "Jogador (X): $vitoriasJogadorIA",
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = "IA (O): $vitoriasIA",
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Empate: $empatesIA",
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                text = "Jogador X: $vitoriasPlayerX",
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Jogador O: $vitoriasPlayerO",
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Empate: $empatesPvP",
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grade Composable 3x3
        TabuleiroGrid(
            tabuleiro = tabuleiro,
            linhaVitoria = linhaVitoria,
            onCellClick = { onCellClick(it) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Botão para reiniciar o jogo
        Button(
            onClick = { mostrarDialogoReset = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = CircleShape,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Reiniciar jogo",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        // Opção para Zerar Placar da Sessão
        TextButton(
            onClick = { mostrarDialogoZerarPlacar = true },
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(
                text = "Zerar placar",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }

    // Diálogo de confirmação para reiniciar o jogo
    if (mostrarDialogoReset) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoReset = false },
            title = {
                Text(
                    text = "Resetar jogo",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            text = {
                Text(
                    text = "Deseja reiniciar o jogo?",
                    color = Color.DarkGray
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        reiniciarPartida()
                        mostrarDialogoReset = false
                    }
                ) {
                    Text("Sim", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarDialogoReset = false }
                ) {
                    Text("Não", color = Color.Gray)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Diálogo de confirmação para zerar placar
    if (mostrarDialogoZerarPlacar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoZerarPlacar = false },
            title = {
                Text(
                    text = "Zerar placar",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            text = {
                Text(
                    text = "Deseja zerar a contagem do placar da sessão atual?",
                    color = Color.DarkGray
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (modoJogo == ModoJogo.CONTRA_IA) {
                            vitoriasJogadorIA = 0
                            vitoriasIA = 0
                            empatesIA = 0
                        } else {
                            vitoriasPlayerX = 0
                            vitoriasPlayerO = 0
                            empatesPvP = 0
                        }
                        mostrarDialogoZerarPlacar = false
                    }
                ) {
                    Text("Sim", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarDialogoZerarPlacar = false }
                ) {
                    Text("Não", color = Color.Gray)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun TabuleiroGrid(
    tabuleiro: List<String>,
    linhaVitoria: List<Int>,
    onCellClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        val espacamento = 10.dp
        val tamanhoCelula: Dp = if (maxWidth < 360.dp) {
            (maxWidth - 50.dp) / 3
        } else {
            96.dp
        }

        Box(
            modifier = Modifier
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(espacamento)
            ) {
                for (linha in 0..2) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(espacamento)
                    ) {
                        for (coluna in 0..2) {
                            val indice = linha * 3 + coluna
                            val valor = tabuleiro[indice]
                            val isVencedor = linhaVitoria.contains(indice)

                            val corTexto = when {
                                isVencedor && valor == "X" -> PlayerXColor
                                isVencedor && valor == "O" -> PlayerOColor
                                else -> Color.Black
                            }

                            Button(
                                onClick = { onCellClick(indice) },
                                modifier = Modifier.size(tamanhoCelula),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    disabledContainerColor = Color.White
                                ),
                                contentPadding = PaddingValues(0.dp),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 2.dp,
                                    pressedElevation = 1.dp
                                )
                            ) {
                                Text(
                                    text = valor,
                                    fontSize = 58.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = corTexto,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SeletorModoJogo(
    modoAtual: ModoJogo,
    onModoSelected: (ModoJogo) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(25.dp))
            .background(Color(0xFFF3F4F6))
            .padding(4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ModoJogo.values().forEach { modo ->
            val selecionado = modo == modoAtual
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (selecionado) Color.Black else Color.Transparent)
                    .clickable { onModoSelected(modo) }
                    .padding(horizontal = 18.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = modo.titulo,
                    color = if (selecionado) Color.White else Color.Black,
                    fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun SeletorDificuldade(
    dificuldadeAtual: DificuldadeIA,
    onDificuldadeSelected: (DificuldadeIA) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Dificuldade da IA:",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFF3F4F6))
                .padding(3.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DificuldadeIA.values().forEach { diff ->
                val selecionado = diff == dificuldadeAtual
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (selecionado) Color.Black else Color.Transparent)
                        .clickable { onDificuldadeSelected(diff) }
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = diff.titulo,
                        color = if (selecionado) Color.White else Color.Black,
                        fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JogoDaVelhaPreview() {
    JodoDaVelhaWithAITheme {
        JogoDaVelhaScreen()
    }
}
