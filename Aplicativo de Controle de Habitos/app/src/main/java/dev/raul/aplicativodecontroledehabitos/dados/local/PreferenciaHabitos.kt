package dev.raul.aplicativodecontroledehabitos.dados.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.raul.aplicativodecontroledehabitos.dados.model.Habito
import dev.raul.aplicativodecontroledehabitos.util.UtilitarioData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale.getDefault

class PreferenciaHabitos(context: Context) {

    private val preferencias: SharedPreferences =
        context.getSharedPreferences("habitos_preferencias", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val chaveHabitos = "lista_habitos_salvos"

    fun carregarHabitos(): List<Habito> {
        val json = preferencias.getString(chaveHabitos, null)
        if (json.isNullOrBlank()) {
            val padrao = criarHabitosPadrao()
            salvarHabitos(padrao)
            return padrao
        }
        return try {
            val tipo = object : TypeToken<List<Habito>>() {}.type
            gson.fromJson<List<Habito>>(json, tipo) ?: criarHabitosPadrao()
        } catch (_: Exception) {
            criarHabitosPadrao()
        }
    }

    fun salvarHabitos(habitos: List<Habito>) {
        val json = gson.toJson(habitos)
        preferencias.edit().putString(chaveHabitos, json).apply()
    }

    private fun criarHabitosPadrao(): List<Habito> {
        val hoje = UtilitarioData.dataAtualIso()

        val datasAgua = mutableSetOf<String>()
        val calAgua = Calendar.getInstance()
        val formatador = SimpleDateFormat("yyyy-MM-dd", getDefault())

        for (i in 0 until 7) {
            datasAgua.add(formatador.format(calAgua.time))
            calAgua.add(Calendar.DAY_OF_MONTH, -1)
        }
        calAgua.add(Calendar.DAY_OF_MONTH, -1)
        for (i in 0 until 5) {
            datasAgua.add(formatador.format(calAgua.time))
            calAgua.add(Calendar.DAY_OF_MONTH, -1)
        }

        val datasLeitura = mutableSetOf<String>()
        val calLeitura = Calendar.getInstance()
        for (i in 0 until 4) {
            datasLeitura.add(formatador.format(calLeitura.time))
            calLeitura.add(Calendar.DAY_OF_MONTH, -1)
        }

        val datasTreino = mutableSetOf<String>()
        val calTreino = Calendar.getInstance()
        calTreino.add(Calendar.DAY_OF_MONTH, -1)
        datasTreino.add(formatador.format(calTreino.time))
        calTreino.add(Calendar.DAY_OF_MONTH, -2)
        datasTreino.add(formatador.format(calTreino.time))

        // Estudo
        val datasEstudo = mutableSetOf<String>()
        val calEstudo = Calendar.getInstance()
        calEstudo.add(Calendar.DAY_OF_MONTH, -1)
        datasEstudo.add(formatador.format(calEstudo.time))

        return listOf(
            Habito(
                id = "1",
                nome = "Beber 2 L de água",
                descricao = "Um copo ao acordar e um a cada refeição.",
                corHex = "#0088A9",
                nomeIcone = "water",
                diasDaSemana = listOf(1, 2, 3, 4, 5, 6, 7),
                lembreteAtivo = true,
                horarioLembrete = "08:00",
                datasConcluidas = datasAgua
            ),
            Habito(
                id = "2",
                nome = "Ler 20 páginas",
                descricao = "Livro de desenvolvimento pessoal antes de dormir.",
                corHex = "#6D28D9",
                nomeIcone = "book",
                diasDaSemana = listOf(1, 2, 3, 4, 5, 6, 7),
                lembreteAtivo = true,
                horarioLembrete = "21:00",
                datasConcluidas = datasLeitura
            ),
            Habito(
                id = "3",
                nome = "Treino",
                descricao = "Musculação e aeróbico.",
                corHex = "#C2410C",
                nomeIcone = "fitness",
                diasDaSemana = listOf(2, 4, 6),
                lembreteAtivo = false,
                horarioLembrete = "18:00",
                datasConcluidas = datasTreino
            ),
            Habito(
                id = "4",
                nome = "Estudar",
                descricao = "Desenvolvimento Android e Kotlin.",
                corHex = "#A16207",
                nomeIcone = "study",
                diasDaSemana = listOf(2, 3, 4, 5, 6),
                lembreteAtivo = false,
                horarioLembrete = "20:00",
                datasConcluidas = datasEstudo
            )
        )
    }
}
