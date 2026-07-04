package com.catedra.peliculas.ui.AuthState

sealed class AuthState{
    object NoAutenicado : AuthState()
    object Autenticado : AuthState()
    data class Error(val mensaje: String) : AuthState()

}