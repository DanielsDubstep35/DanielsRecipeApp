package dm.daniel.violet.view.screens.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dm.daniel.violet.view.components.bottombars.coilImage
import dm.daniel.violet.view.components.topbars.GenericRecipeTop
import dm.daniel.violet.viewModel.UserRecipeUiState
import dm.daniel.violet.viewModel.UserRecipeViewmodel

@Composable
fun UserRecipeScreen(
    userRecipeViewmodel: UserRecipeViewmodel,
    recipeId: String,
    navController: NavHostController
) {
    val userRecipeUiState = userRecipeViewmodel?.recipeUiState ?: UserRecipeUiState()

    val scaffoldState = rememberScaffoldState()

    val isRecipeIdNotBlank = recipeId.isNotBlank()

    LaunchedEffect(key1 = Unit) {
        if (isRecipeIdNotBlank) {
            userRecipeViewmodel.getRecipe(recipeId = recipeId)
        } else {
            userRecipeViewmodel.resetState()
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            GenericRecipeTop(uiState = userRecipeUiState, navController = navController)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
                .padding(padding),
            verticalArrangement = Arrangement.Top
        ) {

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colors.background)
                ) {

                    coilImage(url = userRecipeUiState.imageUrl,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(8.dp),
                        shape = MaterialTheme.shapes.small)

                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        shape = MaterialTheme.shapes.small,
                        backgroundColor = MaterialTheme.colors.primary
                    ) {
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(text = userRecipeUiState.Title, color = MaterialTheme.colors.onPrimary)
                                Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Top) {
                                    IconButton(onClick = { userRecipeViewmodel.favoriteRecipe(recipeId) }) {
                                        Icon(Icons.Filled.Favorite, contentDescription = "Favorite", tint = Color.Red)
                                    }
                                }
                            }

                            Text(
                                text = userRecipeUiState.instruction, color = MaterialTheme.colors.secondary,
                                modifier = Modifier.verticalScroll(rememberScrollState())
                            )
                        }
                    }

                }
            }

        }

    }
}