package dm.daniel.violet.viewModel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import dm.daniel.violet.model.models.UserRecipes
import dm.daniel.violet.model.repository.FirebaseRepository

class UserRecipeEditViewmodel (
    private val repository: FirebaseRepository = FirebaseRepository(),
): ViewModel() {
    var recipeUiState by mutableStateOf(UserRecipeEditUiState())
        private set

    private val hasUser:Boolean
        get() = repository.hasUser()

    private val user: FirebaseUser?
        get() = repository.user()

    fun onTitleChange(title:String){
        recipeUiState = recipeUiState.copy(Title = title)
    }

    fun onInstructionChange(instruction: String){
        recipeUiState = recipeUiState.copy(instruction = instruction)
    }

    fun onImageChange(imageUri: Uri?){
        recipeUiState = recipeUiState.copy(imageUri = imageUri)
    }

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

    fun getRecipe(recipeId: String) {
        repository.getRecipe(
            recipeId = recipeId,
            onError = {},
        ) {
            recipeUiState = recipeUiState.copy( selectedRecipe = it)
            recipeUiState.selectedRecipe?.let { it1 -> setRecipeFields(it1) }
        }
    }

    fun updateRecipe(
        recipeId: String
    ) {
        repository.updateRecipe(
            recipeId = recipeId,
            title = recipeUiState.Title,
            instruction = recipeUiState.instruction,
            imageUri = recipeUiState.imageUri,
            favorited = recipeUiState.favorited
        ) {
            recipeUiState = recipeUiState.copy(recipeUpdatedStatus = it)
        }
    }

    fun updateRecipeNoImage(
        recipeId: String
    ) {
        repository.updateRecipeNoImage(
            recipeId = recipeId,
            title = recipeUiState.Title,
            instruction = recipeUiState.instruction,
            favorited = recipeUiState.favorited
        ) {
            recipeUiState = recipeUiState.copy(recipeUpdatedStatus = it)
        }
    }

    fun resetRecipeAddedStatus() {
        recipeUiState = recipeUiState.copy(
            recipeAddedStatus = false,
            recipeUpdatedStatus = false
        )
    }

    fun resetState() {
        recipeUiState = UserRecipeEditUiState()
    }

}

data class UserRecipeEditUiState(
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