package me.selemba.common.ui.elements.player

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.selemba.common.audio.AudioFile
import me.selemba.common.audio.Player
import java.time.LocalTime

class PlayerModel {

    var position by mutableStateOf(0f)
        private set

    var state by mutableStateOf(PlayerState.STOPPED)
        private set

    private val scope = CoroutineScope( Dispatchers.IO)
    private var player : Player = Player(scope)

    var wasPlaying = false
        private set

    init {
        player.updatePosition={ pos,len ->
            if(state!=PlayerState.SEEKING) {
                position = pos / len.toFloat()
            }
        }
        player.updateState={
            if(state!=PlayerState.SEEKING){
                state = it
            }

        }
    }

    enum class PlayerState{
        PLAYING,
        PAUSED,
        SEEKING,
        STOPPED,
        //LOADING, //maybe
    }

    suspend fun seekStart(){
        wasPlaying = state==PlayerState.PLAYING
        stop()
        state=PlayerState.SEEKING
    }

    suspend fun seekEnd(){
        player.seek((player.getLengthMilis()*position).toInt())
        state = if(wasPlaying){
            start()
            PlayerState.PLAYING
        }else{
            PlayerState.PAUSED
        }
    }

    fun seek(position: Float){
        this.position = this.position + position
    }

    suspend fun start(){
        scope.launch { player.start() }
    }

    suspend fun stop(){
        scope.launch { player.stop() }
    }

    suspend fun startPause(){
        if(state==PlayerState.PLAYING){
            stop()
        }else{
            start()
        }
    }

    suspend fun loadFile(audioFile: AudioFile){
        println(audioFile.file.name)
        player.stop()
        player.loadFile(audioFile)
        state=PlayerState.PAUSED
    }

}
