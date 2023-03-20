package dm.daniel.violet.view.components.topbars

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import dm.daniel.violet.viewModel.UserRecipeUiState

@Composable
fun GenericRecipeTop(
    uiState: UserRecipeUiState,
    navController: NavController,
) {
    TopAppBar(
        title = { Text(
            text = "${uiState.Title}",
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