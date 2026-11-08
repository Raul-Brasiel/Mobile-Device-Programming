package dev.raul.aplicativodecontroledehabitos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.raul.aplicativodecontroledehabitos.navigation.AppNavigation
import dev.raul.aplicativodecontroledehabitos.ui.theme.AplicativoDeControleDeHabitosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AplicativoDeControleDeHabitosTheme {
                AppNavigation()
            }
        }
    }
}
