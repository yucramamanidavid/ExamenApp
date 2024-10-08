package pe.edu.upeu.juego3enraya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
<<<<<<< HEAD
import androidx.compose.ui.unit.dp
=======
>>>>>>> d90496682a9df03094df824d056fd5eadf3db2d8
import androidx.lifecycle.viewmodel.compose.viewModel
import pe.edu.upeu.juego3enraya.model.AppDatabase
import pe.edu.upeu.juego3enraya.network.RetrofitInstance
import pe.edu.upeu.juego3enraya.repository.GameResultRepository
import pe.edu.upeu.juego3enraya.ui.theme.Juego3EnRayaTheme
import pe.edu.upeu.juego3enraya.ui.viewmodel.TicTacToeViewModel
import pe.edu.upeu.juego3enraya.ui.viewmodel.TicTacToeViewModelFactory
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton


import pe.edu.upeu.juego3enraya.view.TicTacToeScreen



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener la base de datos para pasarla al ViewModel
        val db = AppDatabase.getDatabase(applicationContext)

        // Crear el repositorio que usará el ViewModel
        val repository = GameResultRepository(db.gameResultDao(), RetrofitInstance.api)

        setContent {
            var isDarkTheme by remember { mutableStateOf(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) }

            Juego3EnRayaTheme(darkTheme = isDarkTheme) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Crear una instancia del ViewModel y pasarla a TicTacToeScreen
<<<<<<< HEAD
                    val viewModel: TicTacToeViewModel = viewModel(factory = TicTacToeViewModelFactory(db))

                    // Agregar el IconButton para cambiar entre el modo día y noche
                    Column(modifier = Modifier.padding(innerPadding)) {
                        // IconButton para alternar entre modos
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = {
                                isDarkTheme = !isDarkTheme
                                AppCompatDelegate.setDefaultNightMode(
                                    if (isDarkTheme) {
                                        AppCompatDelegate.MODE_NIGHT_YES // Modo Noche
                                    } else {
                                        AppCompatDelegate.MODE_NIGHT_NO // Modo Día
                                    }
                                )
                            }) {
                                Icon(
                                    imageVector = if (isDarkTheme) Icons.Filled.Brightness4 else Icons.Filled.WbSunny,
                                    contentDescription = if (isDarkTheme) "Modo Noche" else "Modo Día",
                                    tint = if (isDarkTheme) Color.White else Color.Unspecified // Color blanco para la luna
                                )
                            }
                        }

                        TicTacToeScreen(
                            context = this@MainActivity,  // Aquí pasamos el contexto
                            modifier = Modifier.fillMaxSize(),
                            viewModel = viewModel
                        )
                    }
=======
                    val viewModel: TicTacToeViewModel = viewModel(factory = TicTacToeViewModelFactory(repository))
                    TicTacToeScreen(
                        context = this,  // Aquí pasamos el contexto
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
>>>>>>> d90496682a9df03094df824d056fd5eadf3db2d8
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
