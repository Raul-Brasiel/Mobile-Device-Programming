package dev.raul.jododavelhawithai

enum class ModoJogo(val titulo: String) {
    CONTRA_IA("Contra IA"),
    DOIS_JOGADORES("2 Jogadores")
}

enum class DificuldadeIA(val titulo: String) {
    FACIL("Fácil"),
    MEDIO("Médio"),
    DIFICIL("Difícil")
}

data class ResultadoVerificacao(
    val temVencedor: Boolean = false,
    val vencedor: String = "",
    val linhaVitoria: List<Int> = emptyList(),
    val isEmpate: Boolean = false
)