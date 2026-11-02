package com.frn.modernnote.presentation.screens

import com.frn.modernnote.domain.Note

data class NoteScreenState(
    val query: String = "",
    val pinnedNotes: List<Note> = emptyList(),
    val otherNotes: List<Note> = emptyList()
)
