package pe.edu.upeu.juego3enraya.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import pe.edu.upeu.juego3enraya.model.GameResult
import pe.edu.upeu.juego3enraya.ui.exportResultsToPDF
import pe.edu.upeu.juego3enraya.ui.viewmodel.TicTacToeViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun TicTacToeScreen(context: Context, modifier: Modifier = Modifier, viewModel: TicTacToeViewModel = viewModel()) {

    val gameResults by viewModel.gameResults.collectAsState() // Obtener resultados del ViewModel

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        // Llamar al TimeSelector
        TimeSelector(viewModel)


        // Mostrar el tiempo restante
        Text(
            text = "Tiempo restante: ${viewModel.timeRemaining.value}s",
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp)
        )

        TextField(
            value = viewModel.nombreJugador1.value,
            onValueChange = { viewModel.nombreJugador1.value = it },
            label = { Text("Nombre Jugador 1") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        TextField(
            value = viewModel.nombreJugador2.value,
            onValueChange = { viewModel.nombreJugador2.value = it },
            label = { Text("Nombre Jugador 2") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        Text(
            text = if (viewModel.isGameActive.value) "Juego en Progreso" else "Presiona Iniciar para Jugar",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        TicTacToeBoard(viewModel = viewModel)

        Spacer(modifier = Modifier.height(8.dp))

        // Cambiar el botón de iniciar/reiniciar
        Button(onClick = {
            if (viewModel.gameEnded.value) {
                viewModel.resetGame() // Reiniciar el juego cuando ha terminado
            } else if (viewModel.isGameActive.value) {
                viewModel.resetGame() // Reiniciar el juego en cualquier momento mientras esté activo
            } else {
                viewModel.startTimer() // Iniciar el temporizador cuando se presione "Iniciar"
                viewModel.isGameActive.value = true // Activar el juego
            }
        }) {
            Text(text = when {
                viewModel.gameEnded.value -> "Reiniciar"
                viewModel.isGameActive.value -> "Reiniciar"
                else -> "Iniciar"
            })
        }



        Spacer(modifier = Modifier.height(10.dp))

        // Usar un Row para alinear el texto y el botón de exportar en lados opuestos
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Resultados anteriores:", fontSize = 20.sp)
            ExportResultsButton(gameResults, context) // Botón de exportar resultados
        }

        // Lista de resultados
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(gameResults) { result ->
                GameResultItem(result = result)
            }
        }

        if (viewModel.showDialog.value) {
            AlertDialog(
                onDismissRequest = { viewModel.showDialog.value = false },
                confirmButton = {
                    Button(onClick = { viewModel.showDialog.value = false }) {
                        Text("OK")
                    }
                },
                title = { Text("Resultado") },
                text = { Text(viewModel.dialogMessage.value) }
            )
        }
    }
}



@Composable
fun TicTacToeBoard(viewModel: TicTacToeViewModel) {
    Column {
        for (i in 0..2) {
            Row {
                for (j in 0..2) {
                    val index = i * 3 + j
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(8.dp)
                            .background(
                                when (viewModel.board[index]) {
                                    "X" -> Color.Red // Color para "X"
                                    "O" -> Color.Blue // Color para "O"
                                    else -> Color.Gray // Color para vacío
                                },
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable(enabled = viewModel.isGameActive.value && viewModel.board[index].isEmpty()) {
                                viewModel.makeMove(index)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = viewModel.board[index], fontSize = 36.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun GameResultItem(result: GameResult) {
    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Partida: ${result.nombrePartida}", fontSize = 18.sp)
            Text("Jugador 1: ${result.nombreJugador1}", fontSize = 16.sp)
            Text("Jugador 2: ${result.nombreJugador2}", fontSize = 16.sp)
            Text("Ganador: ${result.ganador}", fontSize = 16.sp, color = Color.Green)
            Text("Puntos: ${result.puntos}", fontSize = 16.sp)
            Text("Estado: ${result.estado}", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ExportResultsButton(gameResults: List<GameResult>, context: Context) {
    Button(onClick = {
        exportResultsToPDF(context, gameResults) // Llamar a la función para exportar a PDF
    }) {
        Text(text = "Exportar a PDF")
    }
}

@Composable
fun TimeSelector(viewModel: TicTacToeViewModel) {
    var selectedTime by remember { mutableStateOf(10f) } // Usar Float para el Slider

    Row(
        verticalAlignment = Alignment.CenterVertically, // Alinear verticalmente al centro
        modifier = Modifier.fillMaxWidth().padding(8.dp) // Asegúrate de que ocupe todo el ancho
    ) {
        // Slider para seleccionar el tiempo
        Column(
            modifier = Modifier.weight(1f) // Permitir que el Slider ocupe espacio
        ) {
            Text("Tiempo: ${selectedTime.toInt()}s", fontSize = 14.sp) // Reducir tamaño de texto
            Slider(
                value = selectedTime,
                onValueChange = { selectedTime = it },
                valueRange = 5f..15f,
                steps = 10,
                modifier = Modifier.padding(horizontal = 8.dp) // Reducir padding horizontal
            )
        }

        // Botón para confirmar la selección de tiempo
        Button(
            onClick = {
                viewModel.timeRemaining.value = selectedTime.toInt() // Ajustar el tiempo en el ViewModel
            },
            modifier = Modifier.sizeIn(minWidth = 80.dp) // Ajustar el tamaño del botón
        ) {
            Text("Confirmar")
        }
    }
}

