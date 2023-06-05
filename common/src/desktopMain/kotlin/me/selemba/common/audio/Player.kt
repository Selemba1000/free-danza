package me.selemba.common.audio

import kotlinx.coroutines.*
import java.io.*
import javax.sound.sampled.*


actual class Player actual constructor(actual val coroutineScope: CoroutineScope) {

    var mixer = AudioSystem.getMixer(AudioSystem.getMixerInfo().first())

    var stream : AudioInputStream? = null

    var format : AudioFileFormat? = null

    var job : Job? = null

    actual suspend fun start() {
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
    }

    actual suspend fun stop() {
        TODO("Not yet implemented")
    }

    actual suspend fun loadFile(file: String) {
        val fileFs = File(file)
        stream = AudioSystem.getAudioInputStream(fileFs)
        format = AudioSystem.getAudioFileFormat(fileFs)
    }

    actual suspend fun addFile(file: String){

    }

    actual suspend fun getMixers(): List<String> {
        return AudioSystem.getMixerInfo().map { "${it.name}:${it.vendor}" }.toList()
    }

    actual suspend fun setMixer(mixer: Int) {
        this.mixer = AudioSystem.getMixer(AudioSystem.getMixerInfo()[mixer])
        //reset()
    }

    private fun reset() {
        runBlocking {
            stop()
        }
    }
}