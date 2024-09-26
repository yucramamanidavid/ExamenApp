package pe.edu.upeu.juego3enraya.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import pe.edu.upeu.juego3enraya.model.GameResult
import pe.edu.upeu.juego3enraya.model.GameResultDao

class GameResultRepository (private val wordDao: GameResultDao){
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allWords: Flow<List<GameResult>> = wordDao.getAllGameResults()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: GameResult) {
        wordDao.insertGameResult(word)
    }
}