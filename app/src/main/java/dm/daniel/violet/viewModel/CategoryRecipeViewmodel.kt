package dm.daniel.violet.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dm.daniel.violet.model.api.MealApi
import dm.daniel.violet.model.api.Recipe
import dm.daniel.violet.model.repository.FirebaseRepository
import kotlinx.coroutines.launch

sealed interface CatRecipeUiState {
    data class Success(val recipe: List<Recipe>): CatRecipeUiState
    object Error: CatRecipeUiState
    object Loading: CatRecipeUiState
}

class CategoryRecipeViewmodel: ViewModel() {
    private val repository: FirebaseRepository = FirebaseRepository()

    val user = repository.user()
    val hasUser: Boolean
        get() = repository.hasUser()
    private val userId: String
        get() = repository.getUserId()

    var catRecipeUiState: CatRecipeUiState by mutableStateOf(CatRecipeUiState.Loading)
        private set

    fun getRecipeFromNavigation(id: String): CategoryRecipeViewmodel {
        getRecipe(id = id)
        return this
    }

    fun getRecipe(id: String) {
        viewModelScope.launch {
            var mealApi: MealApi? = null
            try {
                mealApi = MealApi.getInstance()
                catRecipeUiState = CatRecipeUiState.Success(recipe = mealApi.getRecipe(id = id).meals)
            } catch (e: Exception) {
                Log.d("CATEGORYVIEWMODEL, Error from getFoodByCategory", e.message.toString())
                catRecipeUiState = CatRecipeUiState.Error
                // return url that caused error
            }
        }
    }

    fun AddToMyRecipes(recipe: Recipe) {
        viewModelScope.launch {
            FirebaseRepository().addToMyRecipes(userId = userId, title = recipe.strMeal, url = recipe.strMealThumb, instruction = recipe.strInstructions, favorited = false, onComplete = {})
        }
    }
}