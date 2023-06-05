package me.selemba.common.audio

import kotlinx.coroutines.*

expect class Player(coroutineScope: CoroutineScope) {

    suspend fun loadFile(file: String)
    suspend fun addFile(file: String)

    suspend fun start()

    suspend fun stop()

    suspend fun getMixers():List<String>

    suspend fun setMixer(mixer: Int)

    val coroutineScope: CoroutineScope

}