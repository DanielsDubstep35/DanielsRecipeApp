package dm.daniel.violet.view.components.bottombars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeBottomBar(
    navToCategoryScreen: () -> Unit,
    navToHistoryScreen: () -> Unit,
) {
    BottomAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        cutoutShape = MaterialTheme.shapes.large,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            IconButton(onClick = { navToCategoryScreen.invoke() }) {
                Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colors.secondary)
            }

            IconButton(onClick = { }) {
                //Icon(Icons.Default.Home, contentDescription = null, tint = MaterialTheme.colors.secondary)
            }

            IconButton(onClick = { navToHistoryScreen.invoke() }) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = MaterialTheme.colors.secondary)
            }

        }
    }
}