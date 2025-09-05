package com.example.archiveandroid.feature.record.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.archiveandroid.feature.record.view.ui.RecordListScreen

class RecordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecordListScreen()
        }
    }
}
