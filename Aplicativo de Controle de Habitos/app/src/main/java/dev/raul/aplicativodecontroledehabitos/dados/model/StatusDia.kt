package dev.raul.aplicativodecontroledehabitos.dados.model

data class StatusDia(
    val dataIso: String,
    val numeroDia: Int,
    val ehHoje: Boolean,
    val foiConcluido: Boolean
)

data class DiaCalendario(
    val dataIso: String,
    val diaSemanaAbreviado: String,
    val diaDoMes: Int,
    val ehHoje: Boolean,
    val estaSelecionado: Boolean
)
