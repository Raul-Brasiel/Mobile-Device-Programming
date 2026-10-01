package dev.raul.sistemadeautenticacao.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val EsquemaDeCoresEscuro = darkColorScheme(
    primary = Destaque,
    onPrimary = SobreDestaque,
    primaryContainer = DestaqueSuave,
    onPrimaryContainer = TextoDestaque,
    background = FundoEscuro,
    onBackground = TextoPrimario,
    surface = SuperficieEscura,
    onSurface = TextoPrimario,
    surfaceVariant = CampoEscuro,
    onSurfaceVariant = TextoSecundario,
    outline = BordaSutil,
    error = Erro,
    onError = FundoEscuro
)

@Composable
fun SistemaDeAutenticacaoTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val janela = (view.context as Activity).window
            WindowCompat.getInsetsController(janela, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = EsquemaDeCoresEscuro,
        typography = Tipografia,
        content = content
    )
}