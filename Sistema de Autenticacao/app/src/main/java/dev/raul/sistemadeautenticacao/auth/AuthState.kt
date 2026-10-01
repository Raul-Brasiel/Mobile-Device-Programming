package dev.raul.sistemadeautenticacao.auth

data class AuthState(
    val estaCarregando: Boolean = false,
    val estaLogado: Boolean = false,
    val mensagemErro: String? = null,
    val mensagemInfo: String? = null,
    val erroEmail: String? = null,
    val erroSenha: String? = null,
    val erroConfirmacaoSenha: String? = null
)