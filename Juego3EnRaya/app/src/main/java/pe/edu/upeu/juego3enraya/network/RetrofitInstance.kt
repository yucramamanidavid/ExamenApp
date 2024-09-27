package pe.edu.upeu.juego3enraya.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: GameResultApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")  // Asegúrate de que la URL sea correcta
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GameResultApi::class.java)
    }
}


