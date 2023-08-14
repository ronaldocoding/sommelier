package br.com.sommelier.util.validator

import android.content.Context
import br.com.sommelier.R

fun isValidEmail(context: Context, email: String): Boolean {
    val emailPattern = context.getString(R.string.email_pattern).toRegex()
    val duplicatedComPattern = context.getString(R.string.duplicated_com_pattern)
    val containsDuplicatedCom = duplicatedComPattern.toRegex().containsMatchIn(email)
    return email.matches(emailPattern) && !containsDuplicatedCom
}

fun isValidPassword(password: String) = password.length >= 6
