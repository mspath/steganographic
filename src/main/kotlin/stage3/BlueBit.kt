package stage3

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.pow

/*
  stage 3:
  hide a message in the blue bit.
  When the program reads the image bits in order to reconstruct the message, it has to know when to stop,
   that is, when it has read the entire message. For this, certain bytes should be applied at the end of
   the Bytes Array. Specifically, we will add three bytes with values 0, 0, 3
   (or 00000000 00000000 00000011 in the binary format). When the program encounters these bytes,
    it will know that it has reached the end of the message.
 */

fun hideInBlueBit(input: String, output: String, message: String) {
    val inputFile = File(input)
    val outputFile = File(output)
    val myImage: BufferedImage = ImageIO.read(inputFile)
    val messageArray = message.toMessageArray()

    // check whether the image can hold the message
    if (myImage.getAvailableBytes() < messageArray.size) {
        println("The input image is not large enough to hold this message.")
        return
    }

    var pos = 0
    for (byte in messageArray) {
        myImage.hideByte(byte, pos)
        pos += 8
    }
    myImage.hideByte(0b00000000.toByte(), pos)
    pos += 8
    myImage.hideByte(0b00000000.toByte(), pos)
    pos += 8
    myImage.hideByte(0b00000001.toByte(), pos)

    ImageIO.write(myImage, "png", outputFile)
    println("Message saved in $output image.")
}

fun messageInBlueBit(input: String) {
    val inputFile = File(input)
    val myImage: BufferedImage = ImageIO.read(inputFile)
    val blueBits = (0 until myImage.width * myImage.height).map { index ->
        val x = index % myImage.width
        val y = index / myImage.width
        val color = Color(myImage.getRGB(x, y))
        (color.blue and 1)
    }
    val bytes = blueBits.windowed(8, 8).map {
        it.joinToString("").toInt(2).toByte()
    }.toByteArray()
    val message = bytes.toMessage()
    println("Message:")
    println(message)
}

fun BufferedImage.hideByte(byte: Byte, pos: Int) {
    val b = byte.toInt()
    for (i in 0 until 8) {
        val index = pos + i
        val bit = if (b and 2.0.pow(i).toInt() > 0) 1 else 0
        val x = index % width
        val y = index / width
        val color = Color(getRGB(x, y))
        val blueNew = color.blue shr 1 shl 1 or bit
        val colorNew = Color(color.red, color.green, blueNew)
        setRGB(x, y, colorNew.rgb)
    }
}

// max number of bytes available for a message
fun BufferedImage.getAvailableBytes() = (this.width * this.height - 3) / 8

// build a bytearray of the message and add the end flag 001
fun String.toMessageArray() = this.encodeToByteArray() + byteArrayOf(0, 0, 1)

// scans the bytearray until the end flag is found and converts the message to a string
fun ByteArray.toMessage(): String? {
    val endFlag = listOf(0, 0, 1)
    val window = this.toList().windowed(3).map { it == endFlag }
    val end = window.indexOf(true)
    if (end < 0) return null
    return this.copyOfRange(0, end).toString(Charsets.UTF_8)
}

fun hide() {
    println("Input image file:")
    val input = readLine()!!
    println("Output image file:")
    val output = readLine()!!
    println("Message to hide:")
    val message = readLine()!!
    try {
        hideInBlueBit(input, output, message)
    } catch (e: Exception) {
        println("An exception occurred!")
    }
}

fun show() {
    println("Input image file:")
    val input = readLine()!!
    try {
        messageInBlueBit(input)
    } catch (e: Exception) {
        println("An exception occurred!")
    }
}

fun main() {
    while(true) {
        println("Task (hide, show, exit):")
        val command = readLine()!!
        when (command) {
            "exit" -> break
            "hide" -> hide()
            "show" -> show()
            else -> println("Wrong task: $command")
        }
    }
    println("Bye!")
}