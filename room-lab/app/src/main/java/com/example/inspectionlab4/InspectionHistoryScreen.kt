package com.example.inspectionlab4

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date

@Composable
fun rememberRecordInspection(dao: InspectionRecordDao?): ((InspectionTask) -> Unit)? {
    if (dao == null) return null
    val context = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()
    var message by remember { mutableStateOf<String?>(null) }
    message?.let { text ->
        AlertDialog(
            onDismissRequest = { message = null },
            text = { Text(text) },
            confirmButton = { TextButton(onClick = { message = null }) { Text("确定") } },
        )
    }
    return { task ->
        scope.launch {
            try {
                val name = InspectorNameStore.observe(context).first()
                dao.insert(InspectionRecord(
                    taskId = task.id,
                    deviceName = task.deviceName,
                    location = task.location,
                    inspectorName = name,
                ))
                message = "已保存巡检记录"
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                message = "保存失败，请重试"
            }
        }
    }
}

@Composable
fun InspectionHistoryScreen(dao: InspectionRecordDao, onBack: () -> Unit) {
    val records by remember(dao) { dao.observeAll() }.collectAsState(initial = null)
    val recordTask = rememberRecordInspection(dao)
    val demoTask = InspectionTask("A203-DEMO", "投影仪", "A203 实验室", "待巡检")

    Surface(Modifier.fillMaxSize()) {
        Column(
            Modifier.safeDrawingPadding().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            TextButton(onClick = onBack) { Text("返回首页") }
            Text("本机巡检历史", style = MaterialTheme.typography.headlineMedium)
            Text("离线演示任务 · ${demoTask.deviceName} · ${demoTask.location}")
            Button(onClick = { recordTask?.invoke(demoTask) }, modifier = Modifier.testTag("recordDemo")) {
                Text("记录已巡检")
            }

            val current = records
            when {
                current == null -> Text("正在读取历史…")
                current.isEmpty() -> Text("暂无巡检记录", Modifier.testTag("emptyHistory"))
                else -> {
                    Text("共 ${current.size} 条记录", Modifier.testTag("historyCount"))
                    LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(current, key = { it.id }) { record ->
                            Card(Modifier.fillMaxWidth()) {
                                Column(Modifier.padding(12.dp)) {
                                    Text("#${record.id}  ${record.deviceName}", style = MaterialTheme.typography.titleMedium)
                                    Text("${record.taskId} · ${record.location} · ${record.inspectorName}")
                                    Text(DateFormat.getDateTimeInstance().format(Date(record.recordedAt)))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

