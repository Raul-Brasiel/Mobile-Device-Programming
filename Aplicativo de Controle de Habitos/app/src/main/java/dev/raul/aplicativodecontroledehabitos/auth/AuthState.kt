package dev.raul.aplicativodecontroledehabitos.auth

data class AuthState(
    val estaCarregando: Boolean = false,
    val estaLogado: Boolean = false,
    val emailUsuario: String = "",
    val nomeUsuario: String = "",
    val mensagemErro: String? = null,
    val mensagemInfo: String? = null,
    val erroEmail: String? = null,
    val erroSenha: String? = null,
    val erroConfirmacaoSenha: String? = null,
    val erroNome: String? = null
)
