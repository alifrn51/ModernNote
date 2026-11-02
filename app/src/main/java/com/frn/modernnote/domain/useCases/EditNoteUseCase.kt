package com.frn.modernnote.domain.useCases

import com.frn.modernnote.domain.Note
import com.frn.modernnote.domain.NoteRepository

class EditNoteUseCase(
    val repository: NoteRepository
) {

    operator fun invoke(note: Note){
        repository.editNote(note)
    }
}