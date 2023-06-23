package me.selemba.common.audio

import kotlinx.coroutines.*
import me.selemba.common.ui.elements.player.PlayerModel

actual class Player actual constructor(actual val coroutineScope: CoroutineScope) {
    actual suspend fun loadFile(file: AudioFile) {
    }

    actual suspend fun addFile(file: AudioFile) {
    }

    actual suspend fun clearFiles() {
    }

    actual fun listFiles(): List<AudioFile> {
        TODO("Not yet implemented")
    }

    actual suspend fun start() {
    }

    actual suspend fun stop() {
    }

    actual suspend fun next() {
    }

    actual suspend fun seek(positionMilis: Int) {
    }

    actual suspend fun fadeOut(lengthMilis: Int) {
    }

    actual suspend fun fadeIn(lengthMilis: Int) {
    }

    actual fun getMixers(): Map<String, String> {
        TODO("Not yet implemented")
    }

    actual suspend fun setMixer(mixer: String) {
    }

    actual suspend fun setFormat(bitsPerSample: Int, channels: Int, samplesPerSecond: Float) {
    }

    actual val supportsFormatControl: Boolean
        get() = TODO("Not yet implemented")

    actual fun getLengthMilis(): Long {
        TODO("Not yet implemented")
    }

    actual fun getPosition(): Long {
        TODO("Not yet implemented")
    }

    actual suspend fun volume(volume: Float) {
    }

    actual var updatePosition: (Long, Long) -> Unit
        get() = TODO("Not yet implemented")
        set(value) {}
    actual var updateState: (PlayerModel.PlayerState) -> Unit
        get() = TODO("Not yet implemented")
        set(value) {}

}