package pe.edu.upeu.juego3enraya.ui.viewmodel

import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pe.edu.upeu.juego3enraya.model.AppDatabase
import pe.edu.upeu.juego3enraya.model.GameResult

class TicTacToeViewModel(private val db: AppDatabase) : ViewModel() {

    var isGameActive = mutableStateOf(false)
    var nombreJugador1 = mutableStateOf("")
    var nombreJugador2 = mutableStateOf("")
    var shouldResetBoard = mutableStateOf(false)
    private val _gameResults = MutableStateFlow<List<GameResult>>(emptyList()) // Cambiado a StateFlow
    val gameResults: StateFlow<List<GameResult>> = _gameResults // Expuesto como StateFlow
    var board = mutableStateListOf("", "", "", "", "", "", "", "", "")
    var currentPlayer = mutableStateOf("X")
    var dialogMessage = mutableStateOf("")
    var showDialog = mutableStateOf(false)

    // Temporizador
    var timeRemaining = mutableStateOf(10) // Tiempo en segundos
    var timer: CountDownTimer? = null

    init {
        // Cargar resultados desde la base de datos
        viewModelScope.launch(Dispatchers.IO) {
            // Recolectar el Flow emitido por la base de datos
            db.gameResultDao().getAllGameResults().collect { results ->
                // Actualizar la lista de resultados en el hilo principal
                withContext(Dispatchers.Main) {
                    _gameResults.value = results // Actualiza el StateFlow
                }
            }
        }
    }

    fun startTimer() {
        timeRemaining.value = 10 // Resetear el temporizador a 10 segundos
        timer = object : CountDownTimer(10000, 1000) { // 10 segundos
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining.value = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                // El tiempo se acabó, el jugador actual pierde
                onTimeUp()
            }
        }.start()
    }

    fun resetGame() {
        shouldResetBoard.value = true
        isGameActive.value = true
        board.clear()
        board.addAll(List(9) { "" })
        currentPlayer.value = "X"
        startTimer() // Iniciar el temporizador al reiniciar el juego

    }

    fun makeMove(index: Int) {
        if (board[index].isEmpty()) {
            board[index] = currentPlayer.value
            currentPlayer.value = if (currentPlayer.value == "X") "O" else "X"
            checkForWinner()
            timer?.cancel() // Cancelar el temporizador actual
            startTimer() // Reiniciar el temporizador para el siguiente jugador
        }
    }

    private fun checkForWinner() {
        val winningCombinations = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),  // Filas
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),  // Columnas
            listOf(0, 4, 8), listOf(2, 4, 6)   // Diagonales
        )

        for (combination in winningCombinations) {
            val (a, b, c) = combination
            if (board[a].isNotEmpty() && board[a] == board[b] && board[a] == board[c]) {
                val winner = if (board[a] == "X") nombreJugador1.value else nombreJugador2.value
                onGameEnd(winner)
                return
            }
        }

        if (board.none { it.isEmpty() }) {
            onGameEnd("Empate")
        }
    }
    private fun onTimeUp() {
        // Lógica a realizar cuando se acaba el tiempo
        val loser = if (currentPlayer.value == "X") nombreJugador1.value else nombreJugador2.value
        val winner = if (currentPlayer.value == "X") nombreJugador2.value else nombreJugador1.value // Determinar el ganador

        dialogMessage.value = "$loser ha perdido por tiempo."
        showDialog.value = true
        isGameActive.value = false

        // Guardar resultado en la base de datos
        viewModelScope.launch(Dispatchers.IO) {
            db.gameResultDao().insertGameResult(
                GameResult(
                    nombrePartida = "Partida 1",
                    nombreJugador1 = nombreJugador1.value,
                    nombreJugador2 = nombreJugador2.value,
                    ganador = winner, // Asignar el ganador correctamente
                    puntos = 10, // Asignar puntos al ganador
                    estado = "Finalizado"
                )
            )

            // Actualizar la lista de resultados
            db.gameResultDao().getAllGameResults().collect { results ->
                withContext(Dispatchers.Main) {
                    _gameResults.value = results // Actualiza el StateFlow directamente
                }
            }
        }
    }


    private fun onGameEnd(winner: String) {
        dialogMessage.value = if (winner == "Empate") "¡Es un empate!" else "Ganador: $winner"
        showDialog.value = true
        isGameActive.value = false

        viewModelScope.launch(Dispatchers.IO) {
            db.gameResultDao().insertGameResult(
                GameResult(
                    nombrePartida = "Partida ${_gameResults.value.size + 1}", // Aumenta el contador de partidas
                    nombreJugador1 = nombreJugador1.value,
                    nombreJugador2 = nombreJugador2.value,
                    ganador = winner,
                    puntos = if (winner == "Empate") 5 else 10,
                    estado = "Finalizado"
                )
            )

            // Actualiza la lista de resultados
            db.gameResultDao().getAllGameResults().collect { results ->
                withContext(Dispatchers.Main) {
                    _gameResults.value = results // Actualiza el StateFlow directamente
                }
            }
        }
    }

}
