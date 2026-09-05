package com.example.jogodavelha

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    var vezDoX = true
    var contPlayerX = 0
    var contPlayerO = 0
    var contEmpate = 0
    lateinit var status: TextView
    lateinit var txtContPlayerX: TextView
    lateinit var txtContPlayerO: TextView
    lateinit var txtEmpate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        status = findViewById(R.id.textStatus)
        status.text = "Vez do jogador X"
        status.setTextColor(Color.RED)

        val grid = findViewById<GridLayout>(R.id.gridSquares)
        val btnReset = findViewById<Button>(R.id.btnReset)
        txtContPlayerX = findViewById(R.id.textPlayerX)
        txtContPlayerO = findViewById(R.id.textPlayerO)
        txtEmpate = findViewById(R.id.textEmpate)

        txtContPlayerX.text = "Jogador X: 0"
        txtContPlayerO.text = "Jogador O: 0"
        txtEmpate.text = "Empate: 0"

        for (i in 0 until grid.childCount) {
            val square = grid.getChildAt(i)

            if (square is Button) {
                square.setOnClickListener { viewClicked ->
                    val botao = viewClicked as Button

                    if (botao.text.isEmpty()) {
                        if(vezDoX){
                            botao.text = "X"
                            status.text = "Vez do jogador O"
                            status.setTextColor(Color.BLUE)
                        }
                        else{
                            botao.text = "O"
                            status.text = "Vez do jogador X"
                            status.setTextColor(Color.RED)
                        }
                        verificarVencedor(grid)

                        vezDoX = !vezDoX
                    }
                }
            }
        }

        btnReset.setOnClickListener{
            mostrarAlertaReset(grid)
        }
    }

    private fun bloquearBotoes(grid: GridLayout) {
        for (i in 0 until grid.childCount) {
            val square = grid.getChildAt(i)
            if (square is Button) {
                square.isClickable = false
            }
        }
    }

    private fun verificarVencedor(grid: GridLayout) {
        val botoes = Array(9) { i ->
            val botao = grid.getChildAt(i) as Button
            botao.text.toString()
        }

        val combinacoesVitoria = arrayOf(
            intArrayOf(0, 1, 2),
            intArrayOf(3, 4, 5),
            intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6),
            intArrayOf(1, 4, 7),
            intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8),
            intArrayOf(2, 4, 6)
        )

        var teveVencedor = false
        var nomeVencedor = ""

        for (combinacao in combinacoesVitoria) {
            val a = combinacao[0]
            val b = combinacao[1]
            val c = combinacao[2]

            if (botoes[a].isNotEmpty() && botoes[a] == botoes[b] && botoes[a] == botoes[c]) {
                val botaoA = grid.getChildAt(a) as Button
                val botaoB = grid.getChildAt(b) as Button
                val botaoC = grid.getChildAt(c) as Button

                teveVencedor = true
                nomeVencedor = botoes[a]

                if(nomeVencedor == "X"){
                    botaoA.setTextColor(Color.RED)
                    botaoB.setTextColor(Color.RED)
                    botaoC.setTextColor(Color.RED)
                }
                else{
                    botaoA.setTextColor(Color.BLUE)
                    botaoB.setTextColor(Color.BLUE)
                    botaoC.setTextColor(Color.BLUE)
                }
            }
        }

        if (teveVencedor) {
            bloquearBotoes(grid)
            status.text = "O jogador ${nomeVencedor} venceu!"
            status.setTextColor(if(nomeVencedor == "X") Color.RED else Color.BLUE)
            atualizaPlacar(nomeVencedor)

            return
        }

        if (botoes.none { it.isEmpty() }) {
            contEmpate++
            status.text = "Empate!"
            status.setTextColor(Color.GREEN)
            txtEmpate.text = "Empate: $contEmpate"
        }
    }

    private fun mostrarAlertaReset(grid: GridLayout){
        AlertDialog.Builder(this)
            .setTitle("Resetar jogo")
            .setMessage("Deseja reiniciar o jogo?")
            .setPositiveButton("Sim") { _, _ ->
                for (i in 0 until grid.childCount) {
                    val square = grid.getChildAt(i)

                    if(square is Button){
                        square.text = ""
                        square.setTextColor(Color.BLACK)
                        square.isClickable = true
                    }
                }
                vezDoX = true
                status.text = "Vez do jogador X"
                status.setTextColor(Color.RED)
            }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun atualizaPlacar(nomeVencedor: String){
        if(nomeVencedor == "X"){
            contPlayerX++
            txtContPlayerX.text = "Jogador X: $contPlayerX"
        }
        else{
            contPlayerO++
            txtContPlayerO.text = "Jogador O: $contPlayerO"
        }
    }
}