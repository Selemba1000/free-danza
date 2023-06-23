package me.selemba.common.audio

import kotlinx.coroutines.*
import me.selemba.common.ui.elements.player.PlayerModel

expect class Player(coroutineScope: CoroutineScope) {

    suspend fun loadFile(file: AudioFile)
    suspend fun addFile(file: AudioFile)
    suspend fun clearFiles()
    fun listFiles():List<AudioFile>

    suspend fun start()
    suspend fun stop()
    suspend fun next()
    suspend fun seek(positionMilis: Int)
    suspend fun fadeOut(lengthMilis: Int)
    suspend fun fadeIn(lengthMilis: Int)
    fun getLengthMilis():Long
    fun getPosition():Long
    suspend fun volume(volume: Float)

    fun getMixers():Map<String,String>
    suspend fun setMixer(mixer: String)

    suspend fun setFormat(bitsPerSample: Int,channels: Int,samplesPerSecond: Float)

    val supportsFormatControl: Boolean

    val coroutineScope: CoroutineScope

    var updatePosition: (Long,Long)->Unit

    var updateState: (PlayerModel.PlayerState)->Unit

}