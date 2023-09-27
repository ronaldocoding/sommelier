package br.com.sommelier.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import br.com.sommelier.R
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.Sizing
import br.com.sommelier.ui.theme.SommelierTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePicker(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(size = Sizing.normal),
    width: Dp? = null,
    height: Dp = Sizing.massive,
    image: Painter? = null,
    imageContentDescription: String? = null,
    backgroundColor: Color = ColorReference.royalPurple,
    pickerIcon: ImageVector = ImageVector.vectorResource(id = R.drawable.camera_plus_large),
    pickerIconDescription: String? = null,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.getModifier(width, height),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        onClick = onClick
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (image != null) {
                Image(
                    painter = image,
                    contentDescription = imageContentDescription,
                    contentScale = ContentScale.FillBounds
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape)
                        .background(color = ColorReference.blackAlpha50)
                )
            }
            Image(imageVector = pickerIcon, contentDescription = pickerIconDescription)
        }
    }
}

private fun Modifier.getModifier(width: Dp?, height: Dp): Modifier {
    return if (width != null) {
        then(
            Modifier
                .width(width)
                .height(height)
        )
    } else {
        then(
            Modifier
                .fillMaxWidth()
                .height(height)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ImagePickerDefaultPreview() {
    ImagePicker()
}

@Composable
@Preview(showBackground = true)
fun ImagePickerWithImagePreview() {
    ImagePicker(
        image = painterResource(id = R.drawable.porteira_picanharia),
        imageContentDescription = "Restaurant header"
    )
}

@Composable
@Preview(showBackground = true)
fun TwoImagePickersPreview() {
    SommelierTheme {
        Box(modifier = Modifier.padding(horizontal = Sizing.mediumLarge)) {
            ImagePicker()
            ImagePicker(
                modifier = Modifier.padding(top = Sizing.largest),
                width = Sizing.extraLarger,
                height = Sizing.extraLarger,
                shape = CircleShape,
                backgroundColor = ColorReference.frenchFuchsia,
                pickerIcon = ImageVector.vectorResource(id = R.drawable.camera_plus_small)
            )

        }
    }
}

@Composable
@Preview(showBackground = true)
fun TwoImagePickersWithImagesPreview() {
    SommelierTheme {
        Box(modifier = Modifier.padding(horizontal = Sizing.mediumLarge)) {
            ImagePicker(
                image = painterResource(id = R.drawable.porteira_picanharia),
                imageContentDescription = "Restaurant header"
            )
            ImagePicker(
                modifier = Modifier.padding(top = Sizing.largest),
                width = Sizing.extraLarger,
                height = Sizing.extraLarger,
                image = painterResource(id = R.drawable.porteira_picanharia_profile),
                imageContentDescription = "Restaurant profile",
                shape = CircleShape,
                backgroundColor = ColorReference.frenchFuchsia,
                pickerIcon = ImageVector.vectorResource(id = R.drawable.camera_plus_small)
            )

        }
    }
}
