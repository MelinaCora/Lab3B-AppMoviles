package com.catedra.peliculas.ui.auth

sealed class AuthState{
    object NoAutenticado : AuthState()
    object Autenticado : AuthState()
    data class Error(val mensaje: String) : AuthState()

}