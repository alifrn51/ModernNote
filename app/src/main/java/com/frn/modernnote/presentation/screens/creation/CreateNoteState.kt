package com.frn.modernnote.presentation.screens.creation

sealed interface CreateNoteState {
    data class Creation(
        val isSaving: Boolean = false,
    ) : CreateNoteState

    data object Finished : CreateNoteState
}
