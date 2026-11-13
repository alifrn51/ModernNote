package com.frn.modernnote.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frn.modernnote.data.TestNoteRepositoryImpl
import com.frn.modernnote.domain.useCases.AddNoteUseCase
import com.frn.modernnote.domain.useCases.DeleteNoteUseCase
import com.frn.modernnote.domain.useCases.EditNoteUseCase
import com.frn.modernnote.domain.useCases.GetAllNotesUseCase
import com.frn.modernnote.domain.useCases.GetNoteUseCase
import com.frn.modernnote.domain.useCases.SearchNotesUseCase
import com.frn.modernnote.domain.useCases.SwitchPinnedStatusUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class NoteViewModel : ViewModel() {
    private val noteRepository = TestNoteRepositoryImpl
    val addNoteUseCase = AddNoteUseCase(noteRepository)
    val deleteNoteUseCase = DeleteNoteUseCase(noteRepository)
    val editNoteUseCase = EditNoteUseCase(noteRepository)
    val getNoteUseCase = GetNoteUseCase(noteRepository)
    val getAllNotesUseCase = GetAllNotesUseCase(noteRepository)
    val searchNotesUseCase = SearchNotesUseCase(noteRepository)
    val switchPinnedStatusUseCase = SwitchPinnedStatusUseCase(noteRepository)

    private val _state = MutableStateFlow(NoteScreenState())
    val state = _state.asStateFlow()

    private val query = MutableStateFlow("")
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        addSomeNotes()
        query.onEach { input -> _state.update { it.copy(query = input) } }
            .flatMapLatest { input ->
                if (input.isBlank()) {
                    getAllNotesUseCase()
                } else {
                    searchNotesUseCase(input)
                }
            }.onEach { notes ->
                val pinnedNotes = notes.filter { it.isPinned }
                val otherNotes = notes.filter { !it.isPinned }
                _state.update { screenState ->
                    screenState.copy(pinnedNotes = pinnedNotes, otherNotes = otherNotes)
                }
            }.launchIn(scope)
    }

    private fun addSomeNotes() {
        viewModelScope.launch {
            repeat(50) {
                addNoteUseCase(
                    content = "The text originates from sections $it and 1.10.33 of Cicero's \"De Finibus Bonorum et Malorum\", written in 45 BC, a treatise on ethics Wikipedia Wikipedia +1 .",
                    title = "Lorem Ipsum $it is a standard placeholder text used in design and publishing to simulate real content without distracting from layout or typography."
                )
            }
        }
    }

    fun processCommand(command: NoteScreenCommand) {
        viewModelScope.launch {
            when (command) {
                is NoteScreenCommand.DeleteNote -> {
                    deleteNoteUseCase(command.noteId)
                }

                is NoteScreenCommand.EditNote -> {
                    val note = getNoteUseCase(command.note.id)
                    val title = note.title
                    editNoteUseCase(command.note.copy(title = "$title Edited"))
                }

                is NoteScreenCommand.InputSearchQuery -> {
                    query.update { command.query }
                }

                is NoteScreenCommand.SwitchPinnedStatus -> {
                    switchPinnedStatusUseCase(command.noteId)
                }
            }
        }
    }
}