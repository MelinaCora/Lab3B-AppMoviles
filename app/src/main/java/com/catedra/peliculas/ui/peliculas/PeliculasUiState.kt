package com.catedra.peliculas.ui.peliculas

import com.catedra.peliculas.data.model.Pelicula

data class PeliculasUiState(

    val peliculas: List<Pelicula> = emptyList(),

    val consulta: String = "",

    val cargando: Boolean = true,

    val error: String? = null

) {

    val peliculasFiltradas: List<Pelicula>
        get() =
            if (consulta.isBlank()) {
                peliculas
            } else {
                peliculas.filter {
                    it.titulo.contains(
                        consulta,
                        ignoreCase = true
                    )
                }
            }

}