package com.catedra.peliculas.ui.peliculas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catedra.peliculas.data.repository.PeliculasRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class PeliculasViewModel(
    private val repositorio: PeliculasRepositorio = PeliculasRepositorio()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PeliculasUiState())
    val uiState: StateFlow<PeliculasUiState> = _uiState.asStateFlow()

    init {
        cargarPeliculas()
    }

    /**
     * Carga la lista de películas desde el repositorio.
     *
     * TODO Etapa 3a: implementar esta función.
     * Debe:
     * 1. Actualizar el estado: cargando = true, error = null
     * 2. Llamar a repositorio.obtenerPeliculas() dentro de una coroutine
     *    (usar viewModelScope.launch { })
     * 3. Si exitoso: actualizar peliculas y marcar cargando = false
     * 4. Si falla: guardar el mensaje de error y marcar cargando = false
     *
     * Para modificar el estado, usar:
     *   _uiState.update { it.copy(campo = nuevoValor) }
     *
     * Para manejar errores, rodear la llamada al repositorio con:
     *   try { ... } catch (e: Exception) { ... }
     */

    fun cargarPeliculas() {

        _uiState.update {
            it.copy(
                cargando = true,
                error = null
            )
        }

        viewModelScope.launch {

            try {

                repositorio.obtenerPeliculas().collect { peliculas ->

                    _uiState.update {
                        it.copy(
                            peliculas = peliculas,
                            cargando = false
                        )
                    }

                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        cargando = false,
                        error = e.message ?: "Error al obtener películas"
                    )
                }

            }

        }

    }

    /**
     * Actualiza la consulta de búsqueda.
     *
     * TODO Etapa 3b: implementar esta función.
     * Es una sola línea: actualizar el campo consulta en el UiState.
     * peliculasFiltradas se recalcula automáticamente como propiedad calculada —
     * no necesitás hacer nada más.
     *
     * Comparación con el Lab 2A: en el modelo tradicional necesitabas filtrar
     * la lista manualmente y asignar el resultado a peliculasFiltradas.
     * Acá solo actualizás consulta y Compose se encarga del resto.
     */
    fun actualizarBusqueda(consulta: String) {

        _uiState.update {
            it.copy(consulta = consulta)
        }

    }
}
