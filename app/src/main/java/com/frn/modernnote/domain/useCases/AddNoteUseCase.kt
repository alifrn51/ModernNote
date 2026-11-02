package com.frn.modernnote.domain.useCases

import com.frn.modernnote.domain.Note
import com.frn.modernnote.domain.NoteRepository

class AddNoteUseCase(
    val repository: NoteRepository
) {

    operator fun invoke(note: Note){
        repository.addNote(note)
    }
}