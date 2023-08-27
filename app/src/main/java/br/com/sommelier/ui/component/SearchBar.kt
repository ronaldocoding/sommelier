package br.com.sommelier.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.R
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.Sizing
import br.com.sommelier.ui.theme.Spacing
import br.com.sommelier.ui.theme.Typography
import br.com.sommelier.util.emptyString

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String = emptyString(),
    hint: String = emptyString(),
    containerColor: Color = ColorReference.antiFlashWhite,
    searchIconTint: Color = ColorReference.quartz,
    onQueryChange: (String) -> Unit = {},
    onSearch: () -> Unit = {}
) {
    BasicTextField(
        singleLine = true,
        value = query,
        onValueChange = onQueryChange,
        textStyle = Typography.bodyLarge.copy(color = ColorReference.eerieBlack),
        cursorBrush = SolidColor(ColorReference.royalPurple),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
            }
        ),
        modifier = modifier
            .wrapContentSize()
            .border(
                width = Sizing.almostNone,
                color = containerColor,
                shape = RoundedCornerShape(Sizing.normal)
            )
            .background(
                color = containerColor,
                shape = RoundedCornerShape(Sizing.normal)
            )
            .fillMaxWidth()
            .testTag("SearchBar")
    ) { innerTextField ->
        Row(
            modifier = Modifier.padding(
                start = Spacing.small,
                top = Spacing.smaller,
                bottom = Spacing.smaller,
                end = Spacing.small
            ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                contentDescription = "Search Icon",
                tint = searchIconTint,
                modifier = Modifier.testTag("SearchBarIcon")
            )
            Spacer(modifier = Modifier.padding(start = Spacing.small))
            Box {
                if (query.isEmpty()) {
                    Text(
                        text = hint,
                        style = Typography.bodyLarge,
                        color = ColorReference.taupeGray,
                        modifier = Modifier.testTag("SearchBarHint")
                    )
                }
                innerTextField()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarDefaultPreview() {
    SearchBar()
}

@Preview(showBackground = true)
@Composable
fun SearchBarWithQueryPreview() {
    var query by rememberSaveable { mutableStateOf(emptyString()) }
    SearchBar(query = query, hint = "Search a restaurant", onQueryChange = { query = it })
}
