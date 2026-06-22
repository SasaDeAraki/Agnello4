package br.com.fiap.agnellofrontend.ui

import br.com.fiap.agnellofrontend.data.model.Produto

data class ProdutoFormState(
    val nome: String = "",
    val tipo: String = "",
    val regiao: String = "",
    val uva: String = "",
    val valor: String = "",
    val estoque: String = "",
    val avaliacaoCritica: String = ""
)

data class ProdutoUiState(
    val produtos: List<Produto> = emptyList(),
    val form: ProdutoFormState = ProdutoFormState(),
    val isSyncing: Boolean = false,
    val isSaving: Boolean = false,
    val feedback: FeedbackMessage? = null
)

data class FeedbackMessage(
    val text: String,
    val isError: Boolean = false
)
