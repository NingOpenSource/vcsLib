package org.yanning.gradle.vcs_lib.gui

import com.jfoenix.controls.JFXButton
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.io.File

class SettingsView : View("Settings") {
    private val GRADLE_USER_HOME = SimpleStringProperty("")
    override val root = vbox {
        minWidth = 600.0
        minHeight = 300.0
        form {
            fieldset("Project") {
                field("GRADLE_USER_HOME") {
                    textfield(GRADLE_USER_HOME)
                    button("choose...") {
                        setOnAction {
                            val initDir = File(GRADLE_USER_HOME.value)
                            chooseDirectory("please choose a dir", if (initDir.exists()) initDir else null)?.let { targetDir ->
                                GRADLE_USER_HOME.set(targetDir.absolutePath)
                            }
                        }
                    }
                }
            }
            fieldset("Module") {
                field("uri") {
                    textfield()
                }
                field("username") {
                    textfield()
                }
                field("password") {
                    passwordfield()
                }
            }
        }
    }
}
