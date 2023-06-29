import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.mil.chatza.presentation.navigation.Screen

@Composable
fun HomePage(navController: NavHostController) {
    val bottomNavScreens = listOf(Screen.HomeScreen, Screen.SettingsPage, Screen.AboutPage)
    //var currentScreen by remember { mutableStateOf(Screen.HomeScreen) }

    var currentScreen by remember {
        mutableStateOf(Screen.HomeScreen.route)
    }

    Scaffold(
        bottomBar = {
            BottomNavigation {
                bottomNavScreens.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                        label = { Text(screen.route) },
                        selected = currentScreen == screen.route,
                        onClick = {
                            currentScreen = screen.route
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        when (currentScreen) {
            Screen.HomeScreen.route -> HomeScreen()
            Screen.SettingsPage.route -> ProfileScreen()
            Screen.AboutPage.route -> SearchScreen()
        }
    }
}
