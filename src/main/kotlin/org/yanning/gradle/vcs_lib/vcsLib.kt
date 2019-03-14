package org.yanning.gradle.vcs_lib

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.yanning.gradle.vcs_lib.core.AppConfig
import org.yanning.gradle.vcs_lib.core.ConfKey
import org.yanning.gradle.vcs_lib.extension.RepositoryBuilder
import org.yanning.gradle.vcs_lib.task.GUI
import org.yanning.gradle.vcs_lib.utils.Log

class vcsLib : Plugin<Project> {

    override fun apply(target: Project) {
        val appConfig = AppConfig(target)
        val config = appConfig.conf
        if (config.getConf(ConfKey.repoUri).isEmpty()) return
        val repo = RepositoryBuilder(config).build() ?: return
        target.tasks.create(TaskConf.vcsLibUpdate.name) {
            if (repo.hasCheckout()) {
                Log.out("repo has updated,you can update repo forced use to 'gradle vcsLibUpdate'!")
            } else {
                Log.out("repo update start...")
                repo.update()
                Log.out("repo update end...")
            }
        }
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
            target.tasks.create(TaskConf.vcsLibGUI.name, GUI::class.java)
        }
    }
}