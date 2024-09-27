const express = require('express');
const mysql = require('mysql2');
const app = express();
const PORT = 3000;

// Middleware para manejar JSON en las solicitudes
app.use(express.json());

// Configurar la conexión a la base de datos MySQL en Laragon
const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',   // El usuario predeterminado de MySQL en Laragon
    password: '',   // La contraseña predeterminada de MySQL en Laragon (deja en blanco si no tienes contraseña)
    database: 'gameresult'  // Cambia esto por el nombre de tu base de datos
});

// Conectar a la base de datos
db.connect((err) => {
    if (err) {
        console.error('Error conectando a la base de datos:', err);
        return;
    }
    console.log('Conectado a la base de datos MySQL');
});

// Ruta para obtener todos los resultados de los juegos
app.get('/game_results', (req, res) => {
    db.query('SELECT * FROM game_results', (err, results) => {
        if (err) throw err;
        res.json(results);
    });
});

// Ruta para insertar un nuevo resultado de juego
app.post('/game_results', (req, res) => {
    const { nombrePartida, nombreJugador1, nombreJugador2, ganador, puntos, estado } = req.body;
    const sql = 'INSERT INTO game_results (nombre_partida, nombre_jugador1, nombre_jugador2, ganador, puntos, estado) VALUES (?, ?, ?, ?, ?, ?)';
    db.query(sql, [nombrePartida, nombreJugador1, nombreJugador2, ganador, puntos, estado], (err, result) => {
        if (err) throw err;
        res.json({ message: 'Resultado guardado con éxito', id: result.insertId });
    });
});

// Iniciar el servidor en el puerto 3000
app.listen(PORT, () => {
    console.log(`Servidor corriendo en http://localhost:${PORT}`);
});
