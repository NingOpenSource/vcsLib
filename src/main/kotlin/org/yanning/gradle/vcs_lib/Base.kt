package org.yanning.gradle.vcs_lib

import org.gradle.api.Project
import org.yanning.gradle.vcs_lib.core.PlugType
import org.yanning.gradle.vcs_lib.extension.App
import org.yanning.gradle.vcs_lib.task.Build
import org.yanning.gradle.vcs_lib.task.Update
import org.yanning.gradle.vcs_lib.task.Upload
import org.yanning.gradle.vcs_lib.utils.Log

 class Base {
    companion object {


        val TASK_vcsLibInit = "vcsLibInit"
        val TASK_vcsLib = "vcsLib"
        val TASK_vcsLibsBuild = "vcsLibBuild"
        val TASK_vcsLibsUpdate = "vcsLibUpdate"
        val TASK_vcsLibsUpload = "vcsLibUpload"

        fun apply(target: Project?,pluginType: PlugType) {
            target?.pluginManager?.hasPlugin("maven")?.let {
                if (it) {
                    target?.pluginManager?.apply("maven")
                }
            }
            val app = target?.extensions?.create(TASK_vcsLib, App::class.java, target,pluginType)
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
                            Log.out("add maven url->" + it?.toURI()?.toString())
//                        target?.repositories?.maven { t: MavenArtifactRepository? ->
//                            t?.url = it.toURI()
//                        }
                        }
                    }
                }
                it.create(TASK_vcsLibsUpload, Upload::class.java) {
                    it.bindApp(app)
                }
            }
//        FileUtil.readUTFString(javaClass.getResourceAsStream("/vcsLibUpload.gradle"))
//        val fileScript = File(target?.projectDir?.absolutePath, "vcsLibUpload.gradle");
//        FileUtil.writeString(
//                fileScript,
//                FileUtil.readUTFString(javaClass.getResourceAsStream("/vcsLibUpload.gradle"))
//                        .replace("$"+App.KEY_VCS_LIBS_GROUP_ID,"org.yanning.gradle")
//                        .replace("$"+App.KEY_VCS_LIBS_ARTIFACT_ID,"lib_vcs_demo")
//                        .replace("$"+App.KEY_VCS_LIBS_VERSION,"1.0.6")
//        )
//        target?.apply {
//            it.from(fileScript)
//        }
            System.err.println("-------------------------------" + target?.buildDir?.absolutePath + "---" + target?.path /*target?.resources?.text?.fromFile("")*/)
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
    }
}