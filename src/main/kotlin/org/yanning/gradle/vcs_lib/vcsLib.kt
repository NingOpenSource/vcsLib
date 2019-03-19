package org.yanning.gradle.vcs_lib

import jodd.io.FileUtil
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.yanning.gradle.vcs_lib.core.AppConfig
import org.yanning.gradle.vcs_lib.core.ConfKey
import org.yanning.gradle.vcs_lib.core.LibrarySuffix
import org.yanning.gradle.vcs_lib.extension.RepositoryBuilder
import org.yanning.gradle.vcs_lib.task.GUI
import org.yanning.gradle.vcs_lib.task.GUITask
import org.yanning.gradle.vcs_lib.task.UpdateTask
import org.yanning.gradle.vcs_lib.task.UploadTask
import org.yanning.gradle.vcs_lib.utils.Log
import java.io.File
import java.io.IOException

class vcsLib : Plugin<Project> {

    override fun apply(target: Project) {
        val appConfig = AppConfig(target)
        val config = appConfig.conf
        if (config.getConf(ConfKey.repoUri).isEmpty()) return
        val repo = RepositoryBuilder(config).build() ?: return
        target.tasks.register(TaskConf.vcsLibUpdate.name, UpdateTask::class.java, repo, config)
        /**
         * 添加仓库地址
         */
        target.repositories.add(
                target.repositories.maven {
                    it.name = "vcsLib-${target.name}"
                    it.url = repo.outDir().toURI()
                })


        if (target.tasks.findByName(TaskConf.vcsLibGUI.name) == null) {
            /**
             * 创建GUI命令
             */
            target.tasks.register(TaskConf.vcsLibGUI.name, GUITask::class.java,target)
        }

        if (true.toString() == config.getConf(ConfKey.isUseUploadMaven)) {
            /**
             * 配置上传
             */
            val libSuffix: LibrarySuffix
            if (LibrarySuffix.JAR.name.toLowerCase() == config.getConf(ConfKey.mavenUploadType)) {
                libSuffix = LibrarySuffix.JAR
            } else if (LibrarySuffix.AAR.name.toLowerCase() == config.getConf(ConfKey.mavenUploadType)) {
                libSuffix = LibrarySuffix.AAR
            } else {
                libSuffix = LibrarySuffix.JAR
            }
            try {
                if (appConfig.isRootProject()) return
                val fileScriptName: String
                if (libSuffix === org.yanning.gradle.vcs_lib.core.LibrarySuffix.AAR) {
                    fileScriptName = "vcsLibUpload_aar.gradle"
                } else if (libSuffix === org.yanning.gradle.vcs_lib.core.LibrarySuffix.JAR) {
                    fileScriptName = "vcsLibUpload_jar.gradle"
                } else {
                    fileScriptName = "vcsLibUpload_jar.gradle"
                }
//            val fileScript = File(target.projectDir, fileScriptName)
                target.extensions.add(ConfKey.vcsLibHome.displyName(), repo.outDir().toURI().toString())
                target.extensions.add(ConfKey.mavenGroupId.displyName(), config.getConf(ConfKey.mavenGroupId))
                target.extensions.add(ConfKey.mavenArtifactId.displyName(), config.getConf(ConfKey.mavenArtifactId))
                target.extensions.add(ConfKey.mavenVersionName.displyName(), config.getConf(ConfKey.mavenVersionName))
                val fileScript = File(target.projectDir, fileScriptName)
                if (!fileScript.exists() || fileScript.length() == 0.toLong()) {
                    try {
                        FileUtil.writeString(
                                fileScript,
                                FileUtil.readUTFString(javaClass.getResourceAsStream("/$fileScriptName"))
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                target.afterEvaluate {
                    Log.err("apply from ${fileScript.absolutePath}->")
                    target.apply { objectConfigurationAction -> objectConfigurationAction.from(fileScript) }
                }

                target.tasks.register(TaskConf.vcsLibUpload.name, UploadTask::class.java, repo, config)
                target.tasks.findByName(TaskConf.vcsLibUpload.name)
                        ?.dependsOn(target.tasks.findByName(TaskConf.vcsLibUpdate.name))
                        //执行上传到vcs目录的命令
                        ?.dependsOn(target.tasks.findByName("uploadArchives"))
            } catch (e: Exception) {
                Log.err(e.localizedMessage)
                e.printStackTrace()
            }

        }

    }
}