package org.yanning.gradle.vcs_lib.gui

import tornadofx.*


class MainView : View("My View") {
    private val models = observableListOf<Model>()
    override val root = vbox {
        menubar {
            menu("File") {
                item("Settings...") {
                    setOnAction {
                        SettingsView().openModal()
                    }
                }
                item("Exit") {
                    setOnAction {
                        close()
                    }
                }
            }
            menu("Help")
        }
        hbox {
            listview(models){
                multiSelect(true)
            }
//            treeview(TreeItem("Project").apply {
//                for (i in 1..5) {
//                    treeitem("Module$i")
//                }
//            }) {
//                //                minHeight=200.0
////                minWidth=200.0
//            }
        }
    }

    init {
        for (i in 0..10){
            val item=Model()
            item.name="Module$i"
            item.path=":Module$i"
            models.add(item)
        }
    }
}

internal class Model {
    var name = ""
    var path = ""

    override fun toString(): String {
        return name
    }
}