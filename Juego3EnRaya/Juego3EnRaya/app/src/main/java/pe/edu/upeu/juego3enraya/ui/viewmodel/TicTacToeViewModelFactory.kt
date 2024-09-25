package pe.edu.upeu.juego3enraya.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pe.edu.upeu.juego3enraya.model.AppDatabase

class TicTacToeViewModelFactory(private val db: AppDatabase) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TicTacToeViewModel::class.java)) {
            return TicTacToeViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
