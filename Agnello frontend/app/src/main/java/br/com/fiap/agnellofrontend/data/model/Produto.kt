package br.com.fiap.agnellofrontend.data.model

data class Produto(
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
