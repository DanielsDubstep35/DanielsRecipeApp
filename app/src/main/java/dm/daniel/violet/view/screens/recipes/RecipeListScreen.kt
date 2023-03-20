package dm.daniel.violet.view.screens.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dm.daniel.violet.model.api.Recipe
import dm.daniel.violet.view.components.bottombars.coilImage
import dm.daniel.violet.view.components.topbars.GenericListTop
import dm.daniel.violet.viewModel.ListUiState
import dm.daniel.violet.viewModel.ListViewmodel

@Composable
fun RecipeListScreen(
    navToRecipeScreen: (id: String) -> Unit,
    uiState: ListUiState,
    navController: NavController,
    listViewmodel: ListViewmodel,
    category: String
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            GenericListTop(uiState = uiState, navController = navController)
        }
    ) { padding ->
        when (uiState) {
            is ListUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center)
                )
            }
            is ListUiState.Success -> { RecipeList(recipes = uiState.recipes, navToRecipeScreen = navToRecipeScreen) }
            is ListUiState.Error -> {
                Text(text = "Error: $category")
            }
        }
    }

}

@Composable
fun RecipeList(
    recipes: List<Recipe>,
    navToRecipeScreen: (id: String) -> Unit
) {
   LazyVerticalGrid(columns = GridCells.Fixed(2)) {
       items(recipes) { recipe ->
              Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
              ) {
                  Column(
                      modifier = Modifier.background(MaterialTheme.colors.primary).fillMaxSize(),
                      verticalArrangement = Arrangement.SpaceBetween
                  ) {
                      Text(
                          text = recipe.strMeal,
                          modifier = Modifier
                              .fillMaxSize().padding(4.dp),
                          color = MaterialTheme.colors.secondary,
                          textAlign = TextAlign.Center,
                          maxLines = 1,
                          overflow = TextOverflow.Ellipsis,
                          fontSize = 16.sp
                      )
                      Box(
                          modifier = Modifier
                              .wrapContentSize()
                              .clickable(onClick = { navToRecipeScreen(recipe.idMeal) })
                      ) {
                          coilImage(
                              url = recipe.strMealThumb,
                              modifier = Modifier
                                  .fillMaxWidth()
                                  .height(200.dp),
                              shape = MaterialTheme.shapes.small.copy(CornerSize(0.dp)
                              )
                          )
                      }
                  }
              }
       }
   }
}