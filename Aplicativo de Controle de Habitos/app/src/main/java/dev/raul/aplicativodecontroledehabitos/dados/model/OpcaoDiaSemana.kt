package dev.raul.aplicativodecontroledehabitos.dados.model

import java.util.Calendar

data class OpcaoDiaSemana(
    val diaCalendario: Int,
    val letra: String,
    val nomeCurto: String
)

val ListaOpcoesDiasSemana = listOf(
    OpcaoDiaSemana(Calendar.MONDAY, "S", "seg"),
    OpcaoDiaSemana(Calendar.TUESDAY, "T", "ter"),
    OpcaoDiaSemana(Calendar.WEDNESDAY, "Q", "qua"),
    OpcaoDiaSemana(Calendar.THURSDAY, "Q", "qui"),
    OpcaoDiaSemana(Calendar.FRIDAY, "S", "sex"),
    OpcaoDiaSemana(Calendar.SATURDAY, "S", "sáb"),
    OpcaoDiaSemana(Calendar.SUNDAY, "D", "dom")
)
