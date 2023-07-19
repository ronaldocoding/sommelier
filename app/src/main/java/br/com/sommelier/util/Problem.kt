package br.com.sommelier.util

interface Problem

data class AddDocumentProblem(val message: String): Problem
data class GetDocumentProblem(val message: String): Problem
data class NotFoundDocumentProblem(val message: String): Problem
data class UpdateDocumentProblem(val message: String): Problem
data class DeleteDocumentProblem(val message: String): Problem
data class RegisterUserProblem(val message: String): Problem
data class DeleteUserProblem(val message: String): Problem
data class NullResultProblem(val message: String): Problem
data class SignInUserProblem(val message: String): Problem
data class UpdateUserEmailProblem(val message: String): Problem
data class UpdateUserPasswordProblem(val message: String): Problem
data class NullUserProblem(val message: String): Problem
data class ReauthenticateUserProblem(val message: String): Problem
data class AlreadySignedOutUserProblem(val message: String): Problem
data class SendEmailVerificationProblem(val message: String): Problem
data class SendPasswordResetEmailProblem(val message: String): Problem