package com.gianlucaveschi.weatherapp.presentation

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.gianlucaveschi.weatherapp.R
import com.gianlucaveschi.weatherapp.presentation.ui.components.SearchBar
import com.gianlucaveschi.weatherapp.presentation.ui.components.SearchState
import com.gianlucaveschi.weatherapp.presentation.ui.components.TopBar
import com.gianlucaveschi.weatherapp.presentation.ui.theme.WeatherAppTheme
import com.gianlucaveschi.weatherapp.presentation.ui.weather.WeatherAppMainLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            viewModel.loadLocalWeatherInfo()
        }
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
        setContent {
            WeatherAppTheme {
                WeatherAppTheme {
                    val searchBarStatus by viewModel.searchBarState
                    val searchText by viewModel.searchBarText
                    val state by viewModel.state.collectAsState()
                    Scaffold(
                        topBar = {
                            TopBar(viewModel)
                            if (searchBarStatus == SearchState.Opened) {
                                SearchBar(
                                    text = searchText,
                                    onCloseIconClicked = {
                                        if (searchText.isNotBlank()) {
                                            viewModel.onTextChanged("")
                                        } else {
                                            viewModel.onTextChanged("")
                                            viewModel.onEvent(SearchState.Closed)
                                        }
                                    },
                                    onSearchClicked = { query ->
                                        viewModel.onSearchClicked(query)
                                        hideSoftKeyboard()
                                    },
                                    onTextChanged = {
                                        viewModel.onTextChanged(it)
                                    })
                            }
                        },
                        backgroundColor = colorResource(id = R.color.black)
                    ) { padding ->
                        Box(modifier = Modifier.padding(padding)) {
                            WeatherAppMainLayout(state)
                        }
                    }
                }
            }
        }
    }

    private fun hideSoftKeyboard() {
        // Only runs if there is a view that is currently focused
        this.currentFocus?.let { view ->
            val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}

