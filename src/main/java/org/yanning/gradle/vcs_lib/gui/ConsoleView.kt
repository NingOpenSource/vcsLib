package org.yanning.gradle.vcs_lib.gui

import javafx.scene.control.Alert
import tornadofx.*

class ConsoleView : View("VCSLib-Console") {
    override val root = vbox {
        form {
            fieldset("console") {
                textarea {
                }
            }
            fieldset("Repository") {
                field("url") {
                    textfield()
                }
                field("username") {
                    textfield()
                }
                field("password") {
                    textfield()
                }
                field("out path") {
                    textfield()
                    button("choose dir") {
                        action {
                            chooseDirectory {

                            }
                        }
                    }
                }
                button("test connection") {
                    action {
                        alert(type = Alert.AlertType.INFORMATION, header = "Tip", content = "Connect success!")
                    }
                }
            }
            fieldset("Upload library") {
                field("group id") {
                    textfield()
                }
                field("artifact id") {
                    textfield()
                }
                field("version") {
                    textfield()
                }
                button("upload") {
                    action {
                        alert(type = Alert.AlertType.INFORMATION, header = "Tip", content = "Upload success!")
                    }
                }
            }
            button("update library") {
                action {
                    alert(type = Alert.AlertType.INFORMATION, header = "Tip", content = "Update success!")
                }
            }
        }

    }

}
