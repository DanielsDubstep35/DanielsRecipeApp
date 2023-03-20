package dm.daniel.violet.viewModel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import dm.daniel.violet.model.models.UserRecipes
import dm.daniel.violet.model.repository.FirebaseRepository

class UserRecipeViewmodel(
    private val repository: FirebaseRepository = FirebaseRepository(),
) : ViewModel() {
    var recipeUiState by mutableStateOf(UserRecipeUiState())
        private set

    private val hasUser:Boolean
        get() = repository.hasUser()

    private val user: FirebaseUser?
        get() = repository.user()

    fun addRecipe(){
        if(hasUser){
            repository.addRecipe(
                userId = user!!.uid,
                title = recipeUiState.Title,
                instruction = recipeUiState.instruction,
                imageUri = recipeUiState.imageUri,
                favorited = recipeUiState.favorited
            ){
                recipeUiState = recipeUiState.copy(recipeAddedStatus = it)
            }
        }
    }

    fun getRecipe(recipeId: String) {
        repository.getRecipe(
            recipeId = recipeId,
            onError = {},
        ) {
            recipeUiState = recipeUiState.copy( selectedRecipe = it)
            recipeUiState.selectedRecipe?.let { it1 -> setRecipeFields(it1) }
        }
    }

    fun setRecipeFields(recipe: UserRecipes) {
        recipeUiState = recipeUiState.copy(
            recipeId = recipe.recipeId,
            Title = recipe.recipeTitle,
            instruction = recipe.recipeInstruction,
            imageUrl = recipe.imageUrl,
            favorited = recipe.favorited,
            selectedRecipe = recipe
        )
    }

    fun favoriteRecipe(recipeId: String) {
        recipeUiState = recipeUiState.copy(favorited = true)
        repository.favoritedRecipe(
            recipeId = recipeId,
            favorited = true,
            onResult = {}
        )
    }

    fun resetRecipeAddedStatus() {
        recipeUiState = recipeUiState.copy(
            recipeAddedStatus = false,
            recipeUpdatedStatus = false
        )
    }

    fun resetState() {
        recipeUiState = UserRecipeUiState()
    }

}

data class UserRecipeUiState(
    val recipeId: String = "",
    val Title: String = "",
    val imageUri: Uri? = null,
    val imageUrl: String = "",
    val instruction: String = "",
    val favorited: Boolean = false,
    val recipeAddedStatus: Boolean = false,
    val recipeUpdatedStatus: Boolean = false,
    val selectedRecipe: UserRecipes? = null,
)