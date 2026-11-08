package dev.raul.aplicativodecontroledehabitos.util

import dev.raul.aplicativodecontroledehabitos.dados.model.DiaCalendario
import dev.raul.aplicativodecontroledehabitos.dados.model.Habito
import dev.raul.aplicativodecontroledehabitos.dados.model.StatusDia
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object UtilitarioData {
    private val formatoIso = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun dataAtualIso(): String = formatoIso.format(Date())

    fun obterDiaDaSemana(dataIso: String): Int {
        return try {
            val data = formatoIso.parse(dataIso) ?: return Calendar.MONDAY
            val cal = Calendar.getInstance()
            cal.time = data
            cal.get(Calendar.DAY_OF_WEEK)
        } catch (_: Exception) {
            Calendar.MONDAY
        }
    }

    fun formatarCabecalhoData(dataIso: String = dataAtualIso()): String {
        return try {
            val data = formatoIso.parse(dataIso) ?: Date()
            val formatador = SimpleDateFormat("EEEE, d 'de' MMMM", Locale.forLanguageTag("pt-BR"))
            formatador.format(data)
        } catch (_: Exception) {
            "segunda-feira, 2 de novembro"
        }
    }

    fun obterDiasDaSemanaAtual(dataSelecionadaIso: String): List<DiaCalendario> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val diaAtualSemana = calendar.get(Calendar.DAY_OF_WEEK)
        val diasAteSegunda = if (diaAtualSemana == Calendar.SUNDAY) 6 else diaAtualSemana - Calendar.MONDAY
        calendar.add(Calendar.DAY_OF_MONTH, -diasAteSegunda)

        val hoje = dataAtualIso()
        val dias = mutableListOf<DiaCalendario>()
        val formatadorDiaSemana = SimpleDateFormat("EEE", Locale.forLanguageTag("pt-BR"))

        for (i in 0 until 7) {
            val data = calendar.time
            val dataIso = formatoIso.format(data)
            val diaSemanaAbrev = formatadorDiaSemana.format(data).replace(".", "").lowercase(Locale.forLanguageTag("pt-BR"))
            val diaDoMes = calendar.get(Calendar.DAY_OF_MONTH)

            dias.add(
                DiaCalendario(
                    dataIso = dataIso,
                    diaSemanaAbreviado = diaSemanaAbrev,
                    diaDoMes = diaDoMes,
                    ehHoje = dataIso == hoje,
                    estaSelecionado = dataIso == dataSelecionadaIso
                )
            )
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return dias
    }

    fun obterStatusUltimos14Dias(habito: Habito): List<StatusDia> {
        val calendar = Calendar.getInstance()
        val hoje = dataAtualIso()
        val lista = mutableListOf<StatusDia>()

        calendar.add(Calendar.DAY_OF_MONTH, -13)

        for (i in 0 until 14) {
            val dataIso = formatoIso.format(calendar.time)
            val diaDoMes = calendar.get(Calendar.DAY_OF_MONTH)
            val ehHoje = dataIso == hoje
            val foiConcluido = habito.foiConcluidoEm(dataIso)

            lista.add(
                StatusDia(
                    dataIso = dataIso,
                    numeroDia = diaDoMes,
                    ehHoje = ehHoje,
                    foiConcluido = foiConcluido
                )
            )
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return lista
    }

    fun calcularSequenciaAtual(habito: Habito): Int {
        if (habito.diasDaSemana.isEmpty()) return 0

        val calendar = Calendar.getInstance()
        val hojeIso = formatoIso.format(calendar.time)
        var sequencia = 0

        val hojeAgendado = habito.estaAgendadoParaData(hojeIso)
        if (hojeAgendado) {
            if (habito.foiConcluidoEm(hojeIso)) {
                sequencia++
            }
            calendar.add(Calendar.DAY_OF_MONTH, -1)
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, -1)
        }

        var limiteDias = 365
        while (limiteDias > 0) {
            val dataIso = formatoIso.format(calendar.time)
            if (habito.estaAgendadoParaData(dataIso)) {
                if (habito.foiConcluidoEm(dataIso)) {
                    sequencia++
                } else {
                    break
                }
            }
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            limiteDias--
        }
        return sequencia
    }

    fun calcularMelhorSequencia(habito: Habito): Int {
        if (habito.datasConcluidas.isEmpty()) return 0
        val datasOrdenadas = habito.datasConcluidas.sorted()
        var maiorSequencia = 1
        var sequenciaAtual = 1

        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()

        for (i in 1 until datasOrdenadas.size) {
            try {
                val d1 = formatoIso.parse(datasOrdenadas[i - 1]) ?: continue
                val d2 = formatoIso.parse(datasOrdenadas[i]) ?: continue
                cal1.time = d1
                cal2.time = d2

                var passoEncontrado = false
                while (cal1.timeInMillis < cal2.timeInMillis) {
                    cal1.add(Calendar.DAY_OF_MONTH, 1)
                    val dataPasso = formatoIso.format(cal1.time)
                    if (habito.estaAgendadoParaData(dataPasso)) {
                        if (dataPasso == datasOrdenadas[i]) {
                            passoEncontrado = true
                        }
                        break
                    }
                }

                if (passoEncontrado) {
                    sequenciaAtual++
                    if (sequenciaAtual > maiorSequencia) {
                        maiorSequencia = sequenciaAtual
                    }
                } else {
                    sequenciaAtual = 1
                }
            } catch (_: Exception) {
                sequenciaAtual = 1
            }
        }
        val atual = calcularSequenciaAtual(habito)
        return maxOf(maiorSequencia, atual)
    }

    fun calcularTaxaConclusao(habito: Habito): Int {
        val ultimos14 = obterStatusUltimos14Dias(habito)
        val diasAgendados = ultimos14.filter { habito.estaAgendadoParaData(it.dataIso) }
        val totalAgendados = diasAgendados.size
        if (totalAgendados == 0) return 100

        val totalConcluidos = diasAgendados.count { it.foiConcluido }
        return ((totalConcluidos.toDouble() / totalAgendados.toDouble()) * 100).toInt()
    }
}
