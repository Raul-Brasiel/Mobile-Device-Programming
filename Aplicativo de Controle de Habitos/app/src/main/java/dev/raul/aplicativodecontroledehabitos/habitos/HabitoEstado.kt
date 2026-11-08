package dev.raul.aplicativodecontroledehabitos.habitos

import dev.raul.aplicativodecontroledehabitos.dados.model.DiaCalendario
import dev.raul.aplicativodecontroledehabitos.dados.model.Habito
import dev.raul.aplicativodecontroledehabitos.util.UtilitarioData

data class HabitoEstado(
    val habitosDoDia: List<Habito> = emptyList(),
    val todosOsHabitos: List<Habito> = emptyList(),
    val dataSelecionadaIso: String = UtilitarioData.dataAtualIso(),
    val diasDaSemana: List<DiaCalendario> = emptyList(),
    val concluidosHoje: Int = 0,
    val totalHoje: Int = 0,
    val porcentagemProgressoHoje: Int = 0,
    val habitoSelecionado: Habito? = null,
    val cabecalhoDataFormatado: String = "",
    val ehHoje: Boolean = true
)
