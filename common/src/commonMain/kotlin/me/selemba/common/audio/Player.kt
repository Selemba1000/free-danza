package me.selemba.common.audio

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.Future
import javax.sound.sampled.*

class Player(private val coroutineScope: CoroutineScope) {

    private val flow = MutableSharedFlow<PlayerState>()

    private var state = PlayerState(false,1,0f,)

    private val native = NativePlayer(coroutineScope)

    private suspend fun emit(){
        flow.emit(state)
    }

    suspend fun load(file: String){
        native.load(file)
        emit()
    }

    suspend fun start(){
        state.playing=native.start()
        emit()
    }

    suspend fun stop(){
        state.playing=native.stop().get()
        emit()
    }

    fun getMixers():List<Mixer.Info>{
        return native.getMixers()
    }

    fun setMixer(mixer: Mixer.Info){
        native.setMixer(mixer)
    }

    fun getLines():List<Line.Info>{
        return native.getLines()
    }

}

data class PlayerState(var playing : Boolean, var volume:Int, var position:Float) {
}

expect class NativePlayer(coroutineScope: CoroutineScope) {
    suspend fun start():Boolean
    suspend fun stop():Future<Boolean>
    suspend fun load(file: String)
    fun getMixers():List<Mixer.Info>
    fun setMixer(mixer: Mixer.Info)
    fun getLines(): List<Line.Info>

}