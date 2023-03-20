package dm.daniel.violet.view.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dm.daniel.violet.model.models.UserRecipes
import dm.daniel.violet.model.repository.FirebaseResources
import dm.daniel.violet.view.components.bottombars.HistoryBottomBar
import dm.daniel.violet.view.components.bottombars.coilImage
import dm.daniel.violet.viewModel.HistoryUiState
import dm.daniel.violet.viewModel.HistoryViewmodel

@Composable
fun HistoryScreen(
    historyViewmodel: HistoryViewmodel,
    onRecipeClick: (recipeId: String) -> Unit,
    navController: NavController,
    navToCategoryScreen: () -> Unit,
    navToHomeScreen: () -> Unit,
) {
    val homeUiState = historyViewmodel?.historyUiState ?: HistoryUiState()

    var editRecipeDialog by remember {
        mutableStateOf(false)
    }

    var selectedRecipe: UserRecipes? by remember {
        mutableStateOf(null)
    }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = Unit){
        historyViewmodel?.loadFavoritedRecipes()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Favorites",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.secondary,
                        style = MaterialTheme.typography.body1,
                        fontSize = 20.sp,
                    )
                },
                backgroundColor = MaterialTheme.colors.primary,
            )
        },
        bottomBar = {
            HistoryBottomBar(navToHomeScreen = navToHomeScreen, navToCategoryScreen = navToCategoryScreen)
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
    ) { padding ->
        Column( modifier = Modifier.padding(padding) ) {
            when (homeUiState.userRecipes) {
                is FirebaseResources.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(align = Alignment.Center)
                    )
                }
                is FirebaseResources.Success -> {
                    Text(text = "Your Favorite Recipes", color = MaterialTheme.colors.primary, fontSize = 20.sp, modifier = Modifier.fillMaxWidth().padding(8.dp), textAlign = TextAlign.Center)
                    Text(text = "Hold to Remove a Recipe from this list", color = MaterialTheme.colors.primary, fontSize = 16.sp, modifier = Modifier.fillMaxWidth().padding(8.dp), textAlign = TextAlign.Center)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(4.dp),
                    ) {
                        items(
                            homeUiState.userRecipes.data ?: emptyList()
                        ) { recipe ->
                            RecipeItem(
                                recipes = recipe,
                                onLongClick = {
                                    editRecipeDialog = true
                                    selectedRecipe = recipe
                                },
                                onClick = {
                                    onRecipeClick.invoke(recipe.recipeId)
                                },
                            )
                        }
                    }
                    AnimatedVisibility(visible = editRecipeDialog) {
                        AlertDialog(
                            onDismissRequest = {
                                editRecipeDialog = false
                            },
                            title = { Text(
                                text = "Remove Recipe From History?",
                                color = MaterialTheme.colors.secondary,
                            ) },
                            backgroundColor = MaterialTheme.colors.primary,
                            confirmButton = {
                                Button(
                                    onClick = {
                                        selectedRecipe?.recipeId?.let {
                                            historyViewmodel?.removeRecipe(recipeId = it, favorited = false)
                                        }
                                        editRecipeDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MaterialTheme.colors.secondary
                                    ),
                                ) {
                                    Text(
                                        text = "Remove",
                                        color = MaterialTheme.colors.primary
                                    )
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = { editRecipeDialog = false },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color.Red
                                    ),
                                ) {
                                    Text(
                                        text = "Cancel",
                                        color = MaterialTheme.colors.primary
                                    )
                                }
                            }
                        )
                    }
                }
                else -> {
                    Text(
                        text = homeUiState
                            .userRecipes.throwable?.localizedMessage ?: "Unknown Error",
                        color = Color.Black
                    )
                    println(homeUiState.userRecipes.throwable?.localizedMessage)
                }
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipeItem(
    recipes: UserRecipes,
    onLongClick: () -> Unit,
    onClick: () -> Unit,
) {

    Card(
        modifier = Modifier
            .combinedClickable(
                onLongClick = { onLongClick.invoke() },
                onClick = { onClick.invoke() }
            )
            .padding(8.dp)
            .width(160.dp)
            .height(200.dp),
        elevation = 8.dp,
    ) {
        Column(
            modifier = Modifier.background(MaterialTheme.colors.primary).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth().wrapContentHeight().padding(4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text( text = recipes.recipeTitle, fontSize = 16.sp, textAlign = TextAlign.Start, modifier = Modifier.padding(4.dp), overflow = TextOverflow.Ellipsis, maxLines = 1, color = MaterialTheme.colors.secondary)
            }

            coilImage(
                url = recipes.imageUrl,
                modifier = Modifier.fillMaxSize(),
                shape = MaterialTheme.shapes.small
            )
        }
    }

}