import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.mil.chatza.core.utils.Consts
import com.mil.chatza.domain.model.Chat
import com.mil.chatza.domain.model.UserProfile
import com.mil.chatza.presentation.components.ProgressBar
import com.mil.chatza.presentation.components.ProvinceChatCard
import com.mil.chatza.presentation.components.UserChatCard
import com.mil.chatza.presentation.navigation.Screen
import com.mil.chatza.presentation.viewmodels.AuthViewModel
import com.mil.chatza.presentation.viewmodels.ChatZaViewModel
import com.mil.chatza.presentation.viewmodels.FirebaseViewModel
import com.mil.chatza.ui.theme.chatZaBrown
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(
    navController: NavHostController,
    firebaseVM: FirebaseViewModel,
    authVM: AuthViewModel,
    chatZaVM: ChatZaViewModel
) {

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Chats")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = chatZaBrown
                )
            )
        },
    ) { paddingValues ->
        print(paddingValues)
        ChatsScreenContent(
            navController = navController,
            firebaseVM = firebaseVM,
            authVM = authVM,
            chatZaVM = chatZaVM
        )
    }

}

@Composable
private fun ChatsScreenContent(
    navController: NavHostController,
    firebaseVM: FirebaseViewModel,
    authVM: AuthViewModel,
    chatZaVM: ChatZaViewModel
) {

    suspend fun getUserDetails(): UserProfile {
        return firebaseVM.getProfileDetails(authVM.auth.currentUser!!.email.toString())
    }

    val scope = rememberCoroutineScope()

    var progressBarState by remember { mutableStateOf(true) }
    var chatList by remember { mutableStateOf(listOf<Chat>()) }
    var myDetails by remember { mutableStateOf(UserProfile()) }
    LaunchedEffect(Unit) {
        myDetails = firebaseVM.currentProfileDetails.value!!
        chatList = firebaseVM.getAllChats()
        progressBarState = false
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            chatList.forEach { chat ->
                progressBarState = true
                if (!Consts.provinceList.contains(chat.chatName) && chat.participants.any { it.email == firebaseVM.currentProfileDetails.value!!.email }) {
                    val chatNameWithoutName = chat.chatName.replace(myDetails.name, "")
                    UserChatCard(name = chatNameWithoutName, email = "test@test.com", onClick = {
                        chatZaVM.setCurrentChat(chatNameWithoutName)
                        navController.navigate(Screen.ChatDetailsScreen.route)
                    })
                }
                progressBarState = false
            }

        }
        when (progressBarState) {
            true -> ProgressBar()
            else -> {  }
        }
    }
}

@Composable
@Preview
private fun PreviewChatScreen() {
    //ChatsScreen(rememberNavController())
}
