package com.example.socketsslclient.core

import androidx.compose.runtime.MutableState
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.network.tls.tls
import io.ktor.utils.io.readUTF8Line
import io.ktor.utils.io.writeStringUtf8
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope.coroutineContext
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

@OptIn(DelicateCoroutinesApi::class)
suspend fun sendMessage(
    message: String,
    ipAddress: String,
    port: Int,
    textReceive: MutableState<String?>
) {
    try {
        val selectorManager = SelectorManager(Dispatchers.IO)

        val socket = aSocket(selectorManager).tcp().connect(ipAddress, port).tls(coroutineContext = coroutineContext) {
            trustManager = object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOf()
                override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
                override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
            }
        }
        println("Now Connecting to ${socket.remoteAddress}")

        val receiveChannel = socket.openReadChannel()

        val sendChannel = socket.openWriteChannel(autoFlush = true)
        sendChannel.writeStringUtf8("$message\n")

        val messageBack: String? = receiveChannel.readUTF8Line()
        textReceive.value = messageBack

        println(textReceive.value)
    } catch (e: Exception) {
        println("Failed to send message: ${e.message}")
    }
}