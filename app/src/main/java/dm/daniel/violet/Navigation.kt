package dm.daniel.violet

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dm.daniel.violet.model.models.HomeViewmodel
import dm.daniel.violet.view.screens.login.SignUpScreen
import dm.daniel.violet.view.screens.login.SigninScreen
import dm.daniel.violet.view.screens.main.CategoryScreen
import dm.daniel.violet.view.screens.main.HistoryScreen
import dm.daniel.violet.view.screens.main.HomeScreen
import dm.daniel.violet.view.screens.recipes.CategoryRecipeScreen
import dm.daniel.violet.view.screens.recipes.RecipeListScreen
import dm.daniel.violet.view.screens.recipes.UserRecipeEditScreen
import dm.daniel.violet.view.screens.recipes.UserRecipeScreen
import dm.daniel.violet.viewModel.*

@Composable
fun Navigation (
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewmodel,
    userRecipeEditViewmodel: UserRecipeEditViewmodel,
    userRecipeViewmodel: UserRecipeViewmodel,
    categoryRecipeViewmodel: CategoryRecipeViewmodel,
    homeViewModel: HomeViewmodel,
    categoryViewModel: CategoryViewmodel,
    historyViewModel: HistoryViewmodel,
    listViewModel: ListViewmodel,
) {
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        authGraph(navController, loginViewModel)
        homeGraph(
            navController = navController,
            userRecipeEditViewmodel = userRecipeEditViewmodel,
            userRecipeViewmodel = userRecipeViewmodel,
            categoryRecipeViewmodel = categoryRecipeViewmodel,
            homeViewModel = homeViewModel,
            categoryViewModel = categoryViewModel,
            historyViewModel = historyViewModel,
            listViewModel = listViewModel,
        )
    }
}

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    loginViewModel: LoginViewmodel,
) {
    navigation(
    startDestination = "signin",
    route = "login"
    ) {
        composable(route = "signin") {
            SigninScreen(
                onNavToHomePage = {
                    navController.navigate("main") {
                        launchSingleTop = true
                        popUpTo(route = "signin") {
                            inclusive = true
                        }
                    }
                },
                loginViewModel = loginViewModel,
            ) {
                navController.navigate("signup") {
                    launchSingleTop = true
                    popUpTo("signin") {
                        inclusive = true
                    }
                }
            }
        }
        composable(route = "signup") {
            SignUpScreen(
                onNavToHomePage = {
                    navController.navigate("main") {
                        popUpTo("signup") {
                            inclusive = true
                        }
                    }
                },
                loginViewModel = loginViewModel
            ) {
                navController.navigate("signin")
            }
        }
    }
}

fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    userRecipeEditViewmodel: UserRecipeEditViewmodel,
    userRecipeViewmodel: UserRecipeViewmodel,
    categoryRecipeViewmodel: CategoryRecipeViewmodel,
    homeViewModel: HomeViewmodel,
    categoryViewModel: CategoryViewmodel,
    historyViewModel: HistoryViewmodel,
    listViewModel: ListViewmodel,
) {
    navigation(
        startDestination = "home",
        route = "main"
    ) {
        composable(route = "home") {
            HomeScreen(
                homeViewModel = homeViewModel,
                onRecipeClick = { recipeId ->
                    navController.navigate(
                        "userRecipe?recipeId=$recipeId"
                    ) {
                        launchSingleTop = true
                    }
                },
                navToRecipePage = {
                    navController.navigate("userRecipeEdit?recipeId={RecipeId}") // this triggers route = "Recipe?id={id}" without any id. The id is created in firebase when the recipe is saved, and it is assigned in the FirebaseRepository (recipeId = getRecipesRef().document().id)
                },
                navToRecipeEditPage = { recipeId ->
                    navController.navigate(
                        "userRecipeEdit?recipeId=$recipeId"
                    ) {
                        launchSingleTop = true
                    }
                },
                navToCategoryScreen = {
                    navController.navigate(
                        "category"
                    ) {
                        launchSingleTop = true
                        popUpTo(route = "home") {
                            inclusive = true
                        }
                    }
                },
                navToHistoryScreen = {
                    navController.navigate(
                        "history"
                    ) {
                        launchSingleTop = true
                        popUpTo(route = "home") {
                            inclusive = true
                        }
                    }
                },
            ) {
                navController.navigate("signin") {
                    launchSingleTop = true
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
        }
        composable(
            route = "userRecipeEdit?recipeId={RecipeId}",
            arguments = listOf(navArgument("RecipeId") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { recipe ->
            UserRecipeEditScreen(
                userRecipeEditViewmodel = userRecipeEditViewmodel,
                recipeId = recipe.arguments?.getString("RecipeId") as String,
                navController = navController
            )
        }
        composable(
            route = "userRecipe?recipeId={RecipeId}",
            arguments = listOf(navArgument("RecipeId") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { recipe ->
            UserRecipeScreen(
                userRecipeViewmodel = userRecipeViewmodel,
                recipeId = recipe.arguments?.getString("RecipeId") as String,
                navController = navController
            )
        }
        composable(
            route = "apiRecipe?recipeId={apiRecipeId}",
            arguments = listOf(navArgument("apiRecipeId") {
                type = NavType.StringType
            })
        ) { apiRecipe ->
            CategoryRecipeScreen(
                categoryRecipeViewmodel = categoryRecipeViewmodel.getRecipeFromNavigation(apiRecipe.arguments?.getString("apiRecipeId") as String),
                navController = navController,
                uiState = categoryRecipeViewmodel.catRecipeUiState,
            )
        }

        composable(route = "category") {
            CategoryScreen(
                categoryViewModel = categoryViewModel,
                onCategoryClick = { category ->
                    navController.navigate(
                        "List?listCategory=$category"
                    ) {
                        launchSingleTop = true
                    }
                },
                navToHistoryScreen = {
                    navController.navigate(
                        "history"
                    ) {
                        launchSingleTop = true
                        popUpTo(route = "home") {
                            inclusive = true
                        }
                    }
                },
                navToHomeScreen = {
                    navController.navigate(
                        "home"
                    ) {
                        launchSingleTop = true
                        popUpTo(route = "home") {
                            inclusive = true
                        }
                    }
                },
                uiState = categoryViewModel.categoriesUIState
            )
        }

        composable(
            route = "history",
        ) {
            HistoryScreen(
                historyViewmodel = historyViewModel,
                onRecipeClick = { recipeId ->
                    navController.navigate(
                        "userRecipe?recipeId=$recipeId"
                    ) {
                        launchSingleTop = true
                    }
                },
                navController = navController,
                navToCategoryScreen = {
                    navController.navigate(
                        "category"
                    ) {
                        launchSingleTop = true
                        popUpTo(route = "home") {
                            inclusive = true
                        }
                    }
                },
                navToHomeScreen = {
                    navController.navigate(
                        "home"
                    ) {
                        launchSingleTop = true
                        popUpTo(route = "home") {
                            inclusive = true
                        }
                    }
                },
            )
        }

        composable(route = "List?listCategory={category}",
            arguments = listOf(navArgument("category") {
                type = NavType.StringType
            })
        ) { list ->
            RecipeListScreen(
                listViewmodel = listViewModel.getCategoryFromNavigation(list.arguments?.getString("category") as String),
                category = list.arguments?.getString("category") as String,
                navController = navController,
                navToRecipeScreen = { recipeId ->
                    navController.navigate(
                        "apiRecipe?recipeId=$recipeId"
                    ) {
                        launchSingleTop = true
                    }
                },
                uiState = listViewModel.listUiState
            )
        }

    }
}
