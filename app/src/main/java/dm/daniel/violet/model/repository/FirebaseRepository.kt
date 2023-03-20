package dm.daniel.violet.model.repository

import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dm.daniel.violet.model.models.UserRecipes
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val RECIPES_COLLECTION_REF = "Recipes"

class FirebaseRepository() {

    var storage = FirebaseStorage.getInstance()

    fun user() = Firebase.auth.currentUser

    fun hasUser(): Boolean = Firebase.auth.currentUser != null

    fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()

    fun getRecipesRef(): CollectionReference {
        val recipesRef = Firebase.firestore.collection(RECIPES_COLLECTION_REF)
        return recipesRef
    }

    fun getUserRecipes(
        userId: String
    ): Flow<FirebaseResources<List<UserRecipes>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = getRecipesRef()
                .orderBy("recipeId")
                .whereEqualTo("userId", userId)
                .addSnapshotListener{ snapshot, e ->
                    val response = if (snapshot != null) {
                        val recipes = snapshot.toObjects(UserRecipes::class.java)
                        FirebaseResources.Success(data = recipes)
                    } else {
                        FirebaseResources.Failure(throwable = e)
                    }
                    trySend(response)
                }
        } catch (e: Exception) {
            trySend(FirebaseResources.Failure(e.cause))
            e.printStackTrace()
        }

        awaitClose {
            snapshotStateListener?.remove()
        }
    }

    fun getFavoritedUserRecipes(
        userId: String
    ): Flow<FirebaseResources<List<UserRecipes>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = getRecipesRef()
                .orderBy("recipeId")
                .whereEqualTo("userId", userId)
                .whereEqualTo("favorited", true)
                .addSnapshotListener{ snapshot, e ->
                    val response = if (snapshot != null) {
                        val recipes = snapshot.toObjects(UserRecipes::class.java)
                        FirebaseResources.Success(data = recipes)
                    } else {
                        FirebaseResources.Failure(throwable = e)
                    }
                    trySend(response)
                }
        } catch (e: Exception) {
            trySend(FirebaseResources.Failure(e.cause))
            e.printStackTrace()
            println(e)
        }

        awaitClose {
            snapshotStateListener?.remove()
        }
    }

    fun getRecipe(
        recipeId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (UserRecipes?) -> Unit
    ) {
        getRecipesRef()
            .document(recipeId)
            .get()
            .addOnSuccessListener {
                onSuccess.invoke(it.toObject(UserRecipes::class.java))
            }
            .addOnFailureListener { e ->
                onError.invoke(e.cause)
            }

    }

    fun addRecipe(
        userId: String,
        title: String,
        imageUri: Uri?,
        instruction: String,
        favorited: Boolean,
        onComplete: (Boolean) -> Unit
    ) {
        val documentId = getRecipesRef().document().id


        var addFile = imageUri
        val addUserRecipeImageRef = storage.reference.child("Users/${Firebase.auth.currentUser?.uid}/UserRecipeImages/${documentId}")
        var uploadTask = addUserRecipeImageRef.putFile(addFile!!)

        uploadTask.addOnCompleteListener {
            addUserRecipeImageRef.downloadUrl.addOnSuccessListener { Url ->
                // get the download url of the image
                val imageUrl = Url.toString()


                val recipe = UserRecipes(
                    userId = userId,
                    recipeId = documentId,
                    recipeTitle = title,
                    imageUrl = imageUrl,
                    recipeInstruction = instruction,
                    favorited = favorited,
                )

                getRecipesRef()
                    .document(documentId)
                    .set(recipe)
                    .addOnCompleteListener { result ->
                        onComplete.invoke(result.isSuccessful)
                    }
            }
        }

    }

    fun addToMyRecipes(
        userId: String,
        title: String,
        url: String,
        instruction: String,
        favorited: Boolean,
        onComplete: (Boolean) -> Unit
    ) {
        val documentId = getRecipesRef().document().id

        val recipe = UserRecipes(
            userId = userId,
            recipeId = documentId,
            recipeTitle = title,
            imageUrl = url,
            recipeInstruction = instruction,
            favorited = favorited,
        )

        getRecipesRef()
            .document(documentId)
            .set(recipe)
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }

    }

    fun deleteRecipe(
        recipeId: String,
        onComplete: (Boolean) -> Unit
    ) {

        val addUserRecipeImageRef = storage.reference.child("Users/${Firebase.auth.currentUser?.uid}/UserRecipeImages/${recipeId}")
        var deleteTask = addUserRecipeImageRef.delete()

        deleteTask.addOnCompleteListener {
            getRecipesRef().document(recipeId)
                .delete()
                .addOnCompleteListener {
                    onComplete.invoke(it.isSuccessful)
                }
        }
    }

    fun updateRecipe(
        recipeId: String,
        title: String,
        imageUri: Uri?,
        instruction: String,
        favorited: Boolean,
        onResult: (Boolean) -> Unit
    ) {

        // upload recipe image to firebase storage, which is separate from firebase firestore
        var updateFile = imageUri
        val updateRecipeImageRef = storage.reference.child("Users/${Firebase.auth.currentUser?.uid}/Images/${recipeId}")
        var updateUploadTask = updateRecipeImageRef.putFile(updateFile!!)

        updateUploadTask.addOnCompleteListener {
            updateRecipeImageRef.downloadUrl.addOnSuccessListener { Url ->
                // get the download url of the image
                val imageUrl = Url.toString()

                val updateData = hashMapOf<String, Any>(
                    "recipeTitle" to title,
                    "recipeInstruction" to instruction,
                    "imageUrl" to imageUrl,
                    "favorited" to favorited,
                )

                getRecipesRef().document(recipeId)
                    .update(updateData)
                    .addOnCompleteListener {
                        onResult(it.isSuccessful)
                    }

            }
        }

    }

    fun updateRecipeNoImage(
        recipeId: String,
        title: String,
        instruction: String,
        favorited: Boolean,
        onResult: (Boolean) -> Unit
    ) {

        // upload recipe image to firebase storage, which is separate from firebase firestore

        val updateData = hashMapOf<String, Any>(
            "recipeTitle" to title,
            "recipeInstruction" to instruction,
            "favorited" to favorited,
        )

        getRecipesRef().document(recipeId)
            .update(updateData)
            .addOnCompleteListener {
                onResult(it.isSuccessful)
            }

    }

    fun favoritedRecipe(
        recipeId: String,
        favorited: Boolean,
        onResult: (Boolean) -> Unit
    ) {

        val updateData = hashMapOf<String, Any>(
            "favorited" to favorited,
        )

        getRecipesRef().document(recipeId)
            .update(updateData)
            .addOnCompleteListener {
                onResult(it.isSuccessful)
            }

    }

    fun signOut() = Firebase.auth.signOut()

}


sealed class FirebaseResources<T>(
    val data: T? = null,
    val throwable: Throwable? = null,
) {
    class Loading<T> : FirebaseResources<T>()
    class Success<T>(data: T?) : FirebaseResources<T>(data = data)
    class Failure<T>(throwable: Throwable?) : FirebaseResources<T>(throwable = throwable)
}