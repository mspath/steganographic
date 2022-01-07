package stage2

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/*
  stage 2:
  read and manipulate the image
  When the input image is read, the least significant bit for each color (Red, Green, and Blue) is set to 1.
  The resulting image will be saved with the provided output image filename in the PNG format.
  A proper method should be applied so that the I/O exceptions do not terminate the program.
  In such cases, an exception message should be printed and the program should return to the command loop.
 */

fun setFirstBit(input: String, output: String) {
    val inputFile = File(input)
    val outputFile = File(output)

    val myImage: BufferedImage = ImageIO.read(inputFile)
    for (x in 0 until myImage.width) {
        for (y in 0 until myImage.height) {
            val color = Color(myImage.getRGB(x, y))
            val colorNew = Color(1 or color.red, 1 or color.green, 1 or color.blue)
            myImage.setRGB(x, y, colorNew.rgb)
        }
    }
    ImageIO.write(myImage, "png", outputFile)
    println("Input Image: $input")
    println("Output Image: $output")
    println("Image $output is saved.")
}

fun hide() {
    println("Input image file:")
    val input = readLine()!!
    println("Output image file:")
    val output = readLine()!!
    try {
        setFirstBit(input, output)
    } catch (e: Exception) {
        println("An exception occurred!")
    }
}

fun main2() {
    while(true) {
        println("Task (hide, show, exit):")
        val command = readLine()!!
        when (command) {
            "exit" -> break
            "hide" -> hide()
            "show" -> println("Obtaining message from image.")
            else -> println("Wrong task: $command")
        }
    }
    println("Bye!")
}