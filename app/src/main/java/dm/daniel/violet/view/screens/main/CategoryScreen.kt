package dm.daniel.violet.view.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dm.daniel.violet.model.models.UserRecipes
import dm.daniel.violet.view.components.bottombars.CategoryBottomBar
import dm.daniel.violet.view.components.topbars.GenericCategoryTop
import dm.daniel.violet.viewModel.CategoryUiState
import dm.daniel.violet.viewModel.CategoryViewmodel

@Composable
fun CategoryScreen(
    onCategoryClick: (SelectedCategory: String) -> Unit,
    navToHomeScreen: () -> Unit,
    navToHistoryScreen: () -> Unit,
    categoryViewModel: CategoryViewmodel,
    uiState: CategoryUiState
) {
    when (uiState) {
        is CategoryUiState.Loading -> {
            CircularProgressIndicator()
            println(uiState)
        }
        is CategoryUiState.Success -> {
            var selectedRecipe: UserRecipes? by remember {
                mutableStateOf(null)
            }

            val scope = rememberCoroutineScope()
            val scaffoldState = rememberScaffoldState()

            /*
            LaunchedEffect(key1 = Unit){
                categoryViewModel?.categories()
            }
            */

            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    GenericCategoryTop(uiState = uiState)
                },
                bottomBar = {
                    CategoryBottomBar(navToHomeScreen = navToHomeScreen, navToHistoryScreen = navToHistoryScreen)
                },
                floatingActionButtonPosition = FabPosition.Center,
                isFloatingActionButtonDocked = true,
            ) { padding ->
                Column {
                    Text(text = "Pick from a wide variety of categories!", color = MaterialTheme.colors.primary, fontSize = 20.sp, modifier = Modifier.fillMaxWidth().padding(8.dp), textAlign = TextAlign.Center)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                    ) {
                        items(uiState.categories) { category ->
                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxHeight()
                                    .width(150.dp)
                                    .clickable(onClick = {
                                        onCategoryClick.invoke(category.strCategory)
                                    }),
                                backgroundColor = MaterialTheme.colors.primary,
                                elevation = 8.dp
                            ) {
                                Text(
                                    text = category.strCategory,
                                    modifier = Modifier.padding(8.dp),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colors.secondary
                                )
                            }
                        }
                    }
                }
            }
        }
        is CategoryUiState.Error -> {
            Text(text = "Error")
        }
    }

}



