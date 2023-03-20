package dm.daniel.violet.view.components.topbars

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import dm.daniel.violet.viewModel.CatRecipeUiState

@Composable
fun GenericCatRecipeTop(
    uiState: CatRecipeUiState,
    navController: NavController,
) {
    when (uiState) {
        is CatRecipeUiState.Loading -> {
            TopAppBar(
                title = { Text(text = "Loading...") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
        is CatRecipeUiState.Success -> {
            TopAppBar(
                title = { Text(
                    text = "${uiState.recipe.get(0).strMeal}",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.secondary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
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
        is CatRecipeUiState.Error -> {
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