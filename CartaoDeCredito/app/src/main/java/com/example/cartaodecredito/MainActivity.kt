package com.example.cartaodecredito

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val inputNome = findViewById<EditText>(R.id.inputNome)
        val inputNumero = findViewById<EditText>(R.id.inputNumero)
        val inputValidade = findViewById<EditText>(R.id.inputValidade)
        val inputCvv = findViewById<EditText>(R.id.inputCvv)

        val nomeCartao = findViewById<TextView>(R.id.textviewNomeFront)
        val card = findViewById<FrameLayout>(R.id.card)
        val cardFront = findViewById<View>(R.id.cardFront)
        val cardBack = findViewById<View>(R.id.cardBack)
        val numeroCartao = findViewById<TextView>(R.id.textviewNumeroBack)
        val validadeCartao = findViewById<TextView>(R.id.textviewValidadeBack_Date)
        val cvvCartao = findViewById<TextView>(R.id.textviewCvvBack_Numero)
        val imageFlag = findViewById<ImageView>(R.id.imageFlag)

        val scale = resources.displayMetrics.density
        card.cameraDistance = 8000 * scale
        var isFront = true
        var formatandoNumero = false
        var formatandoData = false

        fun virarCartao(){
            val degresToRotate = if (isFront) 180f else 0f

            card.animate()
                .rotationY(degresToRotate)
                .setDuration(400)
                .setUpdateListener { valueAnimator ->
                    if(valueAnimator.animatedFraction >= 0.5f){
                        if(isFront){
                            cardFront.visibility = View.INVISIBLE
                            cardBack.visibility = View.VISIBLE
                        }
                        else{
                            cardFront.visibility = View.VISIBLE
                            cardBack.visibility = View.INVISIBLE
                        }
                    }
                }
                .withEndAction {
                    isFront = !isFront
                }
                .start()
        }
        fun descobrirBandeira(numero: String): Int? {
            if (numero.isEmpty()) return null

            val prefixosElo = listOf("4011", "4312", "4389")

            if (prefixosElo.any { numero.startsWith(it) } || numero.startsWith("50") || numero.startsWith("63")) {
                return R.drawable.elo_white
            }

            if (numero.startsWith("4")) {
                return R.drawable.visa_white
            }

            if (numero.length >= 2) {
                val doisPrimeiros = numero.substring(0, 2).toIntOrNull() ?: 0
                if (doisPrimeiros in 51..55) {
                    return R.drawable.mastercard
                }
            }
            if (numero.length >= 4) {
                val quatroPrimeiros = numero.substring(0, 4).toIntOrNull() ?: 0
                if (quatroPrimeiros in 2221..2720) {
                    return R.drawable.mastercard
                }
            }
            return null
        }

        inputNome.doOnTextChanged { text, start, before, count ->

            if (text.isNullOrBlank()) {
                nomeCartao.text = "NOME DO TITULAR"
            }
            else {
                nomeCartao.text = text.toString().uppercase()
            }
        }
        inputNumero.doOnTextChanged { text, start, before, count ->

            if (text.isNullOrBlank()) {
                numeroCartao.text = "0000 0000 0000 0000"
            } else {
                numeroCartao.text = text.toString()
            }
            if (formatandoNumero) return@doOnTextChanged

            val textoLimpo = text.toString().replace(" ", "")
            val textoFormatado = textoLimpo.chunked(4).joinToString(" ")
            val idImagemBandeira = descobrirBandeira(textoLimpo)

            if (text.toString() != textoFormatado) {
                formatandoNumero = true
                inputNumero.setText(textoFormatado)
                inputNumero.setSelection(inputNumero.text.length)
                formatandoNumero = false
            }

            if (idImagemBandeira != null) {
                imageFlag.setImageResource(idImagemBandeira)
            } else {
                imageFlag.setImageDrawable(null)
            }
        }
        inputValidade.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrBlank()) {
                validadeCartao.text = "MM/AA"
            } else {
                validadeCartao.text = text.toString()
            }
            if (formatandoData) return@doOnTextChanged

            val textoLimpo = text.toString().replace("/", "")
            val textoFormatado = textoLimpo.chunked(2).joinToString("/")

            if (text.toString() != textoFormatado) {
                formatandoData = true
                inputValidade.setText(textoFormatado)
                inputValidade.setSelection(inputValidade.text.length)
                formatandoData = false
            }
        }
        inputCvv.doOnTextChanged { text, start, before, count ->

            if (text.isNullOrBlank()) {
                cvvCartao.text = "000"
            }
            else {
                cvvCartao.text = text.toString()
            }
        }

        inputNome.setOnFocusChangeListener { _, temFoco ->
            if (temFoco && !isFront) {
                virarCartao()
            } else if (!temFoco && isFront) {
                virarCartao()
            }

            if (!temFoco) {
                val textoDigitado = inputNome.text.toString().trim()

                if (textoDigitado.isNotEmpty() && textoDigitado.length < 3) {
                    inputNome.error = "O nome deve ter pelo menos 3 letras"
                } else {
                    inputNome.error = null
                }
            }
        }
        inputNumero.setOnFocusChangeListener { _, temFoco ->
            if (!temFoco) {
                val textoDigitado = inputNumero.text.toString().trim()

                if (textoDigitado.isNotEmpty() && textoDigitado.length < 19) {
                    inputNumero.error = "O número do cartão deve ter 16 dígitos"
                } else {
                    inputNumero.error = null
                }
            }
        }
        inputValidade.setOnFocusChangeListener { _, temFoco ->
            if (!temFoco) {
                val textoDigitado = inputValidade.text.toString().trim()

                if (textoDigitado.isNotEmpty() && textoDigitado.length < 5) {
                    inputValidade.error = "A validade deve ter mês e ano"
                } else {
                    inputValidade.error = null
                }
            }
        }
        inputCvv.setOnFocusChangeListener { _, temFoco ->
            if (!temFoco) {
                val textoDigitado = inputCvv.text.toString().trim()

                if (textoDigitado.isNotEmpty() && textoDigitado.length < 3) {
                    inputCvv.error = "O cvv deve ter 3 dígitos"
                } else {
                    inputCvv.error = null
                }
            }
        }
    }
}