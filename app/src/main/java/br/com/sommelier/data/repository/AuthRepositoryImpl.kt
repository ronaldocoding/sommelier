package br.com.sommelier.data.repository

import br.com.sommelier.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth

class AuthRepositoryImpl(private val firebaseAuth: FirebaseAuth) : AuthRepository {
}