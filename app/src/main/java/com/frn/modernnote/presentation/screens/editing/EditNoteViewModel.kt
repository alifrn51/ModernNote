package com.frn.modernnote.presentation.screens.editing

import androidx.compose.runtime.currentRecomposeScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frn.modernnote.data.TestNoteRepositoryImpl
import com.frn.modernnote.domain.useCases.DeleteNoteUseCase
import com.frn.modernnote.domain.useCases.EditNoteUseCase
import com.frn.modernnote.domain.useCases.GetNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditNoteViewModel(val noteId: Int) : ViewModel() {
    private val repository = TestNoteRepositoryImpl
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val getNoteUseCase = GetNoteUseCase(repository)
    private val deleteNoteUseCase = DeleteNoteUseCase(repository)

    private val _state = MutableStateFlow<EditNoteState>(EditNoteState.Initial)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                val note = getNoteUseCase(noteId)
                EditNoteState.Editing(note)
            }
        }
    }
    fun processCommand(command: EditNoteCommand) {
        when (command) {

            is EditNoteCommand.Save -> {
                val title = command.title.trim()
                val content = command.content.trim()

                if (title.isBlank() || content.isBlank()) return

                viewModelScope.launch {
                    val currentState = _state.value

                    if(currentState !is EditNoteState.Editing) return@launch

                    val updatedNote = currentState.note.copy(title = title, content = content)

                    editNoteUseCase(updatedNote)

                    _state.value = EditNoteState.Finished

                }

            }

            EditNoteCommand.Back -> {
                _state.update { EditNoteState.Finished }
            }

            EditNoteCommand.Delete -> {
                viewModelScope.launch {
                    val currentState = _state.value

                    if(currentState !is EditNoteState.Editing)return@launch
                    deleteNoteUseCase(currentState.note.id)
                    _state.value = EditNoteState.Finished
                }

            }
        }
    }

}