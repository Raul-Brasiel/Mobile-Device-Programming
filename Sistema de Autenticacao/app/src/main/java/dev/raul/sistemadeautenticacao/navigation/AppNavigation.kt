package dev.raul.sistemadeautenticacao.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.raul.sistemadeautenticacao.auth.AuthViewModel
import dev.raul.sistemadeautenticacao.ui.telas.Dashboard
import dev.raul.sistemadeautenticacao.ui.telas.EsqueceuASenha
import dev.raul.sistemadeautenticacao.ui.telas.Login
import dev.raul.sistemadeautenticacao.ui.telas.Registrar

object Rotas{
    const val LOGIN = "login"
    const val REGISTRAR = "registrar"
    const val ESQUECEU_SENHA = "esqueceu_senha"
    const val DASHBOARD = "dashboard"
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = viewModel()
){
    val navController = rememberNavController()
    val estado by authViewModel.estado.collectAsState()

    val rotaInicial = remember{
        if (authViewModel.estado.value.estaLogado) Rotas.DASHBOARD else Rotas.LOGIN
    }

    LaunchedEffect(estado.estaLogado){
        val rotaAtual = navController.currentBackStackEntry?.destination?.route ?: return@LaunchedEffect

        if(estado.estaLogado && rotaAtual != Rotas.DASHBOARD){
            navController.navigate(Rotas.DASHBOARD) {
                popUpTo(0) { inclusive = true }
            }
        }
        else if(!estado.estaLogado && rotaAtual == Rotas.DASHBOARD){
            navController.navigate(Rotas.LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = rotaInicial
    ){
        composable(Rotas.LOGIN){
            Login(
                viewModel = authViewModel,
                aoIrParaRegistrar = {
                    authViewModel.limparMensagens()
                    navController.navigate(Rotas.REGISTRAR)
                },
                aoIrParaEsqueceuSenha = {
                    authViewModel.limparMensagens()
                    navController.navigate(Rotas.ESQUECEU_SENHA)
                }
            )
        }

        composable(Rotas.REGISTRAR){
            Registrar(
                viewModel = authViewModel,
                aoVoltar = {
                    authViewModel.limparMensagens()
                    navController.popBackStack()
                }
            )
        }

        composable(Rotas.ESQUECEU_SENHA){
            EsqueceuASenha(
                viewModel = authViewModel,
                aoVoltar = {
                    authViewModel.limparMensagens()
                    navController.popBackStack()
                }
            )
        }

        composable(Rotas.DASHBOARD){
            Dashboard(viewModel = authViewModel)
        }
    }
}