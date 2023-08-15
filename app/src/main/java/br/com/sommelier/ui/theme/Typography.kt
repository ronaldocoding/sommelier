package br.com.sommelier.ui.theme

import androidx.compose.ui.text.TextStyle

@Suppress("unused")
object Typography {
    val displayLarge = TextStyle(
        fontFamily = FontStyle.carattereRegular,
        fontSize = FontSize.largest,
        lineHeight = LineHeight.normal,
        letterSpacing = LetterSpacing.smallest
    )

    val display = TextStyle(
        fontFamily = FontStyle.openSansMedium,
        fontSize = FontSize.extraLarge,
        lineHeight = LineHeight.largest,
        letterSpacing = LetterSpacing.smallest
    )

    val headline = TextStyle(
        fontFamily = FontStyle.openSansSemiBold,
        fontSize = FontSize.extraLarge,
        lineHeight = LineHeight.normal,
        letterSpacing = LetterSpacing.largest
    )

    val headerLarge = TextStyle(
        fontFamily = FontStyle.openSansSemiBold,
        fontSize = FontSize.large,
        lineHeight = LineHeight.large,
        letterSpacing = LetterSpacing.smallest
    )

    val headerMedium = TextStyle(
        fontFamily = FontStyle.openSansMedium,
        fontSize = FontSize.large,
        lineHeight = LineHeight.normal,
        letterSpacing = LetterSpacing.largest
    )

    val headerNormal = TextStyle(
        fontFamily = FontStyle.openSansMedium,
        fontSize = FontSize.medium,
        lineHeight = LineHeight.normal,
        letterSpacing = LetterSpacing.largest
    )

    val headerSmall = TextStyle(
        fontFamily = FontStyle.openSansMedium,
        fontSize = FontSize.small,
        lineHeight = LineHeight.normal,
        letterSpacing = LetterSpacing.largest
    )

    val bodyLarge = TextStyle(
        fontFamily = FontStyle.openSansRegular,
        fontSize = FontSize.normal,
        lineHeight = LineHeight.medium,
        letterSpacing = LetterSpacing.smallest
    )

    val bodyLargeBold = TextStyle(
        fontFamily = FontStyle.openSansBold,
        fontSize = FontSize.normal,
        lineHeight = LineHeight.medium,
        letterSpacing = LetterSpacing.smallest
    )

    val bodyMedium = TextStyle(
        fontFamily = FontStyle.openSansMedium,
        fontSize = FontSize.small,
        lineHeight = LineHeight.normal,
        letterSpacing = LetterSpacing.smallest
    )

    val bodyNormal = TextStyle(
        fontFamily = FontStyle.openSansRegular,
        fontSize = FontSize.small,
        lineHeight = LineHeight.normal,
        letterSpacing = LetterSpacing.smallest
    )

    val bodySmall = TextStyle(
        fontFamily = FontStyle.openSansRegular,
        fontSize = FontSize.extraSmall,
        lineHeight = LineHeight.medium,
        letterSpacing = LetterSpacing.normal
    )

    val caption = TextStyle(
        fontFamily = FontStyle.openSansRegular,
        fontSize = FontSize.smallest,
        lineHeight = LineHeight.medium,
        letterSpacing = LetterSpacing.normal
    )

    val label = TextStyle(
        fontFamily = FontStyle.openSansRegular,
        fontSize = FontSize.extraSmall,
        lineHeight = LineHeight.smallest,
        letterSpacing = LetterSpacing.smallest
    )
}
