package com.frn.modernnote.presentation.screens.creation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frn.modernnote.data.TestNoteRepositoryImpl
import com.frn.modernnote.domain.useCases.AddNoteUseCase
import com.frn.modernnote.presentation.screens.creation.CreateNoteState.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateViewModel : ViewModel() {

    val repository = TestNoteRepositoryImpl
    val addNoteUseCase = AddNoteUseCase(repository)

    private val _state = MutableStateFlow<CreateNoteState>(CreateNoteState.Creation())
    val state = _state.asStateFlow()

    fun processCommand(command: CreateNoteCommand) {
        when (command) {
            is CreateNoteCommand.Save -> {
                val title = command.title.trim()
                val content = command.content.trim()
                if (title.isBlank() || content.isBlank()) return

                viewModelScope.launch {
                    _state.update { previousState ->
                        if (previousState is Creation) {
                            previousState.copy(isSaving = true)
                        } else {
                            previousState
                        }
                    }
                    addNoteUseCase(title = title, content = content)
                    _state.update { Finished }
                }
            }

            CreateNoteCommand.Back -> {
                _state.update {
                    Finished
                }
            }
        }
    }

}
