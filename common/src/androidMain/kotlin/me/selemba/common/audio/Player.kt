package me.selemba.common.audio

import kotlinx.coroutines.*

actual class Player actual constructor(actual val coroutineScope: CoroutineScope) {
    actual suspend fun load(file: String) {
    }

    actual suspend fun start() {
    }

    actual suspend fun stop() {
    }

    actual fun getMixers(): List<String> {
        TODO("Not yet implemented")
    }

    actual fun setMixer(mixer: Int) {
    }

}