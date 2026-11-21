package com.frn.modernnote.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.frn.modernnote.presentation.navigation.NavGraph
import com.frn.modernnote.presentation.screens.editing.EditNoteScreen
import com.frn.modernnote.presentation.ui.theme.ModernNoteTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ModernNoteTheme {
                NavGraph()
            }
        }
    }
}
