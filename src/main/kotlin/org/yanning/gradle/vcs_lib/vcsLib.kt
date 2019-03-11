package org.yanning.gradle.vcs_lib

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.yanning.gradle.vcs_lib.core.AppConfig
import org.yanning.gradle.vcs_lib.extension.RepositoryBuilder
import org.yanning.gradle.vcs_lib.task.GUI

class vcsLib : Plugin<Project> {

    override fun apply(target: Project) {
        val appConfig = AppConfig(target)
        val repo = RepositoryBuilder().setVcsLibConf(appConfig.getConf()!!).build()
        /**
         * 添加仓库地址
         */
        target.repositories.maven {
            it.url = repo!!.outDir().toURI()
        }
        if (target.task(TaskConf.vcsLibGUI.name).enabled) {
            /**
             * 创建GUI命令
             */
            target.tasks.create(TaskConf.vcsLibGUI.name, GUI::class.java)
        }
    }
}