package com.gianlucaveschi.weatherapp.presentation.ui.components

import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.gianlucaveschi.weatherapp.R
import com.gianlucaveschi.weatherapp.presentation.MainViewModel

@Composable
fun TopBar(viewModel: MainViewModel) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = stringResource(id = R.string.app_name),
                fontSize = 20.sp
            )
            IconButton(
                onClick = {
                    viewModel.onEvent(SearchState.Opened)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = ""
                )
            }
        }
    }
}