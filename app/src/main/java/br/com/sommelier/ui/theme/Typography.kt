package br.com.sommelier.ui.theme

import androidx.compose.ui.text.TextStyle

@Suppress("unused")
object Typography {
    val display = TextStyle(
        fontFamily = FontStyle.poppinsMedium,
        fontSize = FontSize.largest,
        lineHeight = LineHeight.largest,
        letterSpacing = LetterSpacing.smallest
    )

    val headline = TextStyle(
        fontFamily = FontStyle.poppinsSemiBold,
        fontSize = FontSize.largest,
        lineHeight = LineHeight.normal,
        letterSpacing = LetterSpacing.largest
    )

    val headerLarge = TextStyle(
        fontFamily = FontStyle.poppinsSemiBold,
        fontSize = FontSize.large,
        lineHeight = LineHeight.large,
        letterSpacing = LetterSpacing.smallest
    )

    val headerMedium = TextStyle(
        fontFamily = FontStyle.poppinsMedium,
        fontSize = FontSize.large,
        lineHeight = LineHeight.normal,
        letterSpacing = LetterSpacing.largest
    )

    val headerNormal = TextStyle(
        fontFamily = FontStyle.poppinsMedium,
        fontSize = FontSize.medium,
        lineHeight = LineHeight.normal,
        letterSpacing = LetterSpacing.largest
    )

    val headerSmall = TextStyle(
        fontFamily = FontStyle.poppinsMedium,
        fontSize = FontSize.small,
        lineHeight = LineHeight.normal,
        letterSpacing = LetterSpacing.largest
    )

    val bodyLarge = TextStyle(
        fontFamily = FontStyle.poppinsRegular,
        fontSize = FontSize.normal,
        lineHeight = LineHeight.medium,
        letterSpacing = LetterSpacing.smallest
    )

    val bodyLargeBold = TextStyle(
        fontFamily = FontStyle.poppinsBold,
        fontSize = FontSize.normal,
        lineHeight = LineHeight.medium,
        letterSpacing = LetterSpacing.smallest
    )

    val bodyMedium = TextStyle(
        fontFamily = FontStyle.poppinsMedium,
        fontSize = FontSize.small,
        lineHeight = LineHeight.normal,
        letterSpacing = LetterSpacing.smallest
    )

    val bodyNormal = TextStyle(
        fontFamily = FontStyle.poppinsRegular,
        fontSize = FontSize.small,
        lineHeight = LineHeight.normal,
        letterSpacing = LetterSpacing.small
    )

    val bodySmall = TextStyle(
        fontFamily = FontStyle.poppinsRegular,
        fontSize = FontSize.extraSmall,
        lineHeight = LineHeight.medium,
        letterSpacing = LetterSpacing.normal
    )

    val caption = TextStyle(
        fontFamily = FontStyle.poppinsRegular,
        fontSize = FontSize.smallest,
        lineHeight = LineHeight.medium,
        letterSpacing = LetterSpacing.normal
    )

    val label = TextStyle(
        fontFamily = FontStyle.poppinsRegular,
        fontSize = FontSize.extraSmall,
        lineHeight = LineHeight.smallest,
        letterSpacing = LetterSpacing.normal
    )
}
