package raul.contador

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    var contador = 0
    var contadorVelocidade = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textView = findViewById<TextView>(R.id.textview)
        val btnAumentar = findViewById<Button>(R.id.btnAumentar)
        val btnDiminuir = findViewById<Button>(R.id.btnDiminuir)
        val btnReset = findViewById<Button>(R.id.btnReset)
        val btnVelocidade = findViewById<Button>(R.id.btnVelocidade)

        textView.text = "$contador"
        btnVelocidade.text = "$contadorVelocidade" + "x"

        btnAumentar.setOnClickListener {
            contador = contador + contadorVelocidade
            textView.text = "$contador"
        }

        btnDiminuir.setOnClickListener {
            if (contador - contadorVelocidade < 0){
                mostrarAlertaNumeroNegativo()
            }
            else{
                contador = contador - contadorVelocidade
                textView.text = "$contador"
            }
        }

        btnReset.setOnClickListener {
            mostrarAlertaReset(textView, btnVelocidade)
        }

        btnVelocidade.setOnClickListener {
            contadorVelocidade++
            btnVelocidade.text = "$contadorVelocidade" + "x"
        }
    }

    private fun mostrarAlertaNumeroNegativo(){
        AlertDialog.Builder(this)
            .setTitle("Número negativo")
            .setMessage("O número ficará menor que 0!")
            .setNegativeButton("Ok", null)
            .show()
    }

    private fun mostrarAlertaReset(textView: TextView, btnVelocidade: Button){
        AlertDialog.Builder(this)
            .setTitle("Resetar contagem")
            .setMessage("Deseja reiniciar a contagem em 0?")
            .setPositiveButton("Sim") { _, _ ->
                contador = 0
                contadorVelocidade = 1
                textView.text = "$contador"
                btnVelocidade.text = "$contadorVelocidade" + "x"
            }
            .setNegativeButton("Não", null)
            .show()
    }
}