package com.frn.modernnote.presentation.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frn.modernnote.data.TestNoteRepositoryImpl
import com.frn.modernnote.domain.useCases.GetAllNotesUseCase
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
    val getAllNotesUseCase = GetAllNotesUseCase(noteRepository)
    val searchNotesUseCase = SearchNotesUseCase(noteRepository)
    val switchPinnedStatusUseCase = SwitchPinnedStatusUseCase(noteRepository)

    private val _state = MutableStateFlow(NoteScreenState())
    val state = _state.asStateFlow()

    private val query = MutableStateFlow("")
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
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

    fun processCommand(command: NoteScreenCommand) {
        viewModelScope.launch {
            when (command) {
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