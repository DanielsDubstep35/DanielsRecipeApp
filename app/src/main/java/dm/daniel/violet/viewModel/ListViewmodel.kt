package dm.daniel.violet.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dm.daniel.violet.model.api.MealApi
import dm.daniel.violet.model.api.Recipe
import kotlinx.coroutines.launch

sealed interface ListUiState {
    data class Success(val recipes: List<Recipe>): ListUiState
    object Error: ListUiState
    object Loading: ListUiState
}

class ListViewmodel: ViewModel() {
    var listUiState: ListUiState by mutableStateOf(ListUiState.Loading)
        private set

    fun getCategoryFromNavigation(category: String?): ListViewmodel {
        getFoodByCategory(category = category)
        return this
    }

    fun getFoodByCategory(category: String?) {
        viewModelScope.launch {
            var mealApi: MealApi? = null
            try {
                mealApi = MealApi.getInstance()
                listUiState = ListUiState.Success(recipes = mealApi.getFoodByCategory(category = category).meals)
            } catch (e: Exception) {
                Log.d("CATEGORYVIEWMODEL, Error from getFoodByCategory", e.message.toString())
                listUiState = ListUiState.Error
                // return url that caused error
                println(mealApi?.getFoodByCategory(category = category)?.meals)
            }
        }
    }
}