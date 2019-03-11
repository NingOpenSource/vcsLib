package org.yanning.gradle.vcs_lib

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.yanning.gradle.vcs_lib.core.AppConfig
import org.yanning.gradle.vcs_lib.extension.Repository
import org.yanning.gradle.vcs_lib.extension.RepositoryBuilder
import org.yanning.gradle.vcs_lib.task.Upload
import org.yanning.gradle.vcs_lib.utils.Log

class vcsLib_jar : Plugin<Project> {
    override fun apply(target: Project) {
        val appConfig= AppConfig(target)
        val conf=appConfig.getConf()
        val uploadConf=appConfig.getUploadConf()
        target.tasks.create(BaseLibUpload.TASK_vcsLibsUpload) {
            val repo= RepositoryBuilder().setVcsLibConf(conf!!).build() ?: return@create
            Log.out("start update...")
            repo.update()
            Log.out("start commit...")
            repo.commit()
            Log.out("complete commit. ")
            Log.out("start upload..." + " [from " + repo.outDir()+ " to " + conf.vcsUri+ "]")
            repo.upload()
            Log.out("complete doUpload.")
        }
    }
}