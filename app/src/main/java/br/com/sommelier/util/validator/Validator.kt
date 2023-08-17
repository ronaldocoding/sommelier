package br.com.sommelier.util.validator

fun isValidName(name: String) = name.length >= 3

fun isValidPassword(password: String) = password.length >= 6

fun passwordsMatch(password: String, passwordConfirmation: String) =
    password == passwordConfirmation
