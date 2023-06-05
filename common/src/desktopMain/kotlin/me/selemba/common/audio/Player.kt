package me.selemba.common.audio

import com.tagtraum.ffsampledsp.*
import kotlinx.coroutines.*
import java.io.*
import java.util.concurrent.*
import javax.sound.sampled.*


actual class NativePlayer actual constructor(private val coroutineScope: CoroutineScope) {

    var mixer = AudioSystem.getMixer(AudioSystem.getMixerInfo().first())

    var stream : AudioInputStream? = null

    var format : AudioFileFormat? = null

    var job : Job? = null

    actual suspend fun start(): Boolean {
        job=coroutineScope.launch {
            if (stream!=null){
                val pcmFormat = AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    format!!.format.sampleRate,
                    16,
                    format!!.format.channels,
                    16 * format!!.format.channels / 8,
                    format!!.format.sampleRate,
                    format!!.format.isBigEndian
                )
                val clip = mixer.getLine(DataLine.Info(Clip::class.java,pcmFormat)) as Clip
                println(mixer.mixerInfo)
                val rawStream = AudioSystem.getAudioInputStream(pcmFormat,stream)
                clip.open(rawStream)
                clip.start()
            }
        }
        return true
    }

    actual suspend fun stop(): Future<Boolean> {
        TODO("Not yet implemented")
    }

    actual suspend fun load(file: String) {
        val fileFs = File(file)
        stream = AudioSystem.getAudioInputStream(fileFs)
        format = AudioSystem.getAudioFileFormat(fileFs)
        println("test")
    }

    actual fun getMixers(): List<Mixer.Info> {
        return AudioSystem.getMixerInfo().toList()
    }

    actual fun getLines(): List<Line.Info>{
        return mixer.sourceLineInfo.toList()
    }

    actual fun setMixer(mixer: Mixer.Info) {
        this.mixer = AudioSystem.getMixer(mixer)
        //reset()
    }

    private fun reset() {
        runBlocking {
            stop()
        }
    }
}