package pe.edu.upeu.juego3enraya.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameResultDao {
    @Insert
     fun insertGameResult(gameResult: GameResult)

    @Query("SELECT * FROM game_results") // Asegúrate de que el nombre de la tabla sea correcto
    fun getAllGameResults(): Flow<List<GameResult>>
}