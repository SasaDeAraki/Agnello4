package br.com.fiap.agnellofrontend.data

import android.content.Context
import androidx.room.Room
import br.com.fiap.agnellofrontend.data.local.AppDatabase
import br.com.fiap.agnellofrontend.data.remote.ProdutoApiService
import br.com.fiap.agnellofrontend.data.repository.ProdutoRepository
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppContainer {
    private const val BASE_URL = "http://10.0.2.2:5000/"

    fun provideProdutoRepository(context: Context): ProdutoRepository {
        val database = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "agnello-estoque.db"
        ).build()

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()

        return ProdutoRepository(
            dao = database.produtoDao(),
            api = retrofit.create(ProdutoApiService::class.java)
        )
    }
}
