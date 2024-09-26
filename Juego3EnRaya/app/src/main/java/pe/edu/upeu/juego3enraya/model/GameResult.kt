package pe.edu.upeu.juego3enraya.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "game_results")
data class GameResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombrePartida: String,
    val nombreJugador1: String,
    val nombreJugador2: String,
    val ganador: String,
    val puntos: Int,
    val estado: String
)
