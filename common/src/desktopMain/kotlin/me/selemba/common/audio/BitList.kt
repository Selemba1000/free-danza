package me.selemba.common.audio

import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.math.ceil

class BitList(list: List<Boolean>) {

    private var list: MutableList<Boolean> = mutableListOf()

    operator fun get(index: Int): Boolean {
        return list[index]
    }

    operator fun set(index: Int, value: Boolean) {
        list[index] = value
    }

    fun invert(): BitList {
        return BitList(list.map { !it })
    }

    fun toLongUnsigned(): Long {
        var tmp = 0L
        for (addr in list.indices) {
            if (list[addr]) {
                tmp += 0b1 shl addr
            }
        }
        return tmp
    }

    fun toLong(): Long {
        return if (list.last()) {
            (this.invert().toLongUnsigned() + 1) * (-1)
        } else {
            this.toLongUnsigned()
        }
    }

    fun toBytearray(byteCount: Int): ByteArray {
        val arr = ByteArray(byteCount)
        for (b in 0 until byteCount) {
            var byte = 0.toByte()
            for (addr in 0 until 8) {
                if (list.size - 1 < (b * 8) + addr) continue
                if (list[(b * 8) + addr]) byte = byte or (0b1 shl addr).toByte()
            }

            arr[b] = byte
        }
        return arr
    }

    fun slice(indices: IntRange): BitList {
        return BitList(list.slice(indices))
    }

    fun length(): Int {
        return list.size
    }

    fun append(bitList: BitList): BitList {
        list.addAll(bitList.list)
        return this
    }

    override fun toString(): String {
        var tmp = ""
        for (bit in list) {
            tmp += if (bit) "1" else "0"
        }
        return tmp.reversed()
    }

    init {
        this.list = list.toMutableList()
    }

}

fun Long.toBitList(): BitList {
    val list = mutableListOf<Boolean>()
    for (addr in 0 until Long.SIZE_BITS) {
        list.add((this shr addr) and 0b1 == 1L)
    }
    return BitList(list)
}

fun ByteArray.toBitList(): BitList {
    val list = mutableListOf<Boolean>()
    for (byte in this) {
        for (addr in 0 until 8) {
            list.add((byte.rotateRight(addr) and 0b1) == 0b1.toByte())
        }
    }
    return BitList(list)
}