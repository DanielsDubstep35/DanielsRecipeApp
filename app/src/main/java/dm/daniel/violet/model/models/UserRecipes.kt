package dm.daniel.violet.model.models

data class UserRecipes(
    val recipeId: String = "",
    val recipeTitle: String = "",
    val userId: String = "",
    val imageUrl: String = "",
    val recipeInstruction: String = "",
    val favorited: Boolean = false,
)
