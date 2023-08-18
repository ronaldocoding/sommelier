package br.com.sommelier.presentation.home.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.sommelier.R
import br.com.sommelier.presentation.home.action.HomeAction
import br.com.sommelier.presentation.home.model.HomeUiModel
import br.com.sommelier.presentation.home.state.HomeEffect
import br.com.sommelier.presentation.home.viewmodel.HomeViewModel
import br.com.sommelier.ui.component.QuickActionButton
import br.com.sommelier.ui.component.SearchBar
import br.com.sommelier.ui.theme.ColorReference
import br.com.sommelier.ui.theme.SommelierTheme
import br.com.sommelier.ui.theme.Spacing
import br.com.sommelier.ui.theme.Typography
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreen() {
    val viewModel = getViewModel<HomeViewModel>()
    val uiState = checkNotNull(viewModel.uiState.observeAsState())
    val uiModel = checkNotNull(uiState.value?.uiModel)

    SommelierTheme {
        Scaffold(
            floatingActionButton = {
                QuickActionButton(
                    modifier = Modifier.padding(Spacing.small),
                    onClick = {
                        viewModel.sendAction(HomeAction.Action.OnClickAddRestaurantButton)
                    }
                )
            },
            containerColor = ColorReference.white
        ) {
            UiState(it, viewModel, uiModel)
            UiEffect(viewModel)
        }
    }
}

@Composable
private fun UiState(
    it: PaddingValues,
    viewModel: HomeViewModel,
    uiModel: HomeUiModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .verticalScroll(rememberScrollState())
    ) {
        HomeTopBar(viewModel)
        HomeSearchBar(uiModel, viewModel)
        RestaurantsTitle()
    }
    Restaurants()
}

@Composable
private fun HomeTopBar(viewModel: HomeViewModel) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = Spacing.mediumLarge,
                    end = Spacing.normalLarge,
                    top = Spacing.small
                )
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = Typography.displayMedium
            )
            IconButton(
                onClick = {
                    viewModel.sendAction(HomeAction.Action.OnClickManageAccountButton)
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_account),
                    contentDescription = stringResource(
                        id = R.string.account_icon_description
                    ),
                    tint = ColorReference.royalPurple
                )
            }
        }
    }
}

@Composable
private fun HomeSearchBar(uiModel: HomeUiModel, viewModel: HomeViewModel) {
    SearchBar(
        query = uiModel.searchFieldUiState.query,
        onQueryChange = { changedValue ->
            viewModel.sendAction(HomeAction.Action.OnTypeSearchField(changedValue))
        },
        hint = stringResource(id = R.string.search_hint),
        modifier = Modifier.padding(horizontal = Spacing.mediumLarge),
        onSearch = {
            viewModel.sendAction(HomeAction.Action.OnSearch)
        },
    )
}

@Composable
private fun RestaurantsTitle() {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.padding(Spacing.medium))
        Text(
            text = stringResource(id = R.string.restaurant_list_title),
            style = Typography.headerNormal,
            color = ColorReference.quartz,
            modifier = Modifier.padding(
                start = Spacing.mediumLarge
            )
        )
    }
}

@Composable
private fun Restaurants() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_chef),
            contentDescription = stringResource(id = R.string.chef_hat_icon_description)
        )
        Spacer(modifier = Modifier.padding(Spacing.smaller))
        Text(
            text = stringResource(id = R.string.no_restaurants_registered),
            style = Typography.bodyLarge,
            color = ColorReference.taupeGray
        )
    }
}

@Composable
private fun UiEffect(viewModel: HomeViewModel) {
    val localLifecycleOwner = LocalLifecycleOwner.current
    viewModel.uiEffect.observe(localLifecycleOwner) { effect ->
        when (effect) {
            is HomeEffect.OpenManageAccount -> {
                // TODO: Open manage account screen
            }

            is HomeEffect.OpenAddRestaurant -> {
                // TODO: Open add restaurant screen
            }

            is HomeEffect.GetRestaurants -> {
                // TODO: Get restaurants
            }
        }
    }
}
