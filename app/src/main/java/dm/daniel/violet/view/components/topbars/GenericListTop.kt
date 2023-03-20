package dm.daniel.violet.view.components.topbars

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import dm.daniel.violet.viewModel.ListUiState

@Composable
fun GenericListTop(
    uiState: ListUiState,
    navController: NavController,
) {
    when (uiState) {
        is ListUiState.Loading -> {
            TopAppBar(
                title = { Text(text = "Loading...") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
        is ListUiState.Success -> {
            TopAppBar(
                title = {
                    Text(
                        text = "Choose Your Recipe!",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.secondary,
                    ) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colors.secondary
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
            )
        }
        is ListUiState.Error -> {
            TopAppBar(
                title = { Text(text = "Error") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    }
}