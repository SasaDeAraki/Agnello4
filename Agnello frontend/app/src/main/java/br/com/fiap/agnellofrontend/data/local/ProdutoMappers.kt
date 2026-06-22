package br.com.fiap.agnellofrontend.data.local

import br.com.fiap.agnellofrontend.data.model.Produto

fun ProdutoEntity.toDomain(): Produto = Produto(
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

fun Produto.toEntity(): ProdutoEntity = ProdutoEntity(
    id,
    nome,
    tipo,
    regiao,
    uva,
    teorAlcoolico,
    intensidade,
    encorpado,
    acucar,
    acidez,
    valor,
    quantidadeEstoque,
    avaliacaoCritica
)
