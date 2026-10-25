package dev.raul.jododavelhawithai

import kotlin.random.Random

object JogoDaVelhaLogic {

    /**
     * Todas as 8 combinações possíveis de vitória no tabuleiro 3x3.
     */
    val combinacoesVitoria: List<IntArray> = listOf(
        intArrayOf(0, 1, 2),
        intArrayOf(3, 4, 5),
        intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6),
        intArrayOf(1, 4, 7),
        intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8),
        intArrayOf(2, 4, 6)
    )

    /**
     * Verifica se houve vitória ou empate no tabuleiro atual.
     */
    fun verificarResultado(tabuleiro: List<String>): ResultadoVerificacao {
        for (comb in combinacoesVitoria) {
            val a = tabuleiro[comb[0]]
            val b = tabuleiro[comb[1]]
            val c = tabuleiro[comb[2]]

            if (a.isNotEmpty() && a == b && a == c) {
                return ResultadoVerificacao(
                    temVencedor = true,
                    vencedor = a,
                    linhaVitoria = comb.toList(),
                    isEmpate = false
                )
            }
        }

        if (tabuleiro.none { it.isEmpty() }) {
            return ResultadoVerificacao(
                temVencedor = false,
                vencedor = "",
                linhaVitoria = emptyList(),
                isEmpate = true
            )
        }

        return ResultadoVerificacao(
            temVencedor = false,
            vencedor = "",
            linhaVitoria = emptyList(),
            isEmpate = false
        )
    }

    /**
     * Retorna o vencedor ("X" ou "O"), "EMPATE" ou null se o jogo ainda continua.
     */
    private fun verificarVencedorRapido(tabuleiro: List<String>): String? {
        for (comb in combinacoesVitoria) {
            val a = tabuleiro[comb[0]]
            if (a.isNotEmpty() && a == tabuleiro[comb[1]] && a == tabuleiro[comb[2]]) {
                return a
            }
        }
        if (tabuleiro.none { it.isEmpty() }) {
            return "EMPATE"
        }
        return null
    }

    /**
     * Obtém a próxima jogada da IA com base na dificuldade selecionada.
     */
    fun obterJogadaIA(tabuleiro: List<String>, dificuldade: DificuldadeIA): Int {
        return when (dificuldade) {
            DificuldadeIA.FACIL -> obterJogadaFacil(tabuleiro)
            DificuldadeIA.MEDIO -> obterJogadaMedia(tabuleiro)
            DificuldadeIA.DIFICIL -> obterJogadaMinimax(tabuleiro)
        }
    }

    /**
     * Nível Fácil: Escolhe aleatoriamente qualquer célula vazia disponível.
     */
    fun obterJogadaFacil(tabuleiro: List<String>): Int {
        val casasVazias = tabuleiro.indices.filter { tabuleiro[it].isEmpty() }
        if (casasVazias.isEmpty()) return -1
        return casasVazias.random()
    }

    /**
     * Nível Médio:
     * 1. Se puder vencer na próxima jogada, vence.
     * 2. Se o oponente puder vencer na próxima jogada, bloqueia.
     * 3. Caso contrário, joga no centro ou escolhe aleatoriamente.
     */
    fun obterJogadaMedia(tabuleiro: List<String>): Int {
        val casasVazias = tabuleiro.indices.filter { tabuleiro[it].isEmpty() }
        if (casasVazias.isEmpty()) return -1
        if (casasVazias.size == 1) return casasVazias.first()

        val tabuleiroMutavel = tabuleiro.toMutableList()

        // 1. Vencer imediatamente com 'O' se possível
        for (posicao in casasVazias) {
            tabuleiroMutavel[posicao] = "O"
            if (verificarVencedorRapido(tabuleiroMutavel) == "O") {
                return posicao
            }
            tabuleiroMutavel[posicao] = ""
        }

        // 2. Bloquear vitória imediata de 'X'
        for (posicao in casasVazias) {
            tabuleiroMutavel[posicao] = "X"
            if (verificarVencedorRapido(tabuleiroMutavel) == "X") {
                return posicao
            }
            tabuleiroMutavel[posicao] = ""
        }

        // 3. Preferência moderada pelo centro (posição 4)
        if (casasVazias.contains(4) && Random.nextFloat() < 0.6f) {
            return 4
        }

        return casasVazias.random()
    }

    /**
     * Nível Difícil: Minimax imbatível com poda Alfa-Beta.
     */
    fun obterJogadaMinimax(tabuleiro: List<String>): Int {
        val casasVazias = tabuleiro.indices.filter { tabuleiro[it].isEmpty() }
        if (casasVazias.isEmpty()) return -1
        if (casasVazias.size == 1) return casasVazias.first()

        if (casasVazias.size >= 8 && tabuleiro[4].isEmpty()) {
            return 4
        }

        val tabuleiroMutavel = tabuleiro.toMutableList()
        var melhorPontuacao = Int.MIN_VALUE
        val melhoresMovimentos = mutableListOf<Int>()

        for (posicao in casasVazias) {
            tabuleiroMutavel[posicao] = "O"
            val pontuacao = minimax(
                tabuleiro = tabuleiroMutavel,
                isMaximizing = false,
                depth = 0,
                alpha = -1000,
                beta = 1000
            )
            tabuleiroMutavel[posicao] = ""

            if (pontuacao > melhorPontuacao) {
                melhorPontuacao = pontuacao
                melhoresMovimentos.clear()
                melhoresMovimentos.add(posicao)
            } else if (pontuacao == melhorPontuacao) {
                melhoresMovimentos.add(posicao)
            }
        }

        return melhoresMovimentos.random()
    }

    private fun minimax(
        tabuleiro: MutableList<String>,
        isMaximizing: Boolean,
        depth: Int,
        alpha: Int,
        beta: Int
    ): Int {
        val vencedor = verificarVencedorRapido(tabuleiro)
        if (vencedor == "O") return 10 - depth
        if (vencedor == "X") return depth - 10
        if (vencedor == "EMPATE") return 0

        var atualAlpha = alpha
        var atualBeta = beta

        if (isMaximizing) {
            var melhorPontuacao = Int.MIN_VALUE
            for (i in 0 until 9) {
                if (tabuleiro[i].isEmpty()) {
                    tabuleiro[i] = "O"
                    val pontuacao = minimax(tabuleiro, false, depth + 1, atualAlpha, atualBeta)
                    tabuleiro[i] = ""
                    melhorPontuacao = maxOf(melhorPontuacao, pontuacao)
                    atualAlpha = maxOf(atualAlpha, melhorPontuacao)
                    if (atualBeta <= atualAlpha) break
                }
            }
            return melhorPontuacao
        } else {
            var melhorPontuacao = Int.MAX_VALUE
            for (i in 0 until 9) {
                if (tabuleiro[i].isEmpty()) {
                    tabuleiro[i] = "X"
                    val pontuacao = minimax(tabuleiro, true, depth + 1, atualAlpha, atualBeta)
                    tabuleiro[i] = ""
                    melhorPontuacao = minOf(melhorPontuacao, pontuacao)
                    atualBeta = minOf(atualBeta, melhorPontuacao)
                    if (atualBeta <= atualAlpha) break
                }
            }
            return melhorPontuacao
        }
    }
}
