import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mil.chatza.ui.theme.ChatZATheme
import kotlinx.coroutines.launch


private const val TAG = "HomeScreen"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Drawer Sample")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {

                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        },
                    ) {
                        Icon(
                            Icons.Rounded.Menu,
                            contentDescription = ""
                        )
                    }
                })
        },

        drawerContent = { DrawerView() },

        bottomBar = {}//add bottom navigation content

    ) {
        paddingValues -> Log.i(TAG, paddingValues.toString())
    }
}


@Composable
fun DrawerView() {
    val language = listOf("English ", "Hindi", "Arabic")
    val category = listOf("Cloth", "electronics", "fashion", "Food")
    LazyColumn {
        item {
            AddDrawerHeader(title = "Language")
        }
        items(language.size) { index ->

            AddDrawerContentView(
                title = language[index],
                selected = index == 1
            )
        }
        item {
            AddDrawerHeader(title = "Category")
        }

        items(category.size) { index ->

            AddDrawerContentView(
                title = category[index],
                selected = index == 2
            )
        }
    }

}

@Composable
fun AddDrawerContentView(title: String, selected: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {}
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {

        if (title.isNotEmpty()) {
            if (selected)
                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    color = Color.Black,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                )
            else
                Text(text = title, modifier = Modifier.weight(1f), fontSize = 12.sp)
        }
    }
}

@Composable
fun AddDrawerHeader(
    title: String,
    titleColor: Color = Color.Black,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        border = BorderStroke(1.dp, color = Color.Gray),

        ) {
        Text(
            text = title,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = titleColor
            ),
            modifier = Modifier.padding(14.dp)
        )

    }
}