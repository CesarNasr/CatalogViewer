package com.cesar.catalogviewer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.cesar.catalogviewer.presentation.detail.CatalogDetailRoute
import com.cesar.catalogviewer.presentation.list.CatalogListRoute
import kotlinx.serialization.Serializable

@Serializable
data object CatalogListDestination

@Serializable
data class CatalogDetailDestination(
    val itemId: String
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CatalogListDestination
    ) {
        composable<CatalogListDestination> {
            CatalogListRoute(
                onItemClick = { itemId ->
                    navController.navigate(
                        CatalogDetailDestination(itemId = itemId)
                    )
                }
            )
        }

        composable<CatalogDetailDestination> { backStackEntry ->
            val destination = backStackEntry.toRoute<CatalogDetailDestination>()

            CatalogDetailRoute(
                itemId = destination.itemId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
