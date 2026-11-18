@file:OptIn(ExperimentalMaterial3Api::class)

package com.frn.modernnote.presentation.screens.creation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.frn.modernnote.presentation.utils.DateFormatter

@Composable
fun CreationScreen(
    modifier: Modifier = Modifier,
    viewModel: CreationViewModel = viewModel(),
    onFinished: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is CreateNoteState.Creation -> {
            Creation(
                modifier = modifier,
                state = currentState,
                viewModel = viewModel,
            )
        }

        CreateNoteState.Finished -> {
            LaunchedEffect(Unit) {
                onFinished()
            }
        }
    }

}


@Preview
@Composable
private fun CreationScreenPreview() {
    CreationScreen(
        onFinished = {

        }
    )
}

@Composable
private fun Creation(
    modifier: Modifier = Modifier,
    state: CreateNoteState.Creation,
    viewModel: CreationViewModel,
) {
    val titleState = rememberTextFieldState()
    val contentState = rememberTextFieldState()

    val isSaveEnabled = titleState.text.isNotBlank() && contentState.text.isNotBlank() && !state.isSaving

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create Note",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 8.dp)
                            .clip(CircleShape)
                            .clickable {
                                viewModel.processCommand(CreateNoteCommand.Back)
                            },
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back",
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                )
            )
        }
    ) { innerPadding ->

        Content(
            modifier = Modifier.padding(innerPadding),
            titleState = titleState,
            contentState = contentState,
            viewModel = viewModel,
            isSaveEnabled = isSaveEnabled
        )

    }

}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    titleState: TextFieldState = rememberTextFieldState(),
    contentState: TextFieldState = rememberTextFieldState(),
    viewModel: CreationViewModel,
    isSaveEnabled : Boolean
) {
    Column(
        modifier = modifier.fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            state = titleState,
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 14.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
            ),
            textStyle = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            ),
            placeholder = {
                Text(
                    text = "Title",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        )

        Text(
            text = DateFormatter.currentDateTime(),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = contentState,
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 14.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
            ),
            textStyle = TextStyle(
                fontSize = 16.sp,
            ),
            placeholder = {
                Text(
                    text = "Note something down",
                    fontSize = 16.sp,
                )
            }
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            enabled = isSaveEnabled,
            contentPadding = PaddingValues(vertical = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            onClick = {
                viewModel.processCommand(
                    CreateNoteCommand.Save(
                        title = titleState.text.toString(),
                        content = contentState.text.toString(),
                    )
                )
            }
        ) {
            Text(
                text = "Save Notes",
                fontSize = 14.sp,
            )
        }

    }

}
