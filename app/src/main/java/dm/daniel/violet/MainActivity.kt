package dm.daniel.violet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.FirebaseApp
import dm.daniel.violet.model.models.HomeViewmodel
import dm.daniel.violet.view.theme.VioletTheme
import dm.daniel.violet.viewModel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            val loginViewModel = viewModel(modelClass = LoginViewmodel::class.java)
            val userRecipeEditViewModel = viewModel(modelClass = UserRecipeEditViewmodel::class.java)
            val userRecipeViewModel = viewModel(modelClass = UserRecipeViewmodel::class.java)
            val categoryRecipeViewmodel = viewModel(modelClass = CategoryRecipeViewmodel::class.java)
            val homeViewModel = viewModel(modelClass = HomeViewmodel::class.java)
            val categoryViewModel = viewModel(modelClass = CategoryViewmodel::class.java)
            val historyViewModel = viewModel(modelClass = HistoryViewmodel::class.java)
            val listViewModel = viewModel(modelClass = ListViewmodel::class.java)
            VioletTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Navigation(
                        loginViewModel = loginViewModel,
                        userRecipeEditViewmodel = userRecipeEditViewModel,
                        userRecipeViewmodel = userRecipeViewModel,
                        categoryRecipeViewmodel = categoryRecipeViewmodel,
                        homeViewModel = homeViewModel,
                        categoryViewModel = categoryViewModel,
                        historyViewModel = historyViewModel,
                        listViewModel = listViewModel
                    )
                }
            }
        }
    }
}
