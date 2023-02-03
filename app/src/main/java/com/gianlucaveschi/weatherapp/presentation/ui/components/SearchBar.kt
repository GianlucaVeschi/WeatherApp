package com.gianlucaveschi.weatherapp.presentation.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.collect

@Composable
fun SearchBar(
    text: String,
    onCloseIconClicked: () -> Unit,
    onTextChanged: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
) {
    var startAnim by remember {
        mutableStateOf(false)
    }

    val radiusAnim = remember {
        Animatable(initialValue = 28f)
    }

    val anim by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
    )

    LaunchedEffect(key1 = true) {
        startAnim = true
        radiusAnim.animateTo(0f, animationSpec = tween(durationMillis = 1600))
    }

    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        TextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .clip(RoundedCornerShape(
                    topEnd = 0.dp,
                    bottomEnd = 0.dp,
                    topStart = radiusAnim.value.dp,
                    bottomStart = radiusAnim.value.dp
                ))
                .background(color = Color.White)
                .fillMaxWidth(anim)
                .height(56.dp),
            value = text,
            singleLine = true,
            placeholder = {
                Text(
                    text = "Search...",
                    color = Color.Black.copy(alpha = ContentAlpha.medium)
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.Black,
                backgroundColor = Color.Transparent,
                cursorColor = MaterialTheme.colors.primary,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            onValueChange = onTextChanged,
            trailingIcon = {
                IconButton(onClick = {
                    if (text.isNotBlank()) {
                        onTextChanged("")
                    } else {
                        onCloseIconClicked()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "",
                        tint = Color.Black
                    )
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "",
                    tint = Color.Black.copy(alpha = ContentAlpha.medium)
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {
                onSearchClicked(text)
            })
        )
    }

    DisposableEffect(Unit) {
        focusRequester.requestFocus()
        onDispose { }
    }
}