package br.com.fiap.agnellofrontend.data.repository

import br.com.fiap.agnellofrontend.data.local.ProdutoDao
import br.com.fiap.agnellofrontend.data.local.toDomain
import br.com.fiap.agnellofrontend.data.local.toEntity
import br.com.fiap.agnellofrontend.data.model.Produto
import br.com.fiap.agnellofrontend.data.remote.ProdutoApiService
import br.com.fiap.agnellofrontend.data.remote.toDomain
import br.com.fiap.agnellofrontend.data.remote.toDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProdutoRepository(
    private val dao: ProdutoDao,
    private val api: ProdutoApiService
) {
    fun observeProdutos(): Flow<List<Produto>> = dao.observeAll().map { lista ->
        lista.map { it.toDomain() }
    }

    suspend fun sincronizar() {
        val remotos = api.listarProdutos().map { it.toDomain() }
        withContext(Dispatchers.IO) {
            dao.clear()
            dao.upsertAll(remotos.map { it.toEntity() })
        }
    }

    suspend fun salvar(produto: Produto): Produto {
        val criado = api.criarProduto(produto.toDto()).toDomain()
        withContext(Dispatchers.IO) {
            dao.upsert(criado.toEntity())
        }
        return criado
    }

    suspend fun excluir(produto: Produto) {
        api.excluirProduto(produto.id)
        withContext(Dispatchers.IO) {
            dao.delete(produto.toEntity())
        }
    }
}
