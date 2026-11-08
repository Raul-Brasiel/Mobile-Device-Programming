package dev.raul.aplicativodecontroledehabitos.dados.repositorio

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class RepositorioAuth {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    val usuarioAtual: FirebaseUser?
        get() = firebaseAuth.currentUser

    val estaLogado: Boolean
        get() = firebaseAuth.currentUser != null

    val emailUsuario: String
        get() = firebaseAuth.currentUser?.email ?: ""

    val nomeUsuario: String
        get() = firebaseAuth.currentUser?.displayName ?: ""

    suspend fun entrar(email: String, senha: String): Result<FirebaseUser?> {
        return try {
            val resultado = firebaseAuth.signInWithEmailAndPassword(email.trim(), senha).await()
            Result.success(resultado.user)
        } catch (e: Exception) {
            Result.failure(Exception(mapearErroFirebase(e)))
        }
    }

    suspend fun registrar(nome: String, email: String, senha: String): Result<FirebaseUser?> {
        return try {
            val resultado = firebaseAuth.createUserWithEmailAndPassword(email.trim(), senha).await()
            val usuario = resultado.user

            if (nome.isNotBlank() && usuario != null) {
                val atualizacao = UserProfileChangeRequest.Builder()
                    .setDisplayName(nome.trim())
                    .build()
                usuario.updateProfile(atualizacao).await()
            }
            Result.success(usuario)
        } catch (e: Exception) {
            Result.failure(Exception(mapearErroFirebase(e)))
        }
    }

    suspend fun enviarEmailRecuperacao(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email.trim()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(mapearErroFirebase(e)))
        }
    }

    fun sair() {
        firebaseAuth.signOut()
    }

    private fun mapearErroFirebase(e: Exception): String {
        return when (e) {
            is FirebaseAuthInvalidUserException -> "Usuário não encontrado com este e-mail."
            is FirebaseAuthInvalidCredentialsException -> "E-mail ou senha incorretos."
            is FirebaseAuthUserCollisionException -> "Já existe uma conta cadastrada com este e-mail."
            is FirebaseAuthWeakPasswordException -> "A senha deve conter no mínimo 6 caracteres."
            is FirebaseNetworkException -> "Erro de conexão. Verifique sua internet."
            else -> e.localizedMessage ?: "Ocorreu um erro inesperado. Tente novamente."
        }
    }
}
