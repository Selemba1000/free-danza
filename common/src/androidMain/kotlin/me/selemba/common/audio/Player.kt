package me.selemba.common.audio

import kotlinx.coroutines.*
import java.util.concurrent.*

actual class NativePlayer actual constructor(private val coroutineScope: CoroutineScope) {
    actual suspend fun start(): Future<Boolean> {
        TODO("Not yet implemented")
    }

    actual suspend fun stop(): Future<Boolean> {
        TODO("Not yet implemented")
    }

    actual suspend fun load(file: String): Future<PlayerState> {
        TODO("Not yet implemented")
    }
}