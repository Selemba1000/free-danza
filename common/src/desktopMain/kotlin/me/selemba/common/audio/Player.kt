package me.selemba.common.audio

import kotlinx.coroutines.*
import me.selemba.common.ui.elements.player.PlayerModel
import java.io.*
import java.lang.Math.pow
import java.nio.ByteBuffer
import javax.sound.sampled.*
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sign


actual class Player actual constructor(actual val coroutineScope: CoroutineScope) {

    var mixer = AudioSystem.getMixer(AudioSystem.getMixerInfo().filter { AudioSystem.getMixer(it).isLineSupported(Line.Info(SourceDataLine::class.java)) }.first())

    val playlist = listOf<AudioFile>().toMutableList()

    var format : AudioFormat = AudioFormat(
        AudioFormat.Encoding.PCM_SIGNED,
        44100f,
        16,
        2,
        (16*2)/8,
        44100f,
        false
    )

    var target : SourceDataLine? = null

    var source : AudioInputStream? = null

    var end : Long = 0

    var read : Long = 0

    private var lengthMilis: Long = 0

    var job : Job? = null

    var playing = false

    var fadeIn : Int = 0
    var fadeInProg : Int = 0

    var fadeOut : Int = 0
    var fadeOutProg : Int = 0

    var volume = 1.0f

    private fun setSource(){
        val tmp = AudioSystem.getAudioInputStream(playlist.first().file)
        val f = AudioSystem.getAudioFileFormat(playlist.first().file)
        val s = AudioSystem.getAudioInputStream(format,tmp)
        lengthMilis = (f.properties()["duration"]).toString().toLong()/1000
        val skip = playlist.first().cutStart*s.format.frameRate/1000
        s.skip(skip.toLong())
        //read = skip.toLong()
        read=0
        end = ((lengthMilis-playlist.first().cutEnd)*format.frameRate/1000).toLong()
        source=s
    }

    private suspend fun loop(){
        val transcode = source!!//AudioSystem.getAudioInputStream(format,source)

        while (true){
            val frame = ByteArray(format.frameSize)
            transcode.read(frame,0,format.frameSize)
            var vals = readFrame(frame,format.sampleSizeInBits,format.channels,true)
            vals = vals.map { it.map { (it*volume).toLong() } }
            if(vals.first().first().absoluteValue<2.0.pow(format.sampleSizeInBits)*0.1&&vals.first()[1].absoluteValue<2.0.pow(format.sampleSizeInBits)*0.1){
                val ex = writeFrame(vals,format.sampleSizeInBits,format.channels,true)
                target!!.write(ex,0,format.frameSize)
                break
            }
            read++
        }

        /*
        for (i in 0..3000){
            val frame = ByteArray(format.frameSize)
            transcode.read(frame,0,format.frameSize)
            var vals = readFrame(frame,format.sampleSizeInBits,format.channels,true)
            vals = vals.map { it.map { (it*volume).toLong() } }
            vals = vals.map { it.map { (it*(1- ((i / 3000) - 1).toDouble().pow(2.0))).toLong() } }
            val ex = writeFrame(vals,format.sampleSizeInBits,format.channels,true)
            target!!.write(ex,0,format.frameSize)
            read+=source!!.format.frameSize
        }*/

        while (playing){
            if(source==null){
                start()
                return
            }
            if(read>end){
                if(playlist.size>1){
                    next()
                    start()
                }
                return
            }

            if(read % 100 == 0L){
                updatePosition(getPosition(),getLengthMilis())
            }

            val frame = ByteArray(format.frameSize)
            transcode.read(frame,0,format.frameSize)
            var vals = readFrame(frame,format.sampleSizeInBits,format.channels,true)
            vals = vals.map { it.map { (it*volume).toLong() } }
            val ex = writeFrame(vals,format.sampleSizeInBits,format.channels,true)
            target!!.write(ex,0,format.frameSize)
            read++
        }
    }

    private fun readFrame(byteArray: ByteArray,sampleSize:Int,channels: Int,signed: Boolean):List<List<Long>>{
        val samples = (byteArray.size*8)/(sampleSize*channels)
        //println("samples: ${byteArray.size}")
        val res = mutableListOf<List<Long>>()
        for (s in 0 until samples) {
            val tmp = mutableListOf<Long>()
            val data = byteArray.slice((s*sampleSize*channels)/8 until ((s+1)*sampleSize*channels)/8).toByteArray().toBitList()
            for (c in 0 until channels) {
                val point = if(signed){
                     data.slice((sampleSize)*c until (sampleSize)*(c+1)).toLong()
                }else{
                    data.slice((sampleSize)*c until (sampleSize)*(c+1)).toLongUnsigned()
                }
                //println("$c:$point")
                tmp.add(point)
            }
            res.add(tmp)
        }
        return res
    }

    private fun writeFrame(data: List<List<Long>>,sampleSize: Int,channels: Int,signed: Boolean):ByteArray{
        val ret = BitList(listOf())
        for (s in data.indices){
            for (c in data[s].indices){
                ret.append(data[s][c].toBitList().slice(0 until sampleSize))
                //println(data[s][c].toBitList().toLong())
            }
        }
        //println((sampleSize*channels*data.size)/8)
        return ret.toBytearray((sampleSize*channels*data.size)/8)
    }

    actual suspend fun start() {
        if(playing)return
        if(mixer == null) return
        if(target==null){
            target = mixer.getLine(DataLine.Info(SourceDataLine::class.java,format)) as SourceDataLine
            target!!.addLineListener {
                if(it.type== LineEvent.Type.START)updateState(PlayerModel.PlayerState.PLAYING)
                if(it.type== LineEvent.Type.STOP)updateState(PlayerModel.PlayerState.PAUSED)
            }
        }
        if(!target!!.isOpen)target!!.open()
        if(!target!!.isRunning)target!!.start()
        if(source==null)setSource()
        playing=true
        job = coroutineScope.launch {
            loop()
        }
    }

    actual suspend fun stop() {
        playing = false
        target?.drain()
        job?.join()
        target?.stop()
        //target?.close()
    }

    actual suspend fun next(){
        playlist.remove(playlist.first())
        source = null
        println("next")
    }

    actual suspend fun seek(positionMilis: Int) {
        playing=false
        job?.join()
        target?.stop()
        target?.flush()
        setSource()
        val skip = positionMilis*source!!.format.frameRate/1000
        source!!.skip(skip.toLong())
        read=skip.toLong()
    }

    actual fun getLengthMilis():Long{
        return lengthMilis
    }

    actual fun getPosition():Long{
        if(end==0L){
            return 5L
        }
        if(read==0L) return 1L
        return (lengthMilis*(read/end.toFloat())).toLong()
    }

    actual suspend fun fadeOut(lengthMilis: Int) {
        fadeOutProg = 0
        fadeOut=lengthMilis
    }

    actual suspend fun fadeIn(lengthMilis: Int) {
        fadeInProg=0
        fadeIn=lengthMilis
    }

    actual suspend fun volume(volume: Float){
        this.volume=volume
    }

    actual suspend fun loadFile(file: AudioFile) {
        clearFiles()
        addFile(file)
    }

    actual suspend fun addFile(file: AudioFile){
        playlist.add(file)
    }

    actual suspend fun clearFiles() {
        playlist.clear()
    }

    actual fun listFiles(): List<AudioFile> {
        return playlist.toList()
    }

    actual fun getMixers(): Map<String,String> {
        return AudioSystem.getMixerInfo().filter { AudioSystem.getMixer(it).isLineSupported(Line.Info(SourceDataLine::class.java)) }
            .associate { Pair<String, String>(it.name, it.description) }
    }

    actual suspend fun setMixer(mixer: String) {
        this.mixer = AudioSystem.getMixer(AudioSystem.getMixerInfo().find { it.name==mixer })
        stop()
        start()
    }

    actual val supportsFormatControl: Boolean = true
    actual suspend fun setFormat(bitsPerSample: Int, channels: Int, samplesPerSecond: Float) {
        format = AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            samplesPerSecond,
            bitsPerSample,
            channels,
            (bitsPerSample*channels)/8,
            samplesPerSecond,
            true
        )
    }

    actual var updatePosition: (Long,Long) -> Unit = { pos,len ->  }
    actual var updateState: (PlayerModel.PlayerState) -> Unit = {}


}