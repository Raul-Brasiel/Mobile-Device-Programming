package dev.raul.sistemadeautenticacao.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _estado = MutableStateFlow(AuthState(estaLogado = firebaseAuth.currentUser != null))
    val estado: StateFlow<AuthState> = _estado.asStateFlow()
    val emailDoUsuario: String get() = firebaseAuth.currentUser?.email ?: ""

    private fun validarEmail(email: String): String? = when {
        email.isBlank() -> "Informe o e-mail"
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "E-mail inválido"
        else -> null
    }

    private fun validarSenha(senha: String): String? = when {
        senha.isBlank() -> "Informe a senha"
        senha.length < 6 -> "A senha deve ter pelo menos 6 caracteres"
        else -> null
    }

    private fun validarConfirmacao(senha: String, confirmacao: String): String? = when {
        confirmacao.isBlank() -> "Confirme a senha"
        senha != confirmacao -> "As senhas não coincidem"
        else -> null
    }

    fun limparMensagens() {
        _estado.update {
            it.copy(
                mensagemErro = null,
                mensagemInfo = null,
                erroEmail = null,
                erroSenha = null,
                erroConfirmacaoSenha = null
            )
        }
    }

    fun entrar(email: String, senha: String) {
        val emailLimpo = email.trim()
        val erroEmail = validarEmail(emailLimpo)
        val erroSenha = if (senha.isBlank()) "Informe a senha" else null

        if (erroEmail != null || erroSenha != null) {
            _estado.update {
                it.copy(erroEmail = erroEmail, erroSenha = erroSenha, mensagemErro = null)
            }
            return
        }

        viewModelScope.launch {
            _estado.update {
                it.copy(estaCarregando = true, mensagemErro = null, erroEmail = null, erroSenha = null)
            }
            try {
                firebaseAuth.signInWithEmailAndPassword(emailLimpo, senha).await()
                _estado.update { it.copy(estaLogado = true) }
            } catch (e: Exception) {
                _estado.update { it.copy(mensagemErro = traduzirErro(e)) }
            } finally {
                _estado.update { it.copy(estaCarregando = false) }
            }
        }
    }

    fun cadastrar(email: String, senha: String, confirmacaoSenha: String) {
        val emailLimpo = email.trim()
        val erroEmail = validarEmail(emailLimpo)
        val erroSenha = validarSenha(senha)
        val erroConfirmacao = validarConfirmacao(senha, confirmacaoSenha)

        if (erroEmail != null || erroSenha != null || erroConfirmacao != null) {
            _estado.update {
                it.copy(
                    erroEmail = erroEmail,
                    erroSenha = erroSenha,
                    erroConfirmacaoSenha = erroConfirmacao,
                    mensagemErro = null
                )
            }
            return
        }

        viewModelScope.launch {
            _estado.update {
                it.copy(
                    estaCarregando = true,
                    mensagemErro = null,
                    erroEmail = null,
                    erroSenha = null,
                    erroConfirmacaoSenha = null
                )
            }
            try {
                firebaseAuth.createUserWithEmailAndPassword(emailLimpo, senha).await()
                _estado.update { it.copy(estaLogado = true) }
            } catch (e: Exception) {
                _estado.update { it.copy(mensagemErro = traduzirErro(e)) }
            } finally {
                _estado.update { it.copy(estaCarregando = false) }
            }
        }
    }

    fun recuperarSenha(email: String) {
        val emailLimpo = email.trim()
        val erroEmail = validarEmail(emailLimpo)

        if (erroEmail != null) {
            _estado.update { it.copy(erroEmail = erroEmail, mensagemInfo = null) }
            return
        }

        viewModelScope.launch {
            _estado.update {
                it.copy(estaCarregando = true, mensagemErro = null, mensagemInfo = null, erroEmail = null)
            }
            try {
                firebaseAuth.sendPasswordResetEmail(emailLimpo).await()
                _estado.update {
                    it.copy(mensagemInfo = "Link enviado. Confira sua caixa de entrada.")
                }
            } catch (e: Exception) {
                _estado.update { it.copy(mensagemErro = traduzirErro(e)) }
            } finally {
                _estado.update { it.copy(estaCarregando = false) }
            }
        }
    }

    fun sair() {
        firebaseAuth.signOut()
        _estado.value = AuthState(estaLogado = false)
    }

    private fun traduzirErro(erro: Exception): String = when (erro) {
        is FirebaseAuthUserCollisionException -> "Este e-mail já está cadastrado."
        is FirebaseAuthWeakPasswordException -> "Senha muito fraca. Use pelo menos 6 caracteres."
        is FirebaseAuthInvalidCredentialsException -> "E-mail ou senha incorretos."
        is FirebaseAuthInvalidUserException -> "Conta não encontrada."
        is FirebaseNetworkException -> "Sem conexão com a internet."
        else -> "Algo deu errado. Tente novamente."
    }
}