package dm.daniel.violet.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dm.daniel.violet.model.models.UserRecipes
import dm.daniel.violet.model.repository.FirebaseRepository
import dm.daniel.violet.model.repository.FirebaseResources
import kotlinx.coroutines.launch

class HistoryViewmodel(
    private val repository: FirebaseRepository = FirebaseRepository(),
): ViewModel() {
    var historyUiState by mutableStateOf(HistoryUiState())

    val user = repository.user()
    val hasUser: Boolean
        get() = repository.hasUser()
    private val userId: String
        get() = repository.getUserId()

    fun loadFavoritedRecipes(){
        if (hasUser){
            if (userId.isNotBlank()){
                getUserRecipes(userId)
                Log.d("HomeViewModel: Line 29, folder: controller/HomeViewModel", "userId = $userId")
            }
        }else{
            historyUiState = historyUiState.copy(userRecipes = FirebaseResources.Failure(
                throwable = Throwable(message = "The User is not Logged In")
            ))
        }
    }

    private fun getUserRecipes(userId:String) = viewModelScope.launch {
        repository.getFavoritedUserRecipes(userId).collect {
            historyUiState = historyUiState.copy(userRecipes = it)
        }
    }

    fun removeRecipe(recipeId:String, favorited:Boolean) = repository.favoritedRecipe(
        recipeId = recipeId,
        favorited = favorited
    ) {
        historyUiState = historyUiState.copy(userRecipeRemovedStatus = it)
    }
}

data class HistoryUiState(

    // May not need for now

    /*
    val recipeId: String = "",
    val Title: String = "",
    val imageUri: Uri? = null,
    val imageUrl: String = "",
    val instruction: String = "",
    */

    val userRecipes: FirebaseResources<List<UserRecipes>> = FirebaseResources.Loading(),
    val userRecipeRemovedStatus: Boolean = false,
)