package com.frn.modernnote.presentation.screens.creation

sealed interface CreateNoteCommand {
    data class Save(val title: String, val content: String) : CreateNoteCommand
    data object Back : CreateNoteCommand
}
