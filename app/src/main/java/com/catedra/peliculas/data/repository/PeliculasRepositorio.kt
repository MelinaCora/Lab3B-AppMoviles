package com.catedra.peliculas.data.repository

import com.catedra.peliculas.data.model.Pelicula
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Repositorio de películas.
 * En una app real, esta clase haría llamadas a una API o base de datos.
 * Para este lab, devuelve datos hardcodeados con un delay simulado de red.
 * No modificar este archivo.
 */
class PeliculasRepositorio {

    private val db = FirebaseFirestore.getInstance()

    fun obtenerPeliculas(): Flow<List<Pelicula>> = callbackFlow {

        val listener = db.collection("peliculas")
            .orderBy("titulo")
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val peliculas = snapshot?.documents?.mapNotNull { doc ->

                    Pelicula(
                        id = doc.id,
                        titulo = doc.getString("titulo") ?: return@mapNotNull null,
                        anio = doc.getLong("anio")?.toInt() ?: 0,
                        genero = doc.getString("genero") ?: "",
                        descripcion = doc.getString("descripcion") ?: "",
                        director = doc.getString("director") ?: "",
                        duracionMinutos = doc.getLong("duracionMinutos")?.toInt() ?: 0
                    )

                } ?: emptyList()

                trySend(peliculas)
            }

        awaitClose {
            listener.remove()
        }
    }

    suspend fun obtenerPelicula(id: String): Pelicula? {

        val doc = db.collection("peliculas")
            .document(id)
            .get()
            .await()

        if (!doc.exists()) return null

        return Pelicula(
            id = doc.id,
            titulo = doc.getString("titulo") ?: "",
            anio = doc.getLong("anio")?.toInt() ?: 0,
            genero = doc.getString("genero") ?: "",
            descripcion = doc.getString("descripcion") ?: "",
            director = doc.getString("director") ?: "",
            duracionMinutos = doc.getLong("duracionMinutos")?.toInt() ?: 0
        )
    }
}
