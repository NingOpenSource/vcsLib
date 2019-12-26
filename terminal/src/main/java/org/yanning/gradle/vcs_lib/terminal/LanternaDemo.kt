package org.yanning.gradle.vcs_lib.terminal

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.Terminal

import java.io.IOException
import java.util.*
import kotlin.concurrent.thread
import kotlin.concurrent.timerTask

object LanternaDemo {
    @Volatile
    private var isTerminalClosed = false
    @Volatile
    private var fps = 60f

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {

        // Setup terminal and screen layers
        val terminal = DefaultTerminalFactory().createSwingTerminal()
//                .setForceTextTerminal(true)
//                .setAutoOpenTerminalEmulatorWindow(true)
//                .createTerminal()
        terminal.setCursorVisible(true)
//        Timer(true).schedule(timerTask {
//            try {
//                terminal.flush()
//                if (!isTerminalClosed)
//                    run()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        },1000,1)
        terminal.addResizeListener { terminal1, newSize ->
            try {
                terminal1.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        val screen = TerminalScreen(terminal)
        screen.startScreen()

        // Create panel to hold components
        val root = Panel()

        root.layoutManager = LinearLayout(Direction.HORIZONTAL)
        root.addComponent(userPanel())
        root.addComponent(userPanel2())

        // Create window to hold the panel
        val window = BasicWindow()
        window.component = root
        // Create gui and start gui
        val gui = MultiWindowTextGUI(screen, DefaultWindowManager(), EmptySpace(TextColor.ANSI.BLUE))
        gui.addWindowAndWait(window)
        isTerminalClosed = true
        //        AppLauncher.Companion.startApp(args);
    }

    private fun userPanel2(): Panel {
        val panel = Panel()
        panel.layoutManager = GridLayout(2)
        val progressBar = ProgressBar()
        progressBar.preferredWidth = 20
        panel.addComponent(progressBar)
        object : Thread() {
            override fun run() {
                super.run()
                try {
                    Thread.sleep(2000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                var i = 0
                while (i < 100) {
                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                    i++
                    progressBar.value = i
                }
            }
        }.start()
        return panel
    }

    private fun userPanel(): Panel {
        val panel = Panel()
        panel.layoutManager = GridLayout(2)
        panel.addComponent(Label("Forename"))
        panel.addComponent(TextBox())
        panel.addComponent(Label("Surname"))
        panel.addComponent(TextBox())
        panel.addComponent(EmptySpace(TerminalSize(0, 0))) // Empty space underneath labels
        panel.addComponent(Button("Submit") {
            println("Submit")
        })
        panel.addComponent(Button("exit") {
            System.exit(0)
        })
        return panel
    }
}
