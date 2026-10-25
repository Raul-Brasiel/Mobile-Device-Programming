package dev.raul.jododavelhawithai

import org.junit.Assert.*
import org.junit.Test

class JogoDaVelhaLogicTest {

    @Test
    fun testVitoriaLinha() {
        val tabuleiro = listOf(
            "X", "X", "X",
            "O", "O", "",
            "", "", ""
        )
        val res = JogoDaVelhaLogic.verificarResultado(tabuleiro)
        assertTrue(res.temVencedor)
        assertEquals("X", res.vencedor)
        assertEquals(listOf(0, 1, 2), res.linhaVitoria)
        assertFalse(res.isEmpate)
    }

    @Test
    fun testVitoriaColuna() {
        val tabuleiro = listOf(
            "O", "X", "",
            "O", "X", "",
            "O", "", "X"
        )
        val res = JogoDaVelhaLogic.verificarResultado(tabuleiro)
        assertTrue(res.temVencedor)
        assertEquals("O", res.vencedor)
        assertEquals(listOf(0, 3, 6), res.linhaVitoria)
    }

    @Test
    fun testVitoriaDiagonal() {
        val tabuleiro = listOf(
            "X", "O", "O",
            "", "X", "",
            "", "", "X"
        )
        val res = JogoDaVelhaLogic.verificarResultado(tabuleiro)
        assertTrue(res.temVencedor)
        assertEquals("X", res.vencedor)
        assertEquals(listOf(0, 4, 8), res.linhaVitoria)
    }

    @Test
    fun testEmpate() {
        val tabuleiro = listOf(
            "X", "O", "X",
            "X", "O", "O",
            "O", "X", "X"
        )
        val res = JogoDaVelhaLogic.verificarResultado(tabuleiro)
        assertFalse(res.temVencedor)
        assertTrue(res.isEmpate)
    }

    @Test
    fun testIAMedioVenceImediatamente() {
        // O tem chance de vencer na posição 2
        val tabuleiro = listOf(
            "O", "O", "",
            "X", "X", "",
            "", "", ""
        )
        val jogada = JogoDaVelhaLogic.obterJogadaMedia(tabuleiro)
        assertEquals(2, jogada)
    }

    @Test
    fun testIAMedioBloqueiaVitoriaDoHumano() {
        // X tem chance de vencer na posição 2, O deve bloquear na posição 2
        val tabuleiro = listOf(
            "X", "X", "",
            "O", "", "",
            "", "", ""
        )
        val jogada = JogoDaVelhaLogic.obterJogadaMedia(tabuleiro)
        assertEquals(2, jogada)
    }

    @Test
    fun testMinimaxImbativelContraJogadasAleatorias() {
        // Simular 50 partidas completas onde o oponente X joga de forma mista/aleatória
        // A IA Difícil (O) NUNCA pode perder (deve vencer ou empatar sempre)
        for (i in 1..50) {
            val tabuleiro = MutableList(9) { "" }
            var vezDoX = true

            while (true) {
                val resultado = JogoDaVelhaLogic.verificarResultado(tabuleiro)
                if (resultado.temVencedor || resultado.isEmpate) {
                    assertNotEquals("Minimax perdeu uma partida!", "X", resultado.vencedor)
                    break
                }

                if (vezDoX) {
                    val vazias = tabuleiro.indices.filter { tabuleiro[it].isEmpty() }
                    tabuleiro[vazias.random()] = "X"
                } else {
                    val jogadaIA = JogoDaVelhaLogic.obterJogadaMinimax(tabuleiro)
                    tabuleiro[jogadaIA] = "O"
                }
                vezDoX = !vezDoX
            }
        }
    }
}
