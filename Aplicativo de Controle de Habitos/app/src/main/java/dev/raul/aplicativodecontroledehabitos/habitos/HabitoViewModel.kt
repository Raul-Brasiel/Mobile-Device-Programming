package dev.raul.aplicativodecontroledehabitos.habitos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.raul.aplicativodecontroledehabitos.dados.model.Habito
import dev.raul.aplicativodecontroledehabitos.dados.model.StatusDia
import dev.raul.aplicativodecontroledehabitos.dados.repositorio.RepositorioHabitos
import dev.raul.aplicativodecontroledehabitos.util.UtilitarioData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HabitoViewModel(aplicacao: Application) : AndroidViewModel(aplicacao) {

    private val repositorio = RepositorioHabitos(aplicacao.applicationContext)
    private val _dataSelecionadaIso = MutableStateFlow(UtilitarioData.dataAtualIso())
    private val _idHabitoSelecionado = MutableStateFlow<String?>(null)

    val estado: StateFlow<HabitoEstado> = combine(
        repositorio.habitos,
        _dataSelecionadaIso,
        _idHabitoSelecionado
    ){ habitos, dataSelecionadaIso, idHabitoSelecionado ->
        val hoje = UtilitarioData.dataAtualIso()
        val ehHoje = dataSelecionadaIso == hoje

        val habitosDoDia = habitos.filter { it.estaAgendadoParaData(dataSelecionadaIso) }
        val totalAgendados = habitosDoDia.size
        val concluidos = habitosDoDia.count { it.foiConcluidoEm(dataSelecionadaIso) }
        val porcentagem = if (totalAgendados > 0) {
            ((concluidos.toDouble() / totalAgendados.toDouble()) * 100).toInt()
        } else {
            0
        }

        val habitoAtual = habitos.find { it.id == idHabitoSelecionado }
        val diasSemana = UtilitarioData.obterDiasDaSemanaAtual(dataSelecionadaIso)
        val cabecalhoFormatado = UtilitarioData.formatarCabecalhoData(dataSelecionadaIso)

        HabitoEstado(
            habitosDoDia = habitosDoDia,
            todosOsHabitos = habitos,
            dataSelecionadaIso = dataSelecionadaIso,
            diasDaSemana = diasSemana,
            concluidosHoje = concluidos,
            totalHoje = totalAgendados,
            porcentagemProgressoHoje = porcentagem,
            habitoSelecionado = habitoAtual,
            cabecalhoDataFormatado = cabecalhoFormatado,
            ehHoje = ehHoje
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HabitoEstado()
    )

    fun selecionarData(dataIso: String) {
        _dataSelecionadaIso.value = dataIso
    }

    fun selecionarHabito(id: String?) {
        _idHabitoSelecionado.value = id
    }

    fun alternarConclusao(habitoId: String, dataIso: String = _dataSelecionadaIso.value) {
        repositorio.alternarConclusao(habitoId, dataIso)
    }

    fun criarHabito(
        nome: String,
        descricao: String,
        corHex: String,
        nomeIcone: String,
        diasSemana: List<Int>,
        lembreteAtivo: Boolean,
        horarioLembrete: String
    ) {
        val novo = Habito(
            nome = nome.trim(),
            descricao = descricao.trim(),
            corHex = corHex,
            nomeIcone = nomeIcone,
            diasDaSemana = diasSemana,
            lembreteAtivo = lembreteAtivo,
            horarioLembrete = horarioLembrete
        )
        repositorio.adicionar(novo)
    }

    fun atualizarHabito(
        habitoId: String,
        nome: String,
        descricao: String,
        corHex: String,
        nomeIcone: String,
        diasSemana: List<Int>,
        lembreteAtivo: Boolean,
        horarioLembrete: String
    ) {
        val existente = repositorio.buscarPorId(habitoId) ?: return
        val atualizado = existente.copy(
            nome = nome.trim(),
            descricao = descricao.trim(),
            corHex = corHex,
            nomeIcone = nomeIcone,
            diasDaSemana = diasSemana,
            lembreteAtivo = lembreteAtivo,
            horarioLembrete = horarioLembrete
        )
        repositorio.atualizar(atualizado)
    }

    fun excluirHabito(habitoId: String) {
        repositorio.excluir(habitoId)
        if (_idHabitoSelecionado.value == habitoId) {
            _idHabitoSelecionado.value = null
        }
    }

    fun obterSequenciaAtual(habito: Habito): Int = UtilitarioData.calcularSequenciaAtual(habito)
    fun obterMelhorSequencia(habito: Habito): Int = UtilitarioData.calcularMelhorSequencia(habito)
    fun obterTaxaConclusao(habito: Habito): Int = UtilitarioData.calcularTaxaConclusao(habito)
    fun obterUltimos14Dias(habito: Habito): List<StatusDia> = UtilitarioData.obterStatusUltimos14Dias(habito)
}
