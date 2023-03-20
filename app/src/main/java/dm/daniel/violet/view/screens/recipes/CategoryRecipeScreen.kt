package dm.daniel.violet.view.screens.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dm.daniel.violet.view.components.bottombars.coilImage
import dm.daniel.violet.view.components.topbars.GenericCatRecipeTop
import dm.daniel.violet.viewModel.CatRecipeUiState
import dm.daniel.violet.viewModel.CategoryRecipeViewmodel

@Composable
fun CategoryRecipeScreen(
    categoryRecipeViewmodel: CategoryRecipeViewmodel,
    navController: NavController,
    uiState: CatRecipeUiState
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            GenericCatRecipeTop(uiState = uiState, navController = navController)
        }
    ) { padding ->

        when (uiState) {
            is CatRecipeUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center)
                )
            }
            is CatRecipeUiState.Success -> {
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

                            coilImage(url = uiState.recipe.get(0).strMealThumb,
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
                                        Text(text = uiState.recipe.get(0).strMeal, color = MaterialTheme.colors.onPrimary)
                                        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Top) {
                                            IconButton(onClick = { categoryRecipeViewmodel.AddToMyRecipes(recipe = uiState.recipe.get(0) ) }) {
                                                Icon(Icons.Filled.Star, contentDescription = "Star", tint = MaterialTheme.colors.secondary)
                                            }
                                        }
                                    }

                                    Text(
                                        text = uiState.recipe.get(0).strInstructions, color = MaterialTheme.colors.secondary,
                                        modifier = Modifier.verticalScroll(rememberScrollState())
                                    )
                                }
                            }

                        }
                    }

                }
            }
            is CatRecipeUiState.Error -> {
                Text(text = "Error")
            }
        }

    }

}