package com.frn.modernnote.domain.useCases

import com.frn.modernnote.domain.Note
import com.frn.modernnote.domain.NoteRepository

class AddNoteUseCase(
    val repository: NoteRepository
) {

    operator fun invoke(title: String,content: String){
        repository.addNote(title = title, content = content)
    }
}