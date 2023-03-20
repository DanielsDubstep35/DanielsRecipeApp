package dm.daniel.violet.model.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class JsonCategoriesData(
    var meals: List<Category>
)

data class JsonRecipeData(
    var meals: List<Recipe>
)

data class JsonRecipe(
    var meals: List<Recipe>
)

data class Category(
    var strCategory : String
)

data class Recipe(
    var strMeal: String,
    var strMealThumb: String,
    var idMeal: String,
    var strInstructions: String,
)

const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

interface MealApi {
    @GET("list.php?c=list")
    suspend fun getCategories(): JsonCategoriesData

    @GET("filter.php")
    suspend fun getFoodByCategory(@Query("c") category: String?): JsonRecipeData

    @GET("lookup.php")
    suspend fun getRecipe(@Query("i") id: String): JsonRecipe

    companion object {
        var mealService: MealApi? = null

        fun getInstance(): MealApi {
            if (mealService === null) {
                mealService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(MealApi::class.java)
            }
            return mealService!!
        }
    }

}