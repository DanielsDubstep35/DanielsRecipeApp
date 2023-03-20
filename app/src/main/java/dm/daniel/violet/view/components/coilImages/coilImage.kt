package dm.daniel.violet.view.components.bottombars

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import dm.daniel.violet.R

@Composable
fun coilImage(url: String, modifier: Modifier, shape: Shape) {

    val painter = rememberAsyncImagePainter(
        model = url,
        imageLoader = ImageLoader.Builder(LocalContext.current).crossfade(true).placeholder(R.drawable.ic_launcher_background).crossfade(300).build()
    )

    Card(
        shape = shape,
        modifier = modifier,
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Image(
            painter = painter,
            contentDescription = "ImagePainter",
            contentScale = ContentScale.Crop,
        )
    }

}

@Composable
fun coilImage(uri: Uri?, modifier: Modifier, shape: Shape) {

    Card(
        modifier = Modifier
            .width(300.dp)
            .height(250.dp)
            .padding(16.dp),
    ) {

        AsyncImage(
            model = uri,
            contentDescription = "ImagePainter",
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun coilImage(modifier: Modifier) {

    Card(
        modifier = Modifier,
        shape = MaterialTheme.shapes.small.copy(
            all = CornerSize(0.dp)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "No Image Found",
                modifier = Modifier
                    .size(24.dp)
            )
            Text(
                text = "No Image Found",
                style = TextStyle(
                    fontStyle = MaterialTheme.typography.body1.fontStyle,
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 16.sp,
                )
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "No Image Found",
                modifier = Modifier
                    .size(24.dp)
            )

        }
    }
}

