package com.frn.modernnote.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.frn.modernnote.domain.Note
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun NoteScreen(
    modifier: Modifier = Modifier,
    viewModel: NoteViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = state.pinnedNotes,
                    key = { it.id }
                ) { note ->
                    NotesCard(
                        note = note,
                        onNoteClick = {
                            viewModel.processCommand(NoteScreenCommand.SwitchPinnedStatus(it.id))
                        }
                    )
                }
            }
        }
        items(
            items = state.otherNotes,
            key = { it.id }
        ) { note ->
            NotesCard(
                note = note,
                onNoteClick = {
                    viewModel.processCommand(NoteScreenCommand.SwitchPinnedStatus(it.id))
                }
            )
        }


    }

}

@Composable
fun NotesCard(
    modifier: Modifier = Modifier,
    note: Note,
    onNoteClick: (Note) -> Unit
) {

    Text(
        modifier = Modifier.clickable {
            onNoteClick(note)
        },
        text = "${note.title} - ${note.content}"
    )

}