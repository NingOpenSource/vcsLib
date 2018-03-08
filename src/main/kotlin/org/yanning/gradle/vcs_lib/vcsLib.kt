package org.yanning.gradle.vcs_lib

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.yanning.gradle.vcs_lib.extension.App
import org.yanning.gradle.vcs_lib.task.Build
import org.yanning.gradle.vcs_lib.task.Update
import org.yanning.gradle.vcs_lib.task.Upload

class vcsLib : Plugin<Project> {

    override fun apply(target: Project?) {
        target?.getTasksByName("assemble", false)?.iterator()?.next()?.doLast { task ->
            System.err.println("success.....................")
//            target.getTasksByName(TASK_vcsLib, false).iterator().next();
        }
        target?.pluginManager?.hasPlugin("maven")?.let {
            if (it) {
                target?.pluginManager?.apply("maven")
            }
        }
        val app = target?.extensions?.create(TASK_vcsLib, App::class.java, target)
        target?.tasks?.let {
            it.create(TASK_vcsLibsBuild, Build::class.java) {
                it.bindApp(app)
            }
            it.create(TASK_vcsLibsUpdate, Update::class.java) {
                it.bindApp(app)
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