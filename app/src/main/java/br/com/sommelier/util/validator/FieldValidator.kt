package br.com.sommelier.util.validator

private const val EMAIL_PATTERN = "[a-zA-Z0-9\\\\+\\\\.\\\\_\\\\%\\\\-\\\\+]{1,256}\\\\@[a-zA-Z0-9][a-zA-Z0-9\\\\-]{0,64}(\\\\.[a-zA-Z0-9][a-zA-Z0-9\\\\-]{0,25})+"
private const val DUPLICATED_COM_PATTERN = "(\\\\.com).*\\\\1"

fun isValidEmail(email: String): Boolean {
    val emailPattern = EMAIL_PATTERN.toRegex()
    val duplicatedComPattern = DUPLICATED_COM_PATTERN
    val containsDuplicatedCom = duplicatedComPattern.toRegex().containsMatchIn(email)
    return email.matches(emailPattern) && !containsDuplicatedCom
}

fun isValidPassword(password: String) = password.length >= 6
