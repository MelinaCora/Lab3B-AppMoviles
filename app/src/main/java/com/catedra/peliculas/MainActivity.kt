package com.catedra.peliculas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.catedra.peliculas.ui.auth.AuthScreen
import com.catedra.peliculas.ui.auth.AuthState
import com.catedra.peliculas.ui.auth.AuthViewModel
import com.catedra.peliculas.ui.navigation.AppNavigation
import com.catedra.peliculas.ui.theme.Lab2BTheme

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            Lab2BTheme {

                val authState by authViewModel.authState.collectAsStateWithLifecycle()

                when (authState) {

                    is AuthState.NoAutenticado,
                    is AuthState.Error -> {

                        AuthScreen(
                            authState = authState,
                            onIniciarSesion = { email, password ->
                                authViewModel.iniciarSesion(email, password)
                            },
                            onRegistrar = { email, password ->
                                authViewModel.registrar(email, password)
                            }
                        )

                    }

                    is AuthState.Autenticado -> {

                        AppNavigation()

                    }

                }

            }

        }
    }
}