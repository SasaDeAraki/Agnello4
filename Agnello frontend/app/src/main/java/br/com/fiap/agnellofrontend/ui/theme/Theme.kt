package br.com.fiap.agnellofrontend.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Moss90,
    onPrimary = Moss20,
    secondary = Sand90,
    onSecondary = Sand20,
    tertiary = Slate90,
    onTertiary = Slate20,
    background = Color(0xFF111A17),
    surface = Color(0xFF17211D),
    onBackground = Color(0xFFE8EFEA),
    onSurface = Color(0xFFE8EFEA)
)

private val LightColorScheme = lightColorScheme(
    primary = Moss40,
    onPrimary = Color.White,
    secondary = Sand40,
    onSecondary = Color.White,
    tertiary = Slate40,
    onTertiary = Color.White,
    background = Color(0xFFF4F7F5),
    surface = Color.White,
    onBackground = Color(0xFF18201D),
    onSurface = Color(0xFF18201D)
)

@Composable
fun AgnelloFrontendTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
