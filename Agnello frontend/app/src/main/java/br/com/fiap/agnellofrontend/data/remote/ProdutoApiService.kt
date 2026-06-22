package br.com.fiap.agnellofrontend.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProdutoApiService {
    @GET("api/produtos")
    suspend fun listarProdutos(): List<ProdutoDto>

    @POST("api/produtos")
    suspend fun criarProduto(@Body produto: ProdutoDto): ProdutoDto

    @DELETE("api/produtos/{id}")
    suspend fun excluirProduto(@Path("id") id: Int)
}
