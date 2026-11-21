package com.frn.modernnote.presentation.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.frn.modernnote.presentation.screens.creation.CreateNoteScreen
import com.frn.modernnote.presentation.screens.editing.EditNoteScreen
import com.frn.modernnote.presentation.screens.editing.EditNoteState
import com.frn.modernnote.presentation.screens.notes.NoteScreen


@Composable
fun NavGraph(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Notes.route
    ){
        composable(route = Screen.Notes.route) {
            NoteScreen(
                onNoteClick = {note->
                    navController.navigate(Screen.EditNote.createRoute(note.id))
                },
                onAddNoteClick = {
                    navController.navigate(Screen.CreateNote.route)
                }
            )
        }
        composable(route = Screen.CreateNote.route) {
            CreateNoteScreen(
                onFinished = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Screen.EditNote.route) {
            val noteId = Screen.EditNote.getNoteId(it.arguments)
            EditNoteScreen (
                noteId = noteId,
                onFinished = {
                    navController.popBackStack()
                }
            )
        }
    }
}


sealed class Screen(val route: String){
    data object Notes: Screen(route = "notes")
    data object CreateNote: Screen(route = "create_note")
    data object EditNote: Screen(route = "edit_note/{note_Id}"){
        fun createRoute(noteId: Int): String {
            return "edit_note/$noteId"
        }

        fun getNoteId(arguments: Bundle?): Int{
            return arguments?.getString("note_Id")?.toInt() ?: 0
        }
    }
}