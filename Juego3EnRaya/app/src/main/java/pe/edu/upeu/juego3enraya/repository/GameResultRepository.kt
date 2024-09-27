package pe.edu.upeu.juego3enraya.repository

import kotlinx.coroutines.flow.Flow
import pe.edu.upeu.juego3enraya.model.GameResult
import pe.edu.upeu.juego3enraya.model.GameResultDao
import pe.edu.upeu.juego3enraya.network.GameResultApi

class GameResultRepository(private val dao: GameResultDao, private val api: GameResultApi) {

    // Obtener los resultados del juego desde Room
    fun getGameResults(): Flow<List<GameResult>> = dao.getAllGameResults()

    // Insertar el resultado del juego en Room y enviarlo al backend
    suspend fun insertGameResult(gameResult: GameResult) {
        dao.insertGameResult(gameResult)  // Almacena localmente
        api.saveGameResult(gameResult)    // Envía al servidor backend
    }
}
