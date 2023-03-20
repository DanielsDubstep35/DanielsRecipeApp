package dm.daniel.violet.model.models

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dm.daniel.violet.model.repository.FirebaseRepository
import dm.daniel.violet.model.repository.FirebaseResources
import kotlinx.coroutines.launch

class HomeViewmodel(
    private val repository: FirebaseRepository = FirebaseRepository(),
) : ViewModel() {
    var homeUiState by mutableStateOf(HomeUiState())

    val user = repository.user()
    val hasUser: Boolean
        get() = repository.hasUser()
    private val userId: String
        get() = repository.getUserId()

    fun loadRecipes(){
        if (hasUser){
            if (userId.isNotBlank()){
                getUserRecipes(userId)
                Log.d("HomeViewModel: Line 29, folder: controller/HomeViewModel", "userId = $userId")
            }
        }else{
            homeUiState = homeUiState.copy(userRecipes = FirebaseResources.Failure(
                throwable = Throwable(message = "The User is not Logged In")
            ))
        }
    }

    private fun getUserRecipes(userId:String) = viewModelScope.launch {
        repository.getUserRecipes(userId).collect {
            homeUiState = homeUiState.copy(userRecipes = it)
        }
    }

    fun deleteRecipe(recipeId:String) = repository.deleteRecipe(recipeId = recipeId){
        homeUiState = homeUiState.copy(userRecipeDeletedStatus = it)
    }

    fun signOut() = repository.signOut()

}

data class HomeUiState(
    val userRecipes: FirebaseResources<List<UserRecipes>> = FirebaseResources.Loading(),
    val userRecipeDeletedStatus: Boolean = false,
)