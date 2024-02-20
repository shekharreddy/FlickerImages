package com.nsr.flickerimages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.nsr.flickerimages.network.Status
import com.nsr.flickerimages.ui.theme.FlickerImagesTheme
import com.nsr.flickerimages.utils.annotatedString
import com.nsr.flickerimages.viewmodel.FlickerUIItem
import com.nsr.flickerimages.viewmodel.FlickerImagesViewModel
import dagger.hilt.android.AndroidEntryPoint

// Activity to show search flicker for images, and details of an item.

@AndroidEntryPoint
class FlickerImagesActivity : ComponentActivity() {
    private val viewModel: FlickerImagesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlickerImagesTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "SearchHomeScreen"
                ) {
                    composable("SearchHomeScreen") {
                        SearchHomeScreen(navController)
                    }
                    composable("SearchItemDetails") {
                        SearchItemDetails()
                    }
                }
            }
        }
    }


    @Composable
    fun SearchHomeScreen(navController: NavHostController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            SimpleSearchScreen(navController)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Search screen to show search Bar with Search results
    @Composable
    fun SimpleSearchScreen(navController: NavHostController) {
        var searchQuery by remember { mutableStateOf("") }

        Column(modifier = Modifier.fillMaxSize()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                shape = MaterialTheme.shapes.large,
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)) {
                    OutlinedTextField(
                        shape = RoundedCornerShape(8.dp),
                        value = searchQuery,
                        colors = TextFieldDefaults.colors(
                            unfocusedTextColor = Color.Gray,
                            focusedTextColor = Color.Black,
                            unfocusedContainerColor = Color.White
                        ),
                        onValueChange = {
                            searchQuery = it
                           if(it.isNotBlank()) {
                               viewModel.fetchFlickerImages(it)
                           }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        placeholder = { Text(getString(R.string.search)) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    )
                }
            }
            HandleSearchResponse(viewModel = viewModel, navController)
        }
    }

    // Details screen to show selected search item details.
    @Composable
    fun SearchItemDetails() {
        val currentSelectedItem = viewModel.getCurrentSelectedItem()
        currentSelectedItem?.let {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),

                ) {
                Image(
                    painter = rememberAsyncImagePainter(it.imageURL),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .aspectRatio(1.333f)
                        .clip(RoundedCornerShape(5.dp))

                )

                Text(
                    text = annotatedString(
                        stringResource(id = R.string.title),
                        it.title.orEmpty()
                    )
                )
                // TODO format Description string to readable string
                Text(
                    text = annotatedString(
                        stringResource(id = R.string.description),
                        it.description.orEmpty()
                    )
                )
                Text(
                    text = annotatedString(
                        stringResource(id = R.string.author),
                        it.author.orEmpty()
                    )
                )

                Text(
                    text = annotatedString(
                        stringResource(id = R.string.published_date),
                        it.publishedDate.orEmpty()
                    )
                )
            }
        }
    }

    @Composable
    private fun HandleSearchResponse(
        viewModel: FlickerImagesViewModel,
        navController: NavHostController
    ) {
        val listDataState by rememberUpdatedState(newValue = viewModel.flickerImagesList.collectAsState())

        when(listDataState.value.status) {
            Status.INIT -> {
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.search_info),
                    modifier = Modifier.padding(top = 32.dp)
                )
            }
            Status.SUCCESS -> {
                listDataState.value.data?.let { UpdateUIWithFlickerImages(it, navController) }
            }
            Status.LOADING -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }
            Status.ERROR ->{
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(stringResource(R.string.error_message))
                    }
                }
            }
        }
    }

    @Composable
    fun UpdateUIWithFlickerImages(flickerItems: List<FlickerUIItem>, navController: NavHostController){
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // TODO adjust the count for mobile and tablet devices by checking tablet or mobile device
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),

            content = {
                items(flickerItems.size) { index ->
                    Image(
                        painter = rememberAsyncImagePainter(flickerItems[index].imageURL),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clickable {
                                viewModel.setCurrentSelectedItem(flickerItems[index])
                                navController.navigate("SearchItemDetails")
                            }
                            .aspectRatio(1.333f)
                            .clip(RoundedCornerShape(4.dp))
                    )
                }
            }
        )
    }
}