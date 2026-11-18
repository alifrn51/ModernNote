package com.frn.modernnote.presentation.screens.editing

import com.frn.modernnote.domain.Note

sealed interface EditNoteState {

    data object Initial : EditNoteState
    data class Editing(
        val note: Note
    ) : EditNoteState {
        val isSaveEnabled: Boolean
            get() = note.title.isNotBlank() && note.content.isNotBlank()
    }

    data object Finished : EditNoteState
}