package pe.edu.upeu.juego3enraya.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import pe.edu.upeu.juego3enraya.model.GameResult
import java.io.File

fun exportResultsToPDF(context: Context, gameResults: List<GameResult>) {
    // Verifica si el almacenamiento está disponible
    if (isExternalStorageWritable()) {
        // Cambiar la ubicación a la carpeta de Documents
        val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val fileName = "resultadOoos_tictactoe.pdf"
        val file = File(downloadsDirectory, fileName)

        try {
            val pdfWriter = PdfWriter(file)
            val pdfDocument = PdfDocument(pdfWriter)
            val document = Document(pdfDocument)

            // Agregar título
            document.add(Paragraph("Resultados del Juego de Tres en Raya").setBold().setFontSize(16f))

            // Crear la tabla con 5 columnas
            val table = Table(floatArrayOf(1f, 1f, 1f, 1f, 1f, 1f)) // Ajusta los pesos de las columnas según sea necesario
            table.addHeaderCell("Partida").setBackgroundColor(ColorConstants.GREEN)
            table.addHeaderCell("Jugador 1").setBackgroundColor(ColorConstants.GREEN)
            table.addHeaderCell("Jugador 2").setBackgroundColor(ColorConstants.GREEN)
            table.addHeaderCell("Ganador").setBackgroundColor(ColorConstants.GREEN)
            table.addHeaderCell("Puntos").setBackgroundColor(ColorConstants.GREEN)
            table.addHeaderCell("Estado").setBackgroundColor(ColorConstants.GREEN)

            // Agregar los resultados al documento
                for (result in gameResults) {
                    table.addCell("Partida ${result.id}").setBackgroundColor(ColorConstants.LIGHT_GRAY) // Ajustar para mostrar el número de partida
                    table.addCell(result.nombreJugador1).setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    table.addCell(result.nombreJugador2).setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    table.addCell(result.ganador).setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    table.addCell(result.puntos.toString()).setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    table.addCell(result.estado).setBackgroundColor(ColorConstants.LIGHT_GRAY)
                }

            // Agregar la tabla al documento
            document.add(table)

            // Cerrar el documento
            document.close()

            // Mensaje de éxito
            Toast.makeText(context, "PDF creado en: ${file.absolutePath}", Toast.LENGTH_LONG).show()
            openFile(context, file) // Llama a la función para abrir el archivo PDF

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error al crear el PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    } else {
        Toast.makeText(context, "No se puede acceder al almacenamiento externo", Toast.LENGTH_LONG).show()
    }
}


fun openFile(context: Context, file: File) {
    val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(uri, "application/pdf") // Tipo MIME para PDF
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    // Verificar si hay alguna app que pueda abrir el archivo
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "No se encontró una app para abrir este archivo", Toast.LENGTH_LONG).show()
    }
}


fun isExternalStorageWritable(): Boolean {
    return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
}
