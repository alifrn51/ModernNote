@file:OptIn(ExperimentalMaterial3Api::class)

package com.frn.modernnote.presentation.screens.editing

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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.frn.modernnote.presentation.screens.creation.CreateNoteCommand
import com.frn.modernnote.presentation.screens.creation.CreateNoteState
import com.frn.modernnote.presentation.screens.creation.CreateViewModel
import com.frn.modernnote.presentation.utils.DateFormatter

@Composable
fun EditNoteScreen(
    modifier: Modifier = Modifier,
    noteId: Int,
    viewModel: EditNoteViewModel = viewModel{
        EditNoteViewModel(noteId)
    },
    onFinished: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is EditNoteState.Editing -> {
            Editing(
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

        EditNoteState.Finished -> {
            onFinished()
        }
        EditNoteState.Initial -> {}
    }

}


@Preview
@Composable
private fun EditNoteScreenPreview() {
    EditNoteScreen(
        noteId = 5,
        onFinished = {

        }
    )
}

@Composable
private fun Editing(
    modifier: Modifier = Modifier,
    state: EditNoteState.Editing,
    viewModel: EditNoteViewModel,
) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Note",
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
                                viewModel.processCommand(EditNoteCommand.Back)
                            },
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back",
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                actions = {
                    Icon(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 16.dp)
                            .clip(CircleShape)
                            .clickable {
                                viewModel.processCommand(EditNoteCommand.Delete)
                            },
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete",
                    )
                }
            )
        }
    ) { innerPadding ->

        Content(
            modifier = Modifier.padding(innerPadding),
            viewModel = viewModel,
            state = state
        )

    }

}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    viewModel: EditNoteViewModel,
    state: EditNoteState.Editing
) {


    val titleState = rememberTextFieldState(
        state.note.title
    )
    val contentState = rememberTextFieldState(
        state.note.content
    )

    Column(
        modifier = modifier
            .fillMaxSize()
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
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
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
            text = DateFormatter.formatDateToString(state.note.updateAt),
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
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
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
            enabled = state.isSaveEnabled,
            contentPadding = PaddingValues(vertical = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            onClick = {
                viewModel.processCommand(
                    EditNoteCommand.Save(
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
