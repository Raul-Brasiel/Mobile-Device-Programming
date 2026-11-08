package dev.raul.aplicativodecontroledehabitos.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.raul.aplicativodecontroledehabitos.dados.repositorio.RepositorioAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repositorioAuth: RepositorioAuth = RepositorioAuth()
) : ViewModel() {

    private val _estado = MutableStateFlow(
        AuthState(
            estaLogado = repositorioAuth.estaLogado,
            emailUsuario = repositorioAuth.emailUsuario,
            nomeUsuario = repositorioAuth.nomeUsuario
        )
    )
    val estado: StateFlow<AuthState> = _estado.asStateFlow()

    fun limparMensagens() {
        _estado.update {
            it.copy(
                mensagemErro = null,
                mensagemInfo = null,
                erroEmail = null,
                erroSenha = null,
                erroConfirmacaoSenha = null,
                erroNome = null
            )
        }
    }

    fun entrar(email: String, senha: String, aoSucesso: () -> Unit = {}) {
        val erroEmail = validarEmail(email)
        val erroSenha = validarSenha(senha)

        if (erroEmail != null || erroSenha != null) {
            _estado.update {
                it.copy(
                    erroEmail = erroEmail,
                    erroSenha = erroSenha,
                    mensagemErro = null
                )
            }
            return
        }

        _estado.update { it.copy(estaCarregando = true, mensagemErro = null, mensagemInfo = null) }

        viewModelScope.launch {
            val resultado = repositorioAuth.entrar(email, senha)
            resultado.fold(
                onSuccess = { usuario ->
                    _estado.update {
                        it.copy(
                            estaCarregando = false,
                            estaLogado = true,
                            emailUsuario = usuario?.email ?: "",
                            nomeUsuario = usuario?.displayName ?: "",
                            mensagemErro = null
                        )
                    }
                    aoSucesso()
                },
                onFailure = { erro ->
                    _estado.update {
                        it.copy(
                            estaCarregando = false,
                            mensagemErro = erro.message ?: "Falha ao realizar login."
                        )
                    }
                }
            )
        }
    }

    fun registrar(
        nome: String,
        email: String,
        senha: String,
        confirmacaoSenha: String,
        aoSucesso: () -> Unit = {}
    ) {
        val erroNome = if (nome.isBlank()) "Informe seu nome completo" else null
        val erroEmail = validarEmail(email)
        val erroSenha = validarSenha(senha)
        val erroConfirmacao = when {
            confirmacaoSenha.isBlank() -> "Confirme sua senha"
            senha != confirmacaoSenha -> "As senhas não coincidem"
            else -> null
        }

        if (erroNome != null || erroEmail != null || erroSenha != null || erroConfirmacao != null) {
            _estado.update {
                it.copy(
                    erroNome = erroNome,
                    erroEmail = erroEmail,
                    erroSenha = erroSenha,
                    erroConfirmacaoSenha = erroConfirmacao,
                    mensagemErro = null
                )
            }
            return
        }

        _estado.update { it.copy(estaCarregando = true, mensagemErro = null, mensagemInfo = null) }

        viewModelScope.launch {
            val resultado = repositorioAuth.registrar(nome, email, senha)
            resultado.fold(
                onSuccess = { usuario ->
                    _estado.update {
                        it.copy(
                            estaCarregando = false,
                            estaLogado = true,
                            emailUsuario = usuario?.email ?: "",
                            nomeUsuario = nome.trim(),
                            mensagemErro = null
                        )
                    }
                    aoSucesso()
                },
                onFailure = { erro ->
                    _estado.update {
                        it.copy(
                            estaCarregando = false,
                            mensagemErro = erro.message ?: "Falha ao criar conta."
                        )
                    }
                }
            )
        }
    }

    fun enviarEmailRecuperacao(email: String, aoSucesso: () -> Unit = {}) {
        val erroEmail = validarEmail(email)
        if (erroEmail != null) {
            _estado.update { it.copy(erroEmail = erroEmail, mensagemErro = null) }
            return
        }

        _estado.update { it.copy(estaCarregando = true, mensagemErro = null, mensagemInfo = null) }

        viewModelScope.launch {
            val resultado = repositorioAuth.enviarEmailRecuperacao(email)
            resultado.fold(
                onSuccess = {
                    _estado.update {
                        it.copy(
                            estaCarregando = false,
                            mensagemInfo = "E-mail de recuperação enviado com sucesso!",
                            mensagemErro = null
                        )
                    }
                    aoSucesso()
                },
                onFailure = { erro ->
                    _estado.update {
                        it.copy(
                            estaCarregando = false,
                            mensagemErro = erro.message ?: "Não foi possível enviar o e-mail de recuperação."
                        )
                    }
                }
            )
        }
    }

    fun sair(aoDeslogar: () -> Unit = {}) {
        repositorioAuth.sair()
        _estado.update {
            AuthState(estaLogado = false)
        }
        aoDeslogar()
    }

    private fun validarEmail(email: String): String? {
        return when {
            email.isBlank() -> "Informe o e-mail"
            !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "E-mail inválido"
            else -> null
        }
    }

    private fun validarSenha(senha: String): String? {
        return when {
            senha.isBlank() -> "Informe a senha"
            senha.length < 6 -> "A senha deve ter pelo menos 6 caracteres"
            else -> null
        }
    }
}
