package com.frn.modernnote.presentation.screens.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.frn.modernnote.R
import com.frn.modernnote.domain.Note
import com.frn.modernnote.presentation.ui.theme.ModernNoteTheme
import com.frn.modernnote.presentation.ui.theme.OtherNotesLightColors
import com.frn.modernnote.presentation.ui.theme.PinnedNotesLightColors
import com.frn.modernnote.presentation.utils.toFormatDate
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun NoteScreen(
    modifier: Modifier = Modifier,
    viewModel: NoteViewModel = viewModel(),
    onNoteClick: (Note) -> Unit,
    onAddNoteClick: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAddNoteClick()
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add_note),
                    contentDescription = "Button add note"
                )
            }
        }
    ) { innerPadding ->


        if (state.otherNotes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(top = 150.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Note list is empty"
                )
            }

        }

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding
        ) {
            item {
                Spacer(modifier = Modifier.height(48.dp))
            }
            item {
                Title(text = "All Notes", modifier = Modifier.padding(horizontal = 16.dp))
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                SearchBar(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    query = state.query, onQueryChange = {
                        viewModel.processCommand(NoteScreenCommand.InputSearchQuery(it))
                    })
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            if (state.pinnedNotes.isNotEmpty()) {
                item {
                    Subtitle(text = "Pinned", modifier = Modifier.padding(horizontal = 16.dp))
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        itemsIndexed(
                            items = state.pinnedNotes,
                            key = { _, note -> note.id }) { index, note ->
                            NoteCard(
                                modifier = Modifier.widthIn(max = 160.dp),
                                note = note,
                                background = PinnedNotesLightColors[index % PinnedNotesLightColors.size],
                                onNoteClick = onNoteClick,
                                onLongClick = {
                                    viewModel.processCommand(NoteScreenCommand.SwitchPinnedStatus(it.id))
                                }
                            )
                        }
                    }
                }
            }


            if (state.otherNotes.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
                item {
                    Subtitle(text = "Others", modifier = Modifier.padding(horizontal = 16.dp))
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                itemsIndexed(
                    items = state.otherNotes, key = { _, note -> note.id }) { index, note ->
                    NoteCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        note = note,
                        background = OtherNotesLightColors[index % OtherNotesLightColors.size],
                        onNoteClick = onNoteClick,
                        onLongClick = {
                            viewModel.processCommand(NoteScreenCommand.SwitchPinnedStatus(it.id))
                        },
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Preview(showBackground = true)
@Composable
private fun NoteScreenPreview() {
    ModernNoteTheme {
        NoteScreen(
            onNoteClick = {},
            onAddNoteClick = {}
        )
    }
}

@Composable
private fun Title(
    text: String,
    modifier: Modifier = Modifier
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
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            ),
        value = query,
        onValueChange = onQueryChange,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
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
    onLongClick: (Note) -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16))
            .background(background)
            .combinedClickable(
                onClick = {
                    onNoteClick(note)
                }, onLongClick = {
                    onLongClick(note)
                }
            )
            .padding(16.dp)
    ) {
        Text(
            text = note.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = note.updateAt.toFormatDate(),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = note.content,
            fontSize = 16.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}