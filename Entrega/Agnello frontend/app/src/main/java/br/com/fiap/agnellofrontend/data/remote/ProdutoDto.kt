package br.com.fiap.agnellofrontend.data.remote

import br.com.fiap.agnellofrontend.data.model.Produto

data class ProdutoDto(
    val id: Int = 0,
    val nome: String,
    val tipo: String? = null,
    val regiao: String? = null,
    val uva: String? = null,
    val teorAlcoolico: Double? = null,
    val intensidade: Int? = null,
    val encorpado: Int? = null,
    val acucar: Int? = null,
    val acidez: Int? = null,
    val valor: Double,
    val quantidadeEstoque: Int,
    val avaliacaoCritica: Double? = null
)

fun ProdutoDto.toDomain(): Produto = Produto(
    id = id,
    nome = nome,
    tipo = tipo,
    regiao = regiao,
    uva = uva,
    teorAlcoolico = teorAlcoolico,
    intensidade = intensidade,
    encorpado = encorpado,
    acucar = acucar,
    acidez = acidez,
    valor = valor,
    quantidadeEstoque = quantidadeEstoque,
    avaliacaoCritica = avaliacaoCritica
)

fun Produto.toDto(): ProdutoDto = ProdutoDto(
    id = id,
    nome = nome,
    tipo = tipo,
    regiao = regiao,
    uva = uva,
    teorAlcoolico = teorAlcoolico,
    intensidade = intensidade,
    encorpado = encorpado,
    acucar = acucar,
    acidez = acidez,
    valor = valor,
    quantidadeEstoque = quantidadeEstoque,
    avaliacaoCritica = avaliacaoCritica
)
