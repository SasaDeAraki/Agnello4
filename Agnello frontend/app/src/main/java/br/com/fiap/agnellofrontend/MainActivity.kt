package br.com.fiap.agnellofrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.agnellofrontend.data.AppContainer
import br.com.fiap.agnellofrontend.ui.ProdutoScreen
import br.com.fiap.agnellofrontend.ui.ProdutoViewModel
import br.com.fiap.agnellofrontend.ui.ProdutoViewModelFactory
import br.com.fiap.agnellofrontend.ui.theme.AgnelloFrontendTheme
import androidx.compose.runtime.collectAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgnelloFrontendTheme {
                AppContent()
            }
        }
    }
}

@Composable
private fun AppContent() {
    val context = LocalContext.current.applicationContext
    val repository = remember(context) {
        AppContainer.provideProdutoRepository(context)
    }
    val viewModel: ProdutoViewModel = viewModel(factory = ProdutoViewModelFactory(repository))
    val uiState by viewModel.uiState.collectAsState()

    ProdutoScreen(
        uiState = uiState,
        onSync = viewModel::sincronizar,
        onSave = viewModel::salvarProduto,
        onDelete = viewModel::excluirProduto,
        onNomeChange = viewModel::onNomeChange,
        onTipoChange = viewModel::onTipoChange,
        onRegiaoChange = viewModel::onRegiaoChange,
        onUvaChange = viewModel::onUvaChange,
        onValorChange = viewModel::onValorChange,
        onEstoqueChange = viewModel::onEstoqueChange,
        onAvaliacaoChange = viewModel::onAvaliacaoChange
    )
}
