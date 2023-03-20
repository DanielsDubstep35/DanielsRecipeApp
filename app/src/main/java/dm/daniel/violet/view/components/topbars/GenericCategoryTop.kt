package dm.daniel.violet.view.components.topbars

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import dm.daniel.violet.viewModel.CategoryUiState

@Composable
fun GenericCategoryTop(
    uiState: CategoryUiState,
) {
    when (uiState) {
        is CategoryUiState.Loading -> {
            TopAppBar(
                title = { Text(text = "Loading...") },
            )
        }
        is CategoryUiState.Success -> {
            TopAppBar(
                title = {
                    Text(
                        text = "Categories",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.secondary,
                    ) },
                backgroundColor = MaterialTheme.colors.primary,
            )
        }
        is CategoryUiState.Error -> {
            TopAppBar(
                title = { Text(text = "Error") },
            )
        }
    }
}