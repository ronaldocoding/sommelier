package br.com.sommelier.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import br.com.sommelier.R

@Immutable
object FontStyle {
    val poppinsRegular = FontFamily(Font(R.font.poppins_regular, FontWeight.Normal))
    val poppinsMedium = FontFamily(Font(R.font.poppins_medium, FontWeight.Medium))
    val poppinsSemiBold = FontFamily(Font(R.font.poppins_semi_bold, FontWeight.SemiBold))
}
