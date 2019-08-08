package org.yanning.gradle.vcs_lib.gui

import tornadofx.*

class AppLauncher :App(MainView::class){


    companion object{
        fun startApp(vararg args:String){
            launch<AppLauncher>(*args)
        }
    }
}