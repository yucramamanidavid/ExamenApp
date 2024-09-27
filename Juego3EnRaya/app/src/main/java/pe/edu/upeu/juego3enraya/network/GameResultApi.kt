package pe.edu.upeu.juego3enraya.network

import pe.edu.upeu.juego3enraya.model.GameResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface GameResultApi {
    @GET("game_results")
    suspend fun getAllGameResults(): List<GameResult>

    @POST("game_results")
    suspend fun saveGameResult(@Body gameResult: GameResult)
}
