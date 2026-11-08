package dev.raul.aplicativodecontroledehabitos.dados.repositorio

import android.content.Context
import dev.raul.aplicativodecontroledehabitos.dados.local.PreferenciaHabitos
import dev.raul.aplicativodecontroledehabitos.dados.model.Habito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RepositorioHabitos(context: Context) {

    private val preferencias = PreferenciaHabitos(context)
    private val _habitos = MutableStateFlow<List<Habito>>(preferencias.carregarHabitos())
    val habitos: StateFlow<List<Habito>> = _habitos.asStateFlow()

    fun buscarPorId(id: String): Habito? {
        return _habitos.value.find { it.id == id }
    }

    fun adicionar(habito: Habito) {
        _habitos.update { listaAtual ->
            val atualizada = listaAtual + habito
            preferencias.salvarHabitos(atualizada)
            atualizada
        }
    }

    fun atualizar(habito: Habito) {
        _habitos.update { listaAtual ->
            val atualizada = listaAtual.map {
                if (it.id == habito.id) habito else it
            }
            preferencias.salvarHabitos(atualizada)
            atualizada
        }
    }

    fun excluir(id: String) {
        _habitos.update { listaAtual ->
            val atualizada = listaAtual.filterNot { it.id == id }
            preferencias.salvarHabitos(atualizada)
            atualizada
        }
    }

    fun alternarConclusao(id: String, dataIso: String) {
        _habitos.update { listaAtual ->
            val atualizada = listaAtual.map { habito ->
                if (habito.id == id) {
                    val novasDatas = if (habito.datasConcluidas.contains(dataIso)) {
                        habito.datasConcluidas - dataIso
                    } else {
                        habito.datasConcluidas + dataIso
                    }
                    habito.copy(datasConcluidas = novasDatas)
                } else {
                    habito
                }
            }
            preferencias.salvarHabitos(atualizada)
            atualizada
        }
    }
}
