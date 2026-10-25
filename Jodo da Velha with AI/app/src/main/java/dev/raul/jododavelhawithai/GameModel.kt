package dev.raul.jododavelhawithai

enum class ModoJogo(val titulo: String) {
    CONTRA_IA("Contra IA"),
    DOIS_JOGADORES("2 Jogadores")
}

/**
 * Níveis de inteligência artificial.
 * - FACIL: Jogadas totalmente aleatórias.
 * - MEDIO: Vence e bloqueia vitórias imediatas; caso contrário, joga de forma mista.
 * - DIFICIL: Algoritmo Minimax imbatível (nunca é derrotado).
 */
enum class DificuldadeIA(val titulo: String) {
    FACIL("Fácil"),
    MEDIO("Médio"),
    DIFICIL("Difícil")
}

/**
 * Resultado da checagem do estado atual do tabuleiro.
 */
data class ResultadoVerificacao(
    val temVencedor: Boolean = false,
    val vencedor: String = "",
    val linhaVitoria: List<Int> = emptyList(),
    val isEmpate: Boolean = false
)