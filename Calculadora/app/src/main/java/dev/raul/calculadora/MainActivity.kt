package dev.raul.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fitOutside
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.raul.calculadora.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {
    var input by remember { mutableStateOf("0") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF282C2E))
    ) {
        Text(
            text = "Calculadora",
            color = Color(0xFFFFA500),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = input,
            color = Color.White,
            textAlign = TextAlign.End,
            fontSize = 40.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp)
        )

        CalculatorButtons(
            onKeyPress = { key ->
                when (key) {
                    "C" -> {
                        input = "0"
                    }
                    "⌫" -> {
                        if (input.length > 1) {
                            input = input.dropLast(1)
                        } else {
                            input = "0"
                        }
                    }
                    "=" -> {
                        val evalResult = verificarExpressao(input)
                        input = evalResult.removePrefix("= ")
                    }
                    "," -> {
                        val lastNumber = input.split(Regex("[+\\−×÷]")).last()
                        if (!lastNumber.contains(",")) {
                            input += ","
                        }
                    }
                    "+", "−", "×", "÷" -> {
                        if (input.isNotEmpty()) {
                            val lastChar = input.last()
                            if (lastChar in listOf('+', '−', '×', '÷')) {
                                input = input.dropLast(1) + key
                            } else if (lastChar != ',') {
                                input += key
                            }
                        }
                    }
                    else -> {
                        if (input == "0" || input.startsWith("Erro")) {
                            input = key
                        } else {
                            input += key
                        }
                    }
                }
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}

fun verificarExpressao(expression: String): String {
    try {
        val expr = expression.replace("×", "*").replace("÷", "/").replace("−", "-").replace(",", ".")
        
        val tokens = mutableListOf<String>()
        var currentNumber = ""
        for (i in expr.indices) {
            val char = expr[i]
            if (char in listOf('+', '-', '*', '/')) {
                if (char == '-' && (i == 0 || expr[i-1] in listOf('+', '-', '*', '/'))) {
                    currentNumber += char
                } else {
                    if (currentNumber.isNotEmpty()) {
                        tokens.add(currentNumber)
                        currentNumber = ""
                    }
                    tokens.add(char.toString())
                }
            } else {
                currentNumber += char
            }
        }
        if (currentNumber.isNotEmpty()) {
            tokens.add(currentNumber)
        }
        
        if (tokens.isNotEmpty() && tokens.last() in listOf("+", "-", "*", "/")) {
            tokens.removeAt(tokens.lastIndex)
        }

        var i = 0
        while (i < tokens.size) {
            val token = tokens[i]
            if (token == "*" || token == "/") {
                val left = tokens[i - 1].toDouble()
                val right = tokens[i + 1].toDouble()
                val res = if (token == "*") left * right else {
                    if (right == 0.0) return "= Erro ao dividir por 0"
                    left / right
                }
                tokens[i - 1] = res.toString()
                tokens.removeAt(i)
                tokens.removeAt(i)
            } else {
                i++
            }
        }

        i = 0
        while (i < tokens.size) {
            val token = tokens[i]
            if (token == "+" || token == "-") {
                val left = tokens[i - 1].toDouble()
                val right = tokens[i + 1].toDouble()
                val res = if (token == "+") left + right else left - right
                tokens[i - 1] = res.toString()
                tokens.removeAt(i)
                tokens.removeAt(i)
            } else {
                i++
            }
        }

        if (tokens.isEmpty()) return ""
        val resultValue = tokens.first().toDouble()
        val finalValue = if (resultValue == -0.0) 0.0 else resultValue

        val isInteger = finalValue % 1.0 == 0.0
        val formattedResult = if (isInteger) {
            finalValue.toLong().toString()
        } else {
            finalValue.toString().replace(".", ",")
        }
        return "= $formattedResult"
    } catch (e: Exception) {
        return "= Erro"
    }
}

@Composable
fun CalculatorButtons(onKeyPress: (String) -> Unit, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val spacing = 10.dp
        val colunas = 4
        val largura = (maxWidth - spacing * (colunas - 1)) / colunas

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                CalcButton("C", FnColor, largura * 2 + spacing, largura, onKeyPress)
                CalcButton("⌫", FnColor, largura, largura, onKeyPress)
                CalcButton("÷", OpColor, largura, largura, onKeyPress)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                CalcButton("7", NumColor, largura, largura, onKeyPress)
                CalcButton("8", NumColor, largura, largura, onKeyPress)
                CalcButton("9", NumColor, largura, largura, onKeyPress)
                CalcButton("×", OpColor, largura, largura, onKeyPress)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                CalcButton("4", NumColor, largura, largura, onKeyPress)
                CalcButton("5", NumColor, largura, largura, onKeyPress)
                CalcButton("6", NumColor, largura, largura, onKeyPress)
                CalcButton("−", OpColor, largura, largura, onKeyPress)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                CalcButton("1", NumColor, largura, largura, onKeyPress)
                CalcButton("2", NumColor, largura, largura, onKeyPress)
                CalcButton("3", NumColor, largura, largura, onKeyPress)
                CalcButton("+", OpColor, largura, largura, onKeyPress)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                CalcButton("0", NumColor, largura * 2 + spacing, largura, onKeyPress)
                CalcButton(",", NumColor, largura, largura, onKeyPress)
                CalcButton("=", EqColor, largura, largura, onKeyPress)
            }
        }
    }
}

@Composable
private fun CalcButton(
    label: String,
    color: Color,
    width: Dp,
    height: Dp,
    onKeyPress: (String) -> Unit
) {
    Button(
        onClick = { onKeyPress(label) },
        modifier = Modifier.size(width = width, height = height),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
    ) {
        Text(
            text = label,
            color = TextColor,
            fontSize = 19.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CalculadoraTheme {
        App()
    }
}