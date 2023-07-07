import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.mil.chatza.presentation.navigation.Screen
import com.mil.chatza.presentation.screens.homeScreens.HomeScreen
import com.mil.chatza.presentation.screens.homeScreens.ProfileScreen
import com.mil.chatza.presentation.viewmodels.AuthViewModel
import com.mil.chatza.presentation.viewmodels.FirebaseViewModel
import com.mil.chatza.ui.theme.chatZaBlue
import com.mil.chatza.ui.theme.chatZaBrown

@Composable
fun HomePage(navController: NavHostController, authVM : AuthViewModel, firebaseVM :FirebaseViewModel) {


    val bottomNavScreens = listOf(Screen.HomeScreen, Screen.SettingsScreen, Screen.ProfileScreen)
    val currentContext = LocalContext.current
    var currentScreen by remember {
        mutableStateOf(Screen.HomeScreen.route)
    }

    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = chatZaBrown
            ) {
                bottomNavScreens.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            when(screen){
                                Screen.HomeScreen -> Icon(Icons.Default.Home, contentDescription = null, tint = chatZaBlue)
                                Screen.SettingsScreen -> Icon(Icons.Default.Search, contentDescription = null, tint = chatZaBlue)
                                Screen.ProfileScreen -> Icon(Icons.Default.Person, contentDescription = null, tint = chatZaBlue)
                                else -> {}
                            }
                        },
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
        Log.i("HomePage", innerPadding.toString())
        BackHandler(enabled = true) {
            if (currentContext is Activity){
                currentContext.moveTaskToBack(true)
            }
        }
        when (currentScreen) {
            Screen.HomeScreen.route -> HomeScreen(navController = navController, authVM = authVM)
            Screen.SettingsScreen.route -> SearchScreen()
            Screen.ProfileScreen.route -> ProfileScreen(authVM, firebaseVM)
        }
    }
}
