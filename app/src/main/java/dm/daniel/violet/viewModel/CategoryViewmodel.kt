package dm.daniel.violet.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dm.daniel.violet.model.api.Category
import dm.daniel.violet.model.api.MealApi
import kotlinx.coroutines.launch

sealed interface CategoryUiState {
    data class Success(val categories: List<Category>): CategoryUiState
    object Error: CategoryUiState
    object Loading: CategoryUiState
}

class CategoryViewmodel : ViewModel() {
    var categoriesUIState: CategoryUiState by mutableStateOf(CategoryUiState.Loading)
        private set

    init {
        getCategories()
    }

    private fun getCategories() {
        viewModelScope.launch {
            var mealApi: MealApi? = null
            try {
                mealApi = MealApi.getInstance()
                categoriesUIState = CategoryUiState.Success(categories = mealApi.getCategories().meals)
            } catch (e: Exception) {
                Log.d("CATEGORYVIEWMODEL, Error from getCategories", e.message.toString())
                categoriesUIState = CategoryUiState.Error
            }
        }
    }

}

/*
sealed interface CategoryUIState {
    data class Success(val categories: List<Category>): CategoryUIState
    object Error: CategoryUIState
    object Loading: CategoryUIState
}
*/