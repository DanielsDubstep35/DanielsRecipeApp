package dm.daniel.violet.view.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dm.daniel.violet.model.models.HomeUiState
import dm.daniel.violet.model.models.HomeViewmodel
import dm.daniel.violet.model.models.UserRecipes
import dm.daniel.violet.model.repository.FirebaseResources
import dm.daniel.violet.view.components.bottombars.HomeBottomBar
import dm.daniel.violet.view.components.bottombars.coilImage

@Composable
fun HomeScreen(
    homeViewModel: HomeViewmodel,
    onRecipeClick: (recipeId: String) -> Unit,
    navToRecipePage: () -> Unit,
    navToRecipeEditPage: (id: String) -> Unit,
    navToCategoryScreen: () -> Unit,
    navToHistoryScreen: () -> Unit,
    navToLoginPage: () -> Unit
) {
    val homeUiState = homeViewModel?.homeUiState ?: HomeUiState()

    var editRecipeDialog by remember {
        mutableStateOf(false)
    }

    var selectedRecipe: UserRecipes? by remember {
        mutableStateOf(null)
    }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = Unit){
        homeViewModel?.loadRecipes()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .width(65.dp)
                    ) {

                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .width(65.dp)
                    ) {
                        IconButton(onClick = {
                            homeViewModel?.signOut()
                            navToLoginPage.invoke()
                        }) {
                            Column {
                                Icon(
                                    imageVector = Icons.Default.ExitToApp,
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.secondary,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                )

                                Text(
                                    text = "Sign Out",
                                    color = MaterialTheme.colors.secondary,
                                    textAlign = TextAlign.Center,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                },
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Daniels Recipe App",
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
            HomeBottomBar(navToCategoryScreen = navToCategoryScreen, navToHistoryScreen = navToHistoryScreen)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navToRecipePage.invoke()
                },
                backgroundColor = MaterialTheme.colors.primary,
                shape = MaterialTheme.shapes.large,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary
                )
            }
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
                    Text(text = "Your Recipes", color = MaterialTheme.colors.primary, fontSize = 20.sp, modifier = Modifier.fillMaxWidth().padding(8.dp), textAlign = TextAlign.Center)
                    Text(text = "Hold to delete a Recipe", color = MaterialTheme.colors.primary, fontSize = 16.sp, modifier = Modifier.fillMaxWidth().padding(8.dp), textAlign = TextAlign.Center)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(4.dp),
                    ) {
                        items(
                            homeUiState.userRecipes.data ?: emptyList()
                        ) { recipe ->
                            HomeRecipeItem(
                                recipes = recipe,
                                onRecipeEditClick = {
                                    navToRecipeEditPage.invoke(recipe.recipeId)
                                },
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
                                text = "Delete this Recipe?",
                                color = MaterialTheme.colors.secondary,
                            ) },
                            backgroundColor = MaterialTheme.colors.primary,
                            confirmButton = {
                                Button(
                                    onClick = {
                                        selectedRecipe?.recipeId?.let {
                                            homeViewModel?.deleteRecipe(it)
                                        }
                                        editRecipeDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MaterialTheme.colors.secondary
                                    ),
                                ) {
                                    Text(
                                        text = "Delete",
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
    LaunchedEffect(key1 = homeViewModel?.hasUser) {
        if (homeViewModel?.hasUser == false) {
            navToLoginPage.invoke()
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeRecipeItem(
    recipes: UserRecipes,
    onRecipeEditClick: () -> Unit,
    onLongClick: () -> Unit,
    onClick: () -> Unit,
) {

    Card(
        modifier = Modifier
            .combinedClickable(
                onLongClick = { onLongClick.invoke() },
                onClick = { onClick.invoke() }
            )
            .padding(16.dp)
            .width(160.dp)
            .height(200.dp),
        elevation = 8.dp,
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recipes.recipeTitle,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(4.dp).width(100.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = MaterialTheme.colors.secondary
                )
                IconButton(
                    onClick = { onRecipeEditClick.invoke() },
                    modifier = Modifier
                        .padding(4.dp).width(24.dp).height(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Recipe",
                        tint = MaterialTheme.colors.secondary,
                    )
                }
            }
            Box(
                modifier = Modifier
                    .wrapContentSize()
            ) {
                coilImage(
                    url = recipes.imageUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = MaterialTheme.shapes.small.copy(CornerSize(0.dp))
                )
            }
        }
    }

}