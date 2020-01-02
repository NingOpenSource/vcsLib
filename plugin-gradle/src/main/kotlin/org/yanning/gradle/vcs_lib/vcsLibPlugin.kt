package org.yanning.gradle.vcs_lib

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.maven.MavenResolver
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.plugins.MavenPlugin
import org.gradle.api.publication.maven.internal.deployer.DefaultGroovyMavenDeployer
import org.gradle.api.tasks.Upload
import org.yanning.gradle.vcs_lib.core.AppConfig
import org.yanning.gradle.vcs_lib.core.RepoConfig
import org.yanning.gradle.vcs_lib.extension.RepoHelper
import org.yanning.gradle.vcs_lib.utils.Log
import java.io.File

class vcsLibPlugin : Plugin<Project> {
    companion object {
        const val TASK_GROUP = "vcsLib"
    }

    private fun confSubProject(target: Project) {
        val repoConfList = RepoConfig.loadConfs(File(target.rootDir, AppConfig.confFileName))
        if (target.rootProject != target) {//覆盖root的配置
            repoConfList.putAll(RepoConfig.loadConfs(File(target.projectDir, AppConfig.confFileName)))
        }
        target.run {
            repositories.apply {
                repoConfList.filterKeys {
                    //已经添加过的maven仓库不再进行重复添加
                    names.contains(it)
                }.forEach { (uri, _) ->
                    maven {
                        RepoConfig.outDir(uri).also { dir ->
                            it.name = uri
                            it.url = dir.toURI()
                        }
                    }
                }
            }
        }
        target.tasks.create("vcsLibUpdate").apply {
            dependsOn(":build")
            group = TASK_GROUP
            actions.add(Action {
                repoConfList.forEach { (_, conf) ->
                    RepoHelper(conf).connectRepo()?.apply {
                        Log.out("repo update start...")
                        update()
                        Log.out("repo update end...")
                    }
                }
            })
        }
    }

    override fun apply(target: Project) {
        confSubProject(target)
//
//        target.rootProject.also { target ->
//            val appConfig = AppConfig(target)
//            val confs = RepoConfig.loadConfs(appConfig)
//            val config = appConfig.conf
//            if (config.getConf(ConfKey.uri).isEmpty()) return
//            val repo = RepoHelper(config).build() ?: return
//
//            target.task(TaskConf.vcsLibUpdate.name).apply {
//                dependsOn(":build")
//                group = TASK_GROUP
//                actions.add(Action {
//
//                })
//                doFirst {
//                    Log.out("repo update start...")
//                    repo.update()
//                    Log.out("repo update end...")
//                }
//            }
//            target.allprojects.forEach { itemProject ->
//                itemProject.repositories.add(
//                        target.repositories.maven {
//                            it.name = "vcsLib-${target.name}"
//                            it.url = repo.outDir().toURI()
//                        })
//            }
//            /**
//             * 添加仓库地址
//             */
//            target.repositories.add(
//                    target.repositories.maven {
//                        it.name = "vcsLib-${target.name}"
//                        it.url = repo.outDir().toURI()
//                    })
//***************************************************************************************************************
//            if (config.getConf(ConfKey.mavenId).isNotEmpty()) {
//                target.plugins.withType(MavenPlugin::class.java) {
//                }
//                target.plugins.findPlugin(MavenPlugin::class.java)?.run {
//
//                }
//
//                val fileScriptName = "vcsLibUpload.gradle"
//                if (appConfig.isRootProject()) return
////            val fileScript = File(target.projectDir, fileScriptName)
//                target.extensions.add(ConfKey.VCSLIB_HOME.displyName(), repo.outDir().absolutePath)
//                target.extensions.add(ConfKey.mavenId.displyName(), "")
//                val fileScript = File(target.projectDir, fileScriptName)
//                if (!fileScript.exists() || fileScript.length() == 0.toLong()) {
//                    try {
//                        FileUtil.writeString(
//                                fileScript,
//                                FileUtil.readUTFString(javaClass.getResourceAsStream("/$fileScriptName"))
//                        )
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//                target.afterEvaluate {
//                    Log.err("apply from ${fileScript.absolutePath}->")
//                    target.apply { objectConfigurationAction -> objectConfigurationAction.from(fileScript) }
//                }
//                target.task(TaskConf.vcsLibUpload.name).run {
//                    group = TASK_GROUP
//                    dependsOn(target.tasks.findByName(TaskConf.vcsLibUpload.name)?.path,
//                            target.tasks.findByName("uploadArchives")?.path)
//                    doFirst {
//                        Log.out("start update...")
//                        repo.update()
//                        Log.out("complete update,start upload..." + " [from " + repo.outDir() + " to " + config.getConf(ConfKey.uri) + "]")
//                        repo.upload()
//                        Log.out("complete doUpload.")
//                    }
//                }
//            }
//            if (true.toString() == config.getConf(ConfKey.isAutoUpdateOnBuild)) {
//                /**
//                 * 配置上传
//                 */
//                try {
//
//                } catch (e: Exception) {
//                    Log.err(e.localizedMessage)
//                    e.printStackTrace()
//                }
//
//            }
//            target.plugins.apply(MavenPlugin::class.java)
//        }

        target.extensions.findByType(Upload::class.java)?.repositories?.withType(DefaultGroovyMavenDeployer::class.java)?.get(0)?.apply {

        }
    }
}