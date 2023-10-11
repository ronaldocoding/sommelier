package br.com.sommelier.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.R
import br.com.sommelier.domain.model.RestaurantDomain
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.Sizing
import br.com.sommelier.ui.theme.Typography
import br.com.sommelier.util.COMMA
import br.com.sommelier.util.DOT
import br.com.sommelier.util.emptyString
import coil.compose.AsyncImage
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantListItem(
    modifier: Modifier = Modifier,
    restaurant: RestaurantDomain,
    borderColor: Color = ColorReference.chineseSilver,
    backgroundColor: Color = ColorReference.white,
    onClick: () -> Unit = {},
) {
    val showShimmer = rememberSaveable { mutableStateOf(true) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(Sizing.largest),
        shape = RoundedCornerShape(Sizing.normal),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(Sizing.almostNone, borderColor),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Sizing.largest),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Sizing.extraLarger)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier
                        .background(shimmerBrush(targetValue = 1300f, showShimmer = showShimmer.value))
                        .size(Sizing.extraLarger)
                        .clip(CircleShape),
                    model = restaurant.profileImageUrl,
                    contentDescription = restaurant.name,
                    onSuccess = { showShimmer.value = false },
                    contentScale = ContentScale.FillBounds
                )
            }

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = restaurant.name, style = Typography.headerSmall)
                Spacer(modifier = Modifier.padding(Sizing.extraSmaller))
                Text(text = restaurant.address, style = Typography.bodySmallest)
            }
            Box(contentAlignment = Alignment.TopCenter) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_star),
                        contentDescription = null // Create string resource to this image
                    )
                    Spacer(modifier = Modifier.padding(Sizing.extraSmaller))
                    Text(
                        text = restaurant.averageRating.toString().replace(DOT, COMMA),
                        style = Typography.bodySmallest
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RestaurantListItemPreview() {
    RestaurantListItem(
        modifier = Modifier.padding(horizontal = Sizing.mediumLarge),
        restaurant = RestaurantDomain(
            name = "Porteira Picanharia",
            description = "Melhor picanharia de Manaus",
            address = "Av. Efigênio Salles, 3050",
            averageRating = 5.0f,
            profileImageUrl = "https://s3-sa-east-1.amazonaws.com/clientefielsp/dados_aplicativos/picanharia_porteira/img_selo/20220505151642_img_selo.png",
            headerImageUrl = emptyString(),
            uid = UUID.randomUUID().toString()
        )
    )
}

