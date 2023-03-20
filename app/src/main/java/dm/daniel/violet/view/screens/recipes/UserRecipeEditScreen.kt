package dm.daniel.violet.view.screens.recipes

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dm.daniel.violet.view.components.bottombars.coilImage
import dm.daniel.violet.view.components.topbars.GenericNewRecipeTop
import dm.daniel.violet.viewModel.UserRecipeEditViewmodel
import kotlinx.coroutines.launch

@Composable
fun UserRecipeEditScreen(
    userRecipeEditViewmodel: UserRecipeEditViewmodel,
    recipeId: String,
    navController: NavController
) {
    val userRecipeUiState = userRecipeEditViewmodel.recipeUiState

    val isFormsNotBlank = userRecipeUiState.Title.isNotBlank() &&
            userRecipeUiState.instruction.isNotBlank()

    val isRecipeIdNotBlank = recipeId.isNotBlank()

    LaunchedEffect(key1 = Unit) {
        if (isRecipeIdNotBlank) {
            userRecipeEditViewmodel.getRecipe(recipeId = recipeId)
        } else {
            userRecipeEditViewmodel.resetState()
        }
    }

    var pickedPhoto by remember { mutableStateOf<Uri?>(null) }

    if (pickedPhoto != null) {
        // Image selected
        userRecipeEditViewmodel.onImageChange(pickedPhoto)
    } else {
        // No image selected
        userRecipeEditViewmodel.onImageChange(null)
    }

    val singlePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> pickedPhoto = uri }
    )

    val scope = rememberCoroutineScope()

    val scaffoldState = rememberScaffoldState()

    val saveOrUpdate = if (isRecipeIdNotBlank) { "Update this existing recipe" } else { "Save this new recipe" }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            GenericNewRecipeTop(navController = navController)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
                .padding(padding),
            verticalArrangement = Arrangement.Top
        ) {

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                if (userRecipeUiState.recipeAddedStatus) {
                    LaunchedEffect(
                        key1 = Unit,
                        block = {
                            scope.launch {
                                scaffoldState.snackbarHostState
                                    .showSnackbar("Added Recipe Successfully")
                                userRecipeEditViewmodel.resetRecipeAddedStatus()
                            }
                        }
                    )
                }

                if (userRecipeUiState.recipeUpdatedStatus) {
                    LaunchedEffect(
                        key1 = Unit,
                        block = {
                            scope.launch {
                                scaffoldState.snackbarHostState
                                    .showSnackbar("Updated Recipe Successfully")
                                userRecipeEditViewmodel.resetRecipeAddedStatus()
                            }
                        }
                    )
                }

                if (!isRecipeIdNotBlank) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(color = MaterialTheme.colors.background),
                        contentAlignment = Alignment.Center
                    ) {
                        if (pickedPhoto != null) {
                            coilImage(uri = pickedPhoto, modifier = Modifier
                                .fillMaxSize()
                                .alpha(0.5f), shape = MaterialTheme.shapes.small.copy(CornerSize(0.dp) ) )
                        } else {
                            coilImage(
                                url = userRecipeUiState.imageUrl,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .alpha(0.5f),
                                shape = MaterialTheme.shapes.small.copy(CornerSize(0.dp))
                            )
                        }

                        // Image Picker
                        Button(
                            onClick = {
                                // Open the users gallery, and get the image
                                singlePhotoLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )

                            },
                            modifier = Modifier
                                .height(60.dp)
                                .wrapContentWidth()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Pick an Image",
                                style = TextStyle(
                                    fontStyle = MaterialTheme.typography.body1.fontStyle,
                                    color = MaterialTheme.colors.secondary,
                                    fontSize = 16.sp,
                                )
                            )
                        }

                    }

                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.wrapContentHeight().padding(vertical = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentSize()
                                    .padding(vertical = 8.dp)
                                    .background(color = MaterialTheme.colors.secondary),
                            ) {
                                Column(
                                    modifier = Modifier.background(color = MaterialTheme.colors.primary)
                                ) {
                                    Text(text = "Title", color = MaterialTheme.colors.secondary, modifier = Modifier.padding(horizontal = 8.dp) )

                                    OutlinedTextField(
                                        value = userRecipeUiState.Title,
                                        onValueChange = { userRecipeEditViewmodel.onTitleChange(it) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        textStyle = TextStyle(
                                            fontStyle = MaterialTheme.typography.body1.fontStyle,
                                            color = MaterialTheme.colors.secondary,
                                            fontSize = 20.sp,
                                        ),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = MaterialTheme.colors.secondary,
                                            unfocusedBorderColor = MaterialTheme.colors.secondary,
                                            cursorColor = MaterialTheme.colors.secondary,
                                            disabledBorderColor = MaterialTheme.colors.secondary,
                                            focusedLabelColor = MaterialTheme.colors.secondary,
                                            unfocusedLabelColor = MaterialTheme.colors.secondary,
                                            disabledLabelColor = MaterialTheme.colors.secondary,
                                            backgroundColor = MaterialTheme.colors.primary,
                                            textColor = MaterialTheme.colors.secondary,
                                        )
                                    )
                                }
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentSize()
                                    .padding(vertical = 8.dp)
                                    .background(color = MaterialTheme.colors.secondary),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .background(color = MaterialTheme.colors.primary)
                                ) {
                                    Text(text = "Instructions", color = MaterialTheme.colors.secondary, modifier = Modifier.padding(horizontal = 8.dp) )

                                    BasicTextField(
                                        value = userRecipeUiState.instruction,
                                        onValueChange = { userRecipeEditViewmodel.onInstructionChange(it) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(150.dp)
                                            .padding(8.dp),
                                        textStyle = TextStyle(
                                            fontStyle = MaterialTheme.typography.body1.fontStyle,
                                            color = MaterialTheme.colors.secondary,
                                            fontSize = 20.sp,
                                        )
                                    )
                                }
                            }
                        }

                        // Save or Update
                        Button(
                            onClick = {
                                if (pickedPhoto == null) {
                                    scope.launch {
                                        scaffoldState.snackbarHostState
                                            .showSnackbar("Please Choose an Image")
                                        userRecipeEditViewmodel.resetRecipeAddedStatus()
                                    }
                                } else {
                                    userRecipeEditViewmodel?.addRecipe()
                                }
                            },
                            modifier = Modifier
                                .height(60.dp)
                                .wrapContentWidth()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = saveOrUpdate,
                                style = TextStyle(
                                    fontStyle = MaterialTheme.typography.body1.fontStyle,
                                    color = MaterialTheme.colors.secondary,
                                    fontSize = 16.sp,
                                )
                            )
                        }
                    }
                } else if (isRecipeIdNotBlank) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(color = MaterialTheme.colors.background),
                        contentAlignment = Alignment.Center
                    ) {
                        if (pickedPhoto != null) {
                            coilImage(uri = pickedPhoto, modifier = Modifier
                                .fillMaxSize()
                                .alpha(0.5f), shape = MaterialTheme.shapes.small.copy(CornerSize(0.dp) ) )
                        } else {
                            coilImage(
                                url = userRecipeUiState.imageUrl,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .alpha(0.5f),
                                shape = MaterialTheme.shapes.small.copy(CornerSize(0.dp))
                            )
                        }

                        // Image Picker
                        Button(
                            onClick = {
                                // Open the users gallery, and get the image
                                singlePhotoLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )

                            },
                            modifier = Modifier
                                .height(60.dp)
                                .wrapContentWidth()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Pick an Image",
                                style = TextStyle(
                                    fontStyle = MaterialTheme.typography.body1.fontStyle,
                                    color = MaterialTheme.colors.secondary,
                                    fontSize = 16.sp,
                                )
                            )
                        }

                    }

                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.wrapContentHeight().padding(vertical = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentSize()
                                    .padding(vertical = 8.dp)
                                    .background(color = MaterialTheme.colors.secondary),
                            ) {
                                Column(
                                    modifier = Modifier.background(color = MaterialTheme.colors.primary)
                                ) {
                                    Text(text = "Title", color = MaterialTheme.colors.secondary, modifier = Modifier.padding(horizontal = 8.dp) )

                                    OutlinedTextField(
                                        value = userRecipeUiState.Title,
                                        onValueChange = { userRecipeEditViewmodel.onTitleChange(it) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        textStyle = TextStyle(
                                            fontStyle = MaterialTheme.typography.body1.fontStyle,
                                            color = MaterialTheme.colors.secondary,
                                            fontSize = 20.sp,
                                        ),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = MaterialTheme.colors.secondary,
                                            unfocusedBorderColor = MaterialTheme.colors.secondary,
                                            cursorColor = MaterialTheme.colors.secondary,
                                            disabledBorderColor = MaterialTheme.colors.secondary,
                                            focusedLabelColor = MaterialTheme.colors.secondary,
                                            unfocusedLabelColor = MaterialTheme.colors.secondary,
                                            disabledLabelColor = MaterialTheme.colors.secondary,
                                            backgroundColor = MaterialTheme.colors.primary,
                                            textColor = MaterialTheme.colors.secondary,
                                        )
                                    )
                                }
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentSize()
                                    .padding(vertical = 8.dp)
                                    .background(color = MaterialTheme.colors.secondary),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .background(color = MaterialTheme.colors.primary)
                                ) {
                                    Text(text = "Instructions", color = MaterialTheme.colors.secondary, modifier = Modifier.padding(horizontal = 8.dp) )

                                    BasicTextField(
                                        value = userRecipeUiState.instruction,
                                        onValueChange = { userRecipeEditViewmodel.onInstructionChange(it) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(150.dp)
                                            .padding(8.dp),
                                        textStyle = TextStyle(
                                            fontStyle = MaterialTheme.typography.body1.fontStyle,
                                            color = MaterialTheme.colors.secondary,
                                            fontSize = 20.sp,
                                        )
                                    )
                                }
                            }
                        }

                        // Save or Update
                        Button(
                            onClick = {
                                if (pickedPhoto == null && userRecipeUiState.imageUrl == "") {
                                    scope.launch {
                                        scaffoldState.snackbarHostState
                                            .showSnackbar("Please Choose an Image")
                                        userRecipeEditViewmodel.resetRecipeAddedStatus()
                                    }
                                } else {
                                    userRecipeEditViewmodel?.updateRecipeNoImage(recipeId = recipeId)
                                }
                            },
                            modifier = Modifier
                                .height(60.dp)
                                .wrapContentWidth()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = saveOrUpdate,
                                style = TextStyle(
                                    fontStyle = MaterialTheme.typography.body1.fontStyle,
                                    color = MaterialTheme.colors.secondary,
                                    fontSize = 16.sp,
                                )
                            )
                        }
                    }

                }
            }

        }

    }

}