package dev.raul.aplicativodecontroledehabitos.dados.model

import dev.raul.aplicativodecontroledehabitos.util.UtilitarioData
import java.util.Calendar
import java.util.UUID

data class Habito(
    val id: String = UUID.randomUUID().toString(),
    val nome: String,
    val descricao: String = "",
    val corHex: String = "#0088A9",
    val nomeIcone: String = "water",
    val diasDaSemana: List<Int> = listOf(2, 3, 4, 5, 6),
    val lembreteAtivo: Boolean = false,
    val horarioLembrete: String = "08:00",
    val datasConcluidas: Set<String> = emptySet(),
    val criadoEm: Long = System.currentTimeMillis()
) {
    fun foiConcluidoEm(dataIso: String): Boolean = datasConcluidas.contains(dataIso)

    fun estaAgendadoParaData(dataIso: String): Boolean {
        if (diasDaSemana.isEmpty()) return true
        val diaSemana = UtilitarioData.obterDiaDaSemana(dataIso)
        return diasDaSemana.contains(diaSemana)
    }

    fun frequenciaFormatada(): String {
        if (diasDaSemana.size == 7) return "Todos os dias"
        if (diasDaSemana.containsAll(listOf(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY)) && diasDaSemana.size == 5) {
            return "Seg a sex"
        }
        if (diasDaSemana.containsAll(listOf(Calendar.SATURDAY, Calendar.SUNDAY)) && diasDaSemana.size == 2) {
            return "Fins de semana"
        }

        val nomesDias = mapOf(
            Calendar.MONDAY to "Seg",
            Calendar.TUESDAY to "Ter",
            Calendar.WEDNESDAY to "Qua",
            Calendar.THURSDAY to "Qui",
            Calendar.FRIDAY to "Sex",
            Calendar.SATURDAY to "Sáb",
            Calendar.SUNDAY to "Dom"
        )

        val ordemSemana = listOf(
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.SATURDAY,
            Calendar.SUNDAY
        )

        val diasOrdenados = diasDaSemana.sortedBy { ordemSemana.indexOf(it) }
        return diasOrdenados.mapNotNull { nomesDias[it] }.joinToString(", ")
    }
}
