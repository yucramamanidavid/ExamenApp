package pe.edu.upeu.juego3enraya.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pe.edu.upeu.juego3enraya.repository.GameResultRepository
import pe.edu.upeu.juego3enraya.ui.viewmodel.TicTacToeViewModel

class TicTacToeViewModelFactory(private val repository: GameResultRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TicTacToeViewModel::class.java)) {
            return TicTacToeViewModel(repository) as T  // Cambia aquí para pasar el repositorio
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
