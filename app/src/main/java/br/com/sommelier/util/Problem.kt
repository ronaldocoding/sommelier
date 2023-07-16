package br.com.sommelier.util

interface Problem

data class AddDocumentProblem(val message: String): Problem
data class GetDocumentProblem(val message: String): Problem
data class NotFoundDocumentProblem(val message: String): Problem
data class UpdateDocumentProblem(val message: String): Problem
data class DeleteDocumentProblem(val message: String): Problem