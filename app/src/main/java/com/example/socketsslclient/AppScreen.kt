package com.example.socketsslclient

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.socketsslclient.core.sendMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AppScreen() {

    var inputIpAddress by remember { mutableStateOf("192.168.101.149") }
    var inputPort by remember { mutableStateOf("4001") }
    var inputMessage by remember { mutableStateOf("") }
    var clientStarted by remember { mutableStateOf(true) }
    val textReceive: MutableState<String?> = remember { mutableStateOf("") }

    LaunchedEffect(clientStarted) {
        launch(Dispatchers.IO) {
            println(clientStarted)

            if (clientStarted) {
                sendMessage(
                    message = inputMessage,
                    ipAddress = inputIpAddress,
                    port = inputPort.toInt(),
                    textReceive = textReceive
                )
                clientStarted = !clientStarted
            }

        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color.White)
            .border(5.dp, Color.Black)
    ) {
        Text(
            text = textReceive.value ?: "-",
            style = TextStyle(Color.Black),
            modifier = Modifier.padding(10.dp)
        )

    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AddSpace()
        CreateTextField(
            value = inputIpAddress,
            onTextChange = { inputIpAddress = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = "Client IP"
        )

        Spacer(modifier = Modifier.padding(10.dp))
        CreateTextField(
            value = inputPort,
            onTextChange = { inputPort = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = "Client Port"
        )

        AddSpace()
        CreateTextField(
            value = inputMessage,
            onTextChange = { inputMessage = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            placeholder = "Message"
        )

        Spacer(modifier = Modifier.padding(10.dp))
        CreateButton(btnText = "Send Message", onClickAction = {
            clientStarted = !clientStarted
        })
    }
}

@Composable
fun CreateTextField(
    value: String,
    onTextChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    placeholder: String
) {
    TextField(value = value,
        onValueChange = onTextChange,
        keyboardOptions = keyboardOptions,
        placeholder = { Text(text = placeholder) })
}

@Composable
fun CreateButton(btnText: String, onClickAction: () -> Unit) {
    Button(onClick = onClickAction) {
        Text(text = btnText)
    }
}


@Composable
fun AddSpace() {
    Spacer(modifier = Modifier.padding(20.dp))
}