package org.yanning.gradle.vcs_lib

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.yanning.gradle.vcs_lib.extension.App
import org.yanning.gradle.vcs_lib.task.Build
import org.yanning.gradle.vcs_lib.task.Update
import org.yanning.gradle.vcs_lib.task.Upload
import org.yanning.gradle.vcs_lib.utils.Log
import java.net.URI

class vcsLib : Plugin<Project> {

    override fun apply(target: Project?) {
        target?.pluginManager?.hasPlugin("maven")?.let {
            if (it) {
                target?.pluginManager?.apply("maven")
            }
        }
        val app = target?.extensions?.create(TASK_vcsLib, App::class.java, target)
        target?.getTasksByName("assemble", false)?.iterator()?.next()?.doLast { task ->
            //            System.err.println("success.....................")
            app?.controller?.doBuild()
            app?.controller?.doUpdate()
        }
        target?.tasks?.let {
            it.create(TASK_vcsLibsBuild, Build::class.java) {
                it.bindApp(app)
            }
            it.create(TASK_vcsLibsUpdate, Update::class.java) {
                it.bindApp(app)
                it.bindOutDirAction {
                    it.forEach {
                        target?.repositories?.maven { t: MavenArtifactRepository? ->
                            t?.url = it.toURI()
                            Log.out("add maven url->" + t?.url)
                        }
                    }
                }
            }
            it.create(TASK_vcsLibsUpload, Upload::class.java) {
                it.bindApp(app)
            }
        }

//        target?.allprojects?.forEach {
//
//        }
//        target?.repositories?.maven { t: MavenArtifactRepository? ->
//            t?.url = URI("temp/libs/")
//        }
//        target?.gradle?.buildStarted {
//            app?.controller?.doBuild()
//            app?.controller?.doUpdate()
//        }
    }

    companion object {
        val TASK_vcsLibInit = "vcsLibsInit"
        val TASK_vcsLib = "vcsLibs"
        val TASK_vcsLibsBuild = "vcsLibsBuild"
        val TASK_vcsLibsUpdate = "vcsLibsUpdate"
        val TASK_vcsLibsUpload = "vcsLibsUpload"
        @JvmStatic
        fun main(strings: Array<String>) {
            print("ok")
        }
    }
}