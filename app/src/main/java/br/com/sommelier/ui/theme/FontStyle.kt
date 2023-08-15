package br.com.sommelier.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import br.com.sommelier.R

@Immutable
object FontStyle {
    val carattereRegular = FontFamily(Font(R.font.carattere_regular, FontWeight.Normal))
    val openSansRegular = FontFamily(Font(R.font.open_sans_regular, FontWeight.Normal))
    val openSansMedium = FontFamily(Font(R.font.open_sans_medium, FontWeight.Medium))
    val openSansSemiBold = FontFamily(Font(R.font.open_sans_semi_bold, FontWeight.SemiBold))
    val openSansBold = FontFamily(Font(R.font.open_sans_bold, FontWeight.Bold))
}
