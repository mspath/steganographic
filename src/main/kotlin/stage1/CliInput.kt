package stage1

/*
stage 1:
A steganography/cryptography program is versatile: it can perform a variety of tasks based on the user's wishes.
 You may want to hide a message within an image, encrypt the text message for extra security, or decipher
 a hidden message you got from someone else.
 In this stage, you will create the user interface for your program.
 The user will be able to choose a task by giving commands via standard input.
 */

fun main1() {
    while(true) {
        println("Task (hide, show, exit):")
        val command = readLine()!!
        when (command) {
            "exit" -> break
            "hide" -> println("Hiding message in image.")
            "show" -> println("Obtaining message from image.")
            else -> println("Wrong task: $command")
        }
    }
    println("Bye!")
}