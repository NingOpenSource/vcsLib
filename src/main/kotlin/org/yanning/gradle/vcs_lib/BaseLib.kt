package org.yanning.gradle.vcs_lib

import org.gradle.api.Project
import org.yanning.gradle.vcs_lib.core.AppConfig
import org.yanning.gradle.vcs_lib.core.LibrarySuffix
import org.yanning.gradle.vcs_lib.extension.App
import org.yanning.gradle.vcs_lib.task.Build
import org.yanning.gradle.vcs_lib.task.GUI
import org.yanning.gradle.vcs_lib.task.Update
import org.yanning.gradle.vcs_lib.task.Upload
import org.yanning.gradle.vcs_lib.utils.Log
import java.io.File

class BaseLib {
    companion object {


        fun apply(target: Project?) {
            if (AppConfig.getVCSLibPath().exists()&&AppConfig.getVCSLibPath().listFiles().isNullOrEmpty()) {
                /**
                 * 添加仓库地址
                 */
                target?.repositories?.maven {
                    it.url = AppConfig.getVCSLibPath().toURI()
                }
            }
            target?.tasks?.create(TaskConf.vcsLibGUI.name, GUI::class.java)
            target?.tasks?.create(TASK_vcsLibsBuild, Build::class.java){
                it.bindApp(app)
            }
            target?.tasks?.create(TaskConf.vcsLibUpdate.name, Update::class.java) {
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
            target?.tasks?.create(TASK_vcsLibsUpload, Upload::class.java) {
                it.bindApp(app)
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