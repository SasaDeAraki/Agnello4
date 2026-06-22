package br.com.fiap.agnellofrontend.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.fiap.agnellofrontend.data.model.Produto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdutoScreen(
    uiState: ProdutoUiState,
    onSync: () -> Unit,
    onSave: () -> Unit,
    onDelete: (Produto) -> Unit,
    onNomeChange: (String) -> Unit,
    onTipoChange: (String) -> Unit,
    onRegiaoChange: (String) -> Unit,
    onUvaChange: (String) -> Unit,
    onValorChange: (String) -> Unit,
    onEstoqueChange: (String) -> Unit,
    onAvaliacaoChange: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Agnello Estoque") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Estoque local com Room",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Sincronize com a API em C# e mantenha um cache offline.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (uiState.isSyncing) {
                    CircularProgressIndicator(modifier = Modifier.padding(start = 12.dp))
                } else {
                    OutlinedButton(onClick = onSync) {
                        Text("Sincronizar")
                    }
                }
            }

            uiState.feedback?.let { message ->
                Surface(
                    tonalElevation = 1.dp,
                    shape = MaterialTheme.shapes.small,
                    color = if (message.isError) {
                        MaterialTheme.colorScheme.errorContainer
                    } else {
                        MaterialTheme.colorScheme.secondaryContainer
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = message.text,
                        modifier = Modifier.padding(12.dp),
                        color = if (message.isError) {
                            MaterialTheme.colorScheme.onErrorContainer
                        } else {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        }
                    )
                }
            }

            FormSection(
                form = uiState.form,
                isSaving = uiState.isSaving,
                onNomeChange = onNomeChange,
                onTipoChange = onTipoChange,
                onRegiaoChange = onRegiaoChange,
                onUvaChange = onUvaChange,
                onValorChange = onValorChange,
                onEstoqueChange = onEstoqueChange,
                onAvaliacaoChange = onAvaliacaoChange,
                onSave = onSave
            )

            HorizontalDivider()

            Text(
                text = "Produtos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            if (uiState.produtos.isEmpty()) {
                Text(
                    text = "Nenhum produto carregado ainda. Toque em sincronizar para buscar da API.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    uiState.produtos.forEach { produto ->
                        ProdutoCard(produto = produto, onDelete = onDelete)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun FormSection(
    form: ProdutoFormState,
    isSaving: Boolean,
    onNomeChange: (String) -> Unit,
    onTipoChange: (String) -> Unit,
    onRegiaoChange: (String) -> Unit,
    onUvaChange: (String) -> Unit,
    onValorChange: (String) -> Unit,
    onEstoqueChange: (String) -> Unit,
    onAvaliacaoChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Novo produto",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = form.nome,
                onValueChange = onNomeChange,
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.tipo,
                onValueChange = onTipoChange,
                label = { Text("Tipo") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.regiao,
                onValueChange = onRegiaoChange,
                label = { Text("Regiao") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.uva,
                onValueChange = onUvaChange,
                label = { Text("Uva") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.valor,
                onValueChange = onValorChange,
                label = { Text("Valor") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.estoque,
                onValueChange = onEstoqueChange,
                label = { Text("Estoque") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.avaliacaoCritica,
                onValueChange = onAvaliacaoChange,
                label = { Text("Avaliacao") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = onSave,
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isSaving) "Salvando..." else "Salvar")
        }
    }
}

@Composable
private fun ProdutoCard(
    produto: Produto,
    onDelete: (Produto) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = produto.nome,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = listOfNotNull(produto.tipo, produto.regiao).joinToString(" - "),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                OutlinedButton(onClick = { onDelete(produto) }) {
                    Text("Excluir")
                }
            }

            Text(text = "Uva: ${produto.uva ?: "-"}")
            Text(text = "Valor: R$ ${produto.valor}")
            Text(text = "Estoque: ${produto.quantidadeEstoque}")
            Text(text = "Avaliacao: ${produto.avaliacaoCritica ?: "-"}")
        }
    }
}
