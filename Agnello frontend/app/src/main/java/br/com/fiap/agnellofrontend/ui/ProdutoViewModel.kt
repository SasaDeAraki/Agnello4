package br.com.fiap.agnellofrontend.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.com.fiap.agnellofrontend.data.model.Produto
import br.com.fiap.agnellofrontend.data.repository.ProdutoRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProdutoViewModel(
    private val repository: ProdutoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProdutoUiState())
    val uiState: StateFlow<ProdutoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeProdutos().collect { produtos ->
                _uiState.update { current ->
                    current.copy(produtos = produtos)
                }
            }
        }
        sincronizar()
    }

    fun onNomeChange(value: String) = updateForm { it.copy(nome = value) }
    fun onTipoChange(value: String) = updateForm { it.copy(tipo = value) }
    fun onRegiaoChange(value: String) = updateForm { it.copy(regiao = value) }
    fun onUvaChange(value: String) = updateForm { it.copy(uva = value) }
    fun onValorChange(value: String) = updateForm { it.copy(valor = value) }
    fun onEstoqueChange(value: String) = updateForm { it.copy(estoque = value) }
    fun onAvaliacaoChange(value: String) = updateForm { it.copy(avaliacaoCritica = value) }

    fun sincronizar() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true, feedback = null) }
            runCatching { repository.sincronizar() }
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isSyncing = false,
                            feedback = FeedbackMessage("Lista sincronizada com o backend.")
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isSyncing = false,
                            feedback = FeedbackMessage(
                                text = error.message ?: "Falha ao sincronizar com o backend.",
                                isError = true
                            )
                        )
                    }
                }
        }
    }

    fun salvarProduto() {
        val form = _uiState.value.form
        val nome = form.nome.trim()
        val valor = form.valor.parseValor()
        val estoque = form.estoque.toIntOrNull()
        val avaliacao = form.avaliacaoCritica.parseValor()

        if (nome.isBlank() || valor == null || estoque == null) {
            _uiState.update {
                it.copy(
                    feedback = FeedbackMessage(
                        text = "Preencha nome, valor e estoque para salvar.",
                        isError = true
                    )
                )
            }
            return
        }

        val produto = Produto(
            nome = nome,
            tipo = form.tipo.takeIf { it.isNotBlank() },
            regiao = form.regiao.takeIf { it.isNotBlank() },
            uva = form.uva.takeIf { it.isNotBlank() },
            valor = valor,
            quantidadeEstoque = estoque,
            avaliacaoCritica = avaliacao
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, feedback = null) }
            runCatching { repository.salvar(produto) }
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            form = ProdutoFormState(),
                            feedback = FeedbackMessage("Produto salvo no backend e no Room.")
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            feedback = FeedbackMessage(
                                text = error.message ?: "Falha ao salvar produto.",
                                isError = true
                            )
                        )
                    }
                }
        }
    }

    fun excluirProduto(produto: Produto) {
        viewModelScope.launch {
            runCatching { repository.excluir(produto) }
                .onSuccess {
                    _uiState.update {
                        it.copy(feedback = FeedbackMessage("Produto removido."))
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            feedback = FeedbackMessage(
                                text = error.message ?: "Falha ao remover produto.",
                                isError = true
                            )
                        )
                    }
                }
        }
    }

    private fun updateForm(block: (ProdutoFormState) -> ProdutoFormState) {
        _uiState.update { current ->
            current.copy(form = block(current.form), feedback = null)
        }
    }

    private fun String.parseValor(): Double? {
        val normalized = trim().replace(",", ".")
        return normalized.toDoubleOrNull()
    }
}

class ProdutoViewModelFactory(
    private val repository: ProdutoRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProdutoViewModel(repository) as T
    }
}
