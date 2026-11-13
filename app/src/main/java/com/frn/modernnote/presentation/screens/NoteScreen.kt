package com.frn.modernnote.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.frn.modernnote.domain.Note
import com.frn.modernnote.presentation.ui.theme.Green100
import com.frn.modernnote.presentation.ui.theme.Green200
import com.frn.modernnote.presentation.ui.theme.Yellow100
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun NoteScreen(
    modifier: Modifier = Modifier, viewModel: NoteViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Title(text = "All Notes")
        }
        item {
            SearchBar(
                query = state.query, onQueryChange = {
                    viewModel.processCommand(NoteScreenCommand.InputSearchQuery(it))
                })
        }
        item {
            Subtitle(text = "Pinned")

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = state.pinnedNotes, key = { it.id }) { note ->
                    NoteCard(
                        note = note,
                        background = Yellow100,
                        onNoteClick = {
                            viewModel.processCommand(NoteScreenCommand.EditNote(it))
                        },
                        onLongClick = {
                            viewModel.processCommand(NoteScreenCommand.SwitchPinnedStatus(it.id))
                        },
                        onDoubleClick = {
                            viewModel.processCommand(NoteScreenCommand.DeleteNote(it.id))
                        }
                    )
                }
            }
        }
        item {
            Subtitle(text = "Others")
        }
        items(
            items = state.otherNotes, key = { it.id }) { note ->
            NoteCard(
                modifier = Modifier.fillMaxWidth(),
                note = note,
                background = Green100,
                onNoteClick = {
                    viewModel.processCommand(NoteScreenCommand.EditNote(it))
                },
                onLongClick = {
                    viewModel.processCommand(NoteScreenCommand.SwitchPinnedStatus(it.id))
                },
                onDoubleClick = {
                    viewModel.processCommand(NoteScreenCommand.DeleteNote(it.id))
                }
            )
        }


    }

}

@Composable
private fun Title(
    modifier: Modifier = Modifier, text: String
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier, query: String, onQueryChange: (String) -> Unit
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = query,
        onValueChange = onQueryChange,
        shape = RoundedCornerShape(16.dp),
        placeholder = {
            Text(
                text = "Search...",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search note",
                tint = MaterialTheme.colorScheme.surfaceVariant
            )
        })
}

@Composable
fun Subtitle(
    modifier: Modifier = Modifier, text: String
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    note: Note,
    background: Color,
    onNoteClick: (Note) -> Unit,
    onLongClick: (Note) -> Unit,
    onDoubleClick: (Note) -> Unit,
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16))
            .background(background)
            .combinedClickable(onClick = {
                onNoteClick(note)
            }, onLongClick = {
                onLongClick(note)
            }, onDoubleClick = {
                onDoubleClick(note)
            })
    ) {
        Text(
            text = note.title,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = note.updateAt.toString(),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = note.content,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}