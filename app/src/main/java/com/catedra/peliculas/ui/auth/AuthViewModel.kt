package com.catedra.peliculas.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _authState = MutableStateFlow<AuthState>(
        if (auth.currentUser != null)
            AuthState.Autenticado
        else
            AuthState.NoAutenticado
    )

    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun iniciarSesion(email: String, password: String) {

        viewModelScope.launch {

            try {

                auth.signInWithEmailAndPassword(email, password).await()

                _authState.value = AuthState.Autenticado

            } catch (e: Exception) {

                _authState.value =
                    AuthState.Error(
                        e.message ?: "Error al iniciar sesión"
                    )

            }

        }

    }

    fun registrar(email: String, password: String) {

        viewModelScope.launch {

            try {

                auth.createUserWithEmailAndPassword(email, password).await()

                val uid = auth.currentUser!!.uid

                val usuario = hashMapOf(
                    "email" to email
                )

                db.collection("usuarios")
                    .document(uid)
                    .set(usuario)
                    .await()

                _authState.value = AuthState.Autenticado

            } catch (e: Exception) {

                _authState.value =
                    AuthState.Error(
                        e.message ?: "Error al crear la cuenta"
                    )

            }

        }

    }

    fun cerrarSesion() {

        auth.signOut()

        _authState.value = AuthState.NoAutenticado

    }
}