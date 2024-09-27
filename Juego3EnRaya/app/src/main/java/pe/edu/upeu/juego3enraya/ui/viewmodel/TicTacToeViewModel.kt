package pe.edu.upeu.juego3enraya.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pe.edu.upeu.juego3enraya.model.AppDatabase
import pe.edu.upeu.juego3enraya.model.GameResult
import androidx.compose.ui.graphics.Color
import pe.edu.upeu.juego3enraya.repository.GameResultRepository

class TicTacToeViewModel(private val repository: GameResultRepository) : ViewModel() {

    var isGameActive = mutableStateOf(false)
    var nombreJugador1 = mutableStateOf("")
    var nombreJugador2 = mutableStateOf("")
    var shouldResetBoard = mutableStateOf(false)
    var gameResults = mutableStateListOf<GameResult>()
    var board = mutableStateListOf("", "", "", "", "", "", "", "", "")
    var currentPlayer = mutableStateOf("X")
    var dialogMessage = mutableStateOf("")
    var showDialog = mutableStateOf(false)

    // Lista de colores para las celdas del tablero
    var cellColors = mutableStateListOf<Color>().apply {
        repeat(9) { add(Color.Gray) } // Inicializa los colores en gris
    }

    private var partidaContador = 1  // Contador de partidas

    init {
        // Cargar resultados desde la base de datos
        viewModelScope.launch(Dispatchers.IO) {
            repository.getGameResults().collect { results ->
                withContext(Dispatchers.Main) {
                    gameResults.clear()
                    gameResults.addAll(results)  // results ya es un List<GameResult>
                }
            }
        }
    }

    // Reiniciar el juego
    fun resetGame() {
        shouldResetBoard.value = true
        isGameActive.value = true
        board.clear()
        board.addAll(List(9) { "" })
        currentPlayer.value = "X"

        // Reiniciar la lista de colores a gris
        cellColors.clear()
        cellColors.addAll(List(9) { Color.Gray })
    }

    // Realizar un movimiento en el tablero
    fun makeMove(index: Int) {
        if (board[index].isEmpty()) {
            board[index] = currentPlayer.value
            cellColors[index] = if (currentPlayer.value == "X") Color.Red else Color.Blue
            currentPlayer.value = if (currentPlayer.value == "X") "O" else "X"
            checkForWinner()
        }
    }

    // Verificar si hay un ganador
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

    // Manejar el final del juego
    private fun onGameEnd(winner: String) {
        dialogMessage.value = if (winner == "Empate") "¡Es un empate!" else "Ganador: $winner"
        showDialog.value = true
        isGameActive.value = false

        val gameResult = GameResult(
            nombrePartida = "Partida $partidaContador",  // Usa el contador para el nombre de la partida
            nombreJugador1 = nombreJugador1.value,
            nombreJugador2 = nombreJugador2.value,
            ganador = winner,
            puntos = if (winner == "Empate") 5 else 10,
            estado = "Finalizado"
        )

        // Incrementar el contador y guardar el resultado localmente y en el servidor
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertGameResult(gameResult)

            partidaContador++

            // Actualizar la lista de resultados después de guardar el resultado
            repository.getGameResults().collect { results ->
                withContext(Dispatchers.Main) {
                    gameResults.clear()
                    gameResults.addAll(results)  // Asegúrate que `results` es List<GameResult>
                }
            }
        }
    }
}