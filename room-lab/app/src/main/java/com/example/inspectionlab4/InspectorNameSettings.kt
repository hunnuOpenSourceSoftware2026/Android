package com.example.inspectionlab4

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun InspectorNameSettings() {
    val context = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()
    val savedName by remember(context) {
        InspectorNameStore.observe(context)
    }.collectAsState(initial = "读取中…")
    var inputName by rememberSaveable { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("巡检员姓名：$savedName")
        OutlinedTextField(
            value = inputName,
            onValueChange = { inputName = it },
            label = { Text("输入巡检员姓名") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            enabled = inputName.isNotBlank(),
            onClick = {
                scope.launch { InspectorNameStore.save(context, inputName) }
            }
        ) { Text("保存姓名") }
    }
}
