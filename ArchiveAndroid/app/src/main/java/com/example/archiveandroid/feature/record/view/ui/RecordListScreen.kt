package com.example.archiveandroid.feature.record.view.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Scaffold

@Composable
fun RecordListScreen() {
    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text("RecordListScreen")
        }
    }
}