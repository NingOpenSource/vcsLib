package org.yanning.gradle.vcs_lib

import jodd.io.FileUtil
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.MavenPlugin
import org.yanning.gradle.vcs_lib.core.AppConfig
import org.yanning.gradle.vcs_lib.core.ConfKey
import org.yanning.gradle.vcs_lib.extension.RepositoryBuilder
import org.yanning.gradle.vcs_lib.utils.Log
import java.io.File
import java.io.IOException

class vcsLibPlugin : Plugin<Project> {
    companion object {
        const val TASK_GROUP = "vcsLib"
    }

    override fun apply(target: Project) {
        target.rootProject.also { target ->
            val appConfig = AppConfig(target)
            val config = appConfig.conf
            if (config.getConf(ConfKey.uri).isEmpty()) return
            val repo = RepositoryBuilder(config).build() ?: return

            target.task(TaskConf.vcsLibUpdate.name).apply {
                group = TASK_GROUP
                doFirst {
                    Log.out("repo update start...")
                    repo.update()
                    Log.out("repo update end...")
                }
            }
            target.allprojects.forEach { itemProject ->
                itemProject.repositories.add(
                        target.repositories.maven {
                            it.name = "vcsLib-${target.name}"
                            it.url = repo.outDir().toURI()
                        })
            }
            /**
             * 添加仓库地址
             */
            target.repositories.add(
                    target.repositories.maven {
                        it.name = "vcsLib-${target.name}"
                        it.url = repo.outDir().toURI()
                    })

            if (config.getConf(ConfKey.mavenId).isNotEmpty()) {
                target.plugins.withType(MavenPlugin::class.java) {
                }
                target.plugins.findPlugin(MavenPlugin::class.java)?.run {

                }

                val fileScriptName = "vcsLibUpload.gradle"
                if (appConfig.isRootProject()) return
//            val fileScript = File(target.projectDir, fileScriptName)
                target.extensions.add(ConfKey.VCSLIB_HOME.displyName(), repo.outDir().absolutePath)
                target.extensions.add(ConfKey.mavenId.displyName(), "")
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
                target.task(TaskConf.vcsLibUpload.name).run {
                    group = TASK_GROUP
                    dependsOn(target.tasks.findByName(TaskConf.vcsLibUpload.name)?.path,
                            target.tasks.findByName("uploadArchives")?.path)
                    doFirst {
                        Log.out("start update...")
                        repo.update()
                        Log.out("complete update,start upload..." + " [from " + repo.outDir() + " to " + config.getConf(ConfKey.uri) + "]")
                        repo.upload()
                        Log.out("complete doUpload.")
                    }
                }
            }
            if (true.toString() == config.getConf(ConfKey.isAutoUpdateOnBuild)) {
                /**
                 * 配置上传
                 */
                try {

                } catch (e: Exception) {
                    Log.err(e.localizedMessage)
                    e.printStackTrace()
                }

            }
            target.plugins.apply(MavenPlugin::class.java)
        }
    }
}