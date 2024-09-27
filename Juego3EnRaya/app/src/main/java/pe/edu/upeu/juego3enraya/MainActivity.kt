package pe.edu.upeu.juego3enraya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import pe.edu.upeu.juego3enraya.model.AppDatabase
import pe.edu.upeu.juego3enraya.network.RetrofitInstance
import pe.edu.upeu.juego3enraya.repository.GameResultRepository
import pe.edu.upeu.juego3enraya.ui.theme.Juego3EnRayaTheme
import pe.edu.upeu.juego3enraya.ui.viewmodel.TicTacToeViewModel
import pe.edu.upeu.juego3enraya.ui.viewmodel.TicTacToeViewModelFactory

import pe.edu.upeu.juego3enraya.view.TicTacToeScreen



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener la base de datos para pasarla al ViewModel
        val db = AppDatabase.getDatabase(applicationContext)

        // Crear el repositorio que usará el ViewModel
        val repository = GameResultRepository(db.gameResultDao(), RetrofitInstance.api)

        setContent {
            Juego3EnRayaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Crear una instancia del ViewModel y pasarla a TicTacToeScreen
                    val viewModel: TicTacToeViewModel = viewModel(factory = TicTacToeViewModelFactory(repository))
                    TicTacToeScreen(
                        context = this,  // Aquí pasamos el contexto
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun TicTacToePreview() {
    Juego3EnRayaTheme {
        TicTacToeScreen(context = LocalContext.current)
    }
}
