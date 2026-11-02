package com.frn.modernnote.domain.useCases

import com.frn.modernnote.domain.NoteRepository

class DeleteNoteUseCase(
    val repository: NoteRepository
) {

    operator fun invoke(id: Int){
        repository.deleteNote(id)
    }
}