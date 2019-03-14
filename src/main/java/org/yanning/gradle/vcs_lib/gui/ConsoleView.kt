package org.yanning.gradle.vcs_lib.gui

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.RadioButton
import javafx.scene.input.ClipboardContent
import javafx.stage.Modality
import javafx.stage.StageStyle
import org.yanning.gradle.vcs_lib.core.AppConfig
import org.yanning.gradle.vcs_lib.core.LibrarySuffix
import tornadofx.*
import java.awt.DisplayMode

/**
 * @param moduleNames `settings.gradle`中记录的模块
 */
class ConsoleView(
        private val moduleNames: List<String>? = listOf("root", "demo1sadasdasd", "demo2dasad", "demo3", "demo4")
) : View("VCSLib-Console") {
    private val repositoriesUrl = SimpleStringProperty()
    private val repositoriesUsername = SimpleStringProperty()
    private val repositoriesPassword = SimpleStringProperty()
    private val repositoriesOutPath = SimpleStringProperty()
    private val uploadModuleName = SimpleStringProperty()
    private val uploadLibrarySuffix = SimpleObjectProperty<LibrarySuffix>()
    private val uploadGroupId = SimpleStringProperty()
    private val uploadArtifactId = SimpleStringProperty()
    private val uploadVersion = SimpleStringProperty()
    private val uploadVersionUsage = SimpleStringProperty()
    override val root = vbox {
        form {
            fieldset("Repository") {
                field("url") {
                    textfield(repositoriesUrl)
                }
                field("username") {
                    textfield(repositoriesUsername)
                }
                field("password") {
                    textfield(repositoriesPassword)
                }
                field("out path") {
                    textfield(repositoriesOutPath)
                    button("choose dir") {
                        action {
                            repositoriesOutPath.set(chooseDirectory()?.absolutePath)
                        }
                    }
                }
                field {
                    button("test connection") {
                        action {
                            alert(type = Alert.AlertType.NONE, buttons = *arrayOf(ButtonType.OK), header = "Tip", content = "Connect success!")
                        }
                    }
                    button("update repository") {
                        action {
                            alert(type = Alert.AlertType.INFORMATION, header = "Tip", content = "Update repository success!")
                        }
                    }
                }

            }
            fieldset("Upload library") {
                field("module") {
                    combobox(uploadModuleName, values = moduleNames)
                }
                field("library suffix") {
                    togglegroup {
                        radiobutton(LibrarySuffix.JAR.displayName, value = LibrarySuffix.JAR) {
                            whenSelected {
                                uploadLibrarySuffix.set(LibrarySuffix.JAR)
                            }
                            uploadLibrarySuffix.addListener { _, _, newValue ->
                                isSelected = LibrarySuffix.JAR == newValue
                            }
                        }
                        radiobutton(LibrarySuffix.AAR.displayName, value = LibrarySuffix.AAR) {
                            whenSelected {
                                uploadLibrarySuffix.set(LibrarySuffix.AAR)
                            }
                            uploadLibrarySuffix.addListener { _, _, newValue ->
                                isSelected = LibrarySuffix.AAR == newValue
                            }
                        }
                    }

                }
                field("group id") {
                    textfield(uploadGroupId)
                }
                field("artifact id") {
                    textfield(uploadArtifactId)
                }
                field("version") {
                    textfield(uploadVersion)
                }
                field("usage") {
                    textfield(uploadVersionUsage) {
                        isEditable = false
                    }
                    button("copy") {
                        action {
                            clipboard.setContent(ClipboardContent().apply {
                                putString(uploadVersionUsage.get())
                            })
                        }
                    }
                }
                button("upload library") {
                    action {
                        LogView("upload library").openModal(escapeClosesWindow = false, block = true, resizable = false)
                    }
                }
            }
        }

    }

    init {
        uploadGroupId.addListener(ChangeListener { _, _, _ -> renderUploadVersionUsage() })
        uploadArtifactId.addListener(ChangeListener { _, _, _ -> renderUploadVersionUsage() })
        uploadVersion.addListener(ChangeListener { _, _, _ -> renderUploadVersionUsage() })
        repositoriesOutPath.set(""/*,AppConfig.getDefaultVCSLibPath().absolutePath*/)
        uploadLibrarySuffix.set(LibrarySuffix.JAR)
        demo()
    }

    fun demo() {
        uploadModuleName.set("demo3")
        uploadVersion.set("0.0.1")
        uploadGroupId.set("com.demo.lib")
        uploadArtifactId.set("demo123")
    }

    private fun renderUploadVersionUsage() {
        if (uploadArtifactId.get().isNullOrEmpty()
                || uploadGroupId.get().isNullOrEmpty()
                || uploadVersion.get().isNullOrEmpty()) {
            uploadVersionUsage.set("")
        } else {
            uploadVersionUsage.set(
                    "implementation '${uploadGroupId.get()}:${uploadArtifactId.get()}:${uploadVersion.get()}'"
            )
        }
    }
}

class LogView(title: String) : View(title) {
    private val consoleLog = SimpleStringProperty()
    private val buffer = StringBuffer()
    override val root = vbox {
        form {
            fieldset("Console log") {
                field {
                    textarea(consoleLog) {
                        isEditable = false
                    }
                }
            }
        }
    }

    fun appendLog(log: String) {
        buffer.append(log)
        consoleLog.set(buffer.toString())
    }

}