package dev.raul.aplicativodecontroledehabitos.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.raul.aplicativodecontroledehabitos.auth.AuthViewModel
import dev.raul.aplicativodecontroledehabitos.habitos.HabitoViewModel
import dev.raul.aplicativodecontroledehabitos.ui.telas.DetalhesHabito
import dev.raul.aplicativodecontroledehabitos.ui.telas.EsqueceuASenha
import dev.raul.aplicativodecontroledehabitos.ui.telas.FormularioHabito
import dev.raul.aplicativodecontroledehabitos.ui.telas.ListaHabitos
import dev.raul.aplicativodecontroledehabitos.ui.telas.Login
import dev.raul.aplicativodecontroledehabitos.ui.telas.Registrar

object Rotas {
    const val LOGIN = "login"
    const val REGISTRAR = "registrar"
    const val ESQUECEU_SENHA = "esqueceu_senha"
    const val LISTA_HABITOS = "lista_habitos"
    const val NOVO_HABITO = "novo_habito"
    const val DETALHES_HABITO = "detalhes_habito/{habitoId}"
    const val EDITAR_HABITO = "editar_habito/{habitoId}"

    fun detalhes(habitoId: String) = "detalhes_habito/$habitoId"
    fun editar(habitoId: String) = "editar_habito/$habitoId"
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = viewModel(),
    habitoViewModel: HabitoViewModel = viewModel()
) {
    val navController = rememberNavController()
    val estadoAuth by authViewModel.estado.collectAsState()

    val rotaInicial = remember {
        if (authViewModel.estado.value.estaLogado) Rotas.LISTA_HABITOS else Rotas.LOGIN
    }

    LaunchedEffect(estadoAuth.estaLogado) {
        val rotaAtual = navController.currentBackStackEntry?.destination?.route ?: return@LaunchedEffect

        if (estadoAuth.estaLogado && rotaAtual == Rotas.LOGIN) {
            navController.navigate(Rotas.LISTA_HABITOS) {
                popUpTo(0) { inclusive = true }
            }
        } else if (!estadoAuth.estaLogado && rotaAtual != Rotas.LOGIN && rotaAtual != Rotas.REGISTRAR && rotaAtual != Rotas.ESQUECEU_SENHA) {
            navController.navigate(Rotas.LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = rotaInicial
    ) {
        composable(Rotas.LOGIN){
            Login(
                viewModel = authViewModel,
                aoIrParaRegistrar = {
                    navController.navigate(Rotas.REGISTRAR)
                },
                aoIrParaEsqueceuSenha = {
                    navController.navigate(Rotas.ESQUECEU_SENHA)
                },
                aoLoginSucesso = {
                    navController.navigate(Rotas.LISTA_HABITOS) {
                        popUpTo(Rotas.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Rotas.REGISTRAR){
            Registrar(
                viewModel = authViewModel,
                aoVoltar = {
                    navController.popBackStack()
                },
                aoRegistroSucesso = {
                    navController.navigate(Rotas.LISTA_HABITOS) {
                        popUpTo(Rotas.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Rotas.ESQUECEU_SENHA){
            EsqueceuASenha(
                viewModel = authViewModel,
                aoVoltar = {
                    navController.popBackStack()
                }
            )
        }

        composable(Rotas.LISTA_HABITOS){
            ListaHabitos(
                viewModel = habitoViewModel,
                aoIrParaNovoHabito = {
                    navController.navigate(Rotas.NOVO_HABITO)
                },
                aoIrParaDetalhes = { id ->
                    navController.navigate(Rotas.detalhes(id))
                },
                aoSair = {
                    authViewModel.sair {
                        navController.navigate(Rotas.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Rotas.NOVO_HABITO){
            FormularioHabito(
                habitoId = null,
                viewModel = habitoViewModel,
                aoVoltar = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Rotas.DETALHES_HABITO,
            arguments = listOf(navArgument("habitoId") { type = NavType.StringType })
        ){ backStackEntry ->
            val habitoId = backStackEntry.arguments?.getString("habitoId") ?: ""
            DetalhesHabito(
                habitoId = habitoId,
                viewModel = habitoViewModel,
                aoVoltar = {
                    navController.popBackStack()
                },
                aoEditar = { id ->
                    navController.navigate(Rotas.editar(id))
                }
            )
        }

        composable(
            route = Rotas.EDITAR_HABITO,
            arguments = listOf(navArgument("habitoId") { type = NavType.StringType })
        ){ backStackEntry ->
            val habitoId = backStackEntry.arguments?.getString("habitoId") ?: ""
            FormularioHabito(
                habitoId = habitoId,
                viewModel = habitoViewModel,
                aoVoltar = {
                    navController.popBackStack()
                }
            )
        }
    }
}
