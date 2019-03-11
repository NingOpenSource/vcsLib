package org.yanning.gradle.vcs_lib.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.yanning.gradle.vcs_lib.extension.App
import org.yanning.gradle.vcs_lib.utils.Log

/**
 * 1,上传到本地仓库
 *
 *
 * 2,从远程仓库更新到本地仓库
 *
 *
 * 3，本地仓库提交到远程仓库
 *
 *
 * 4，完成lib的上传
 */
class Upload : DefaultTask() {
    private var app: App? = null

    fun bindApp(app: App) {
        this.app = app
        app.controller.bindUpload(this)
    }

    @TaskAction
    fun doUpload() {
        if (app != null) {
            //            org.gradle.api.tasks.Upload
            if (app!!.repositoriesTo.repositories.size > 0) {
                val repositoriesTo = app!!.repositoriesTo
                if (repositoriesTo.groupId == null) {
                    Log.err("not set groupId")
                    return
                }
                val repository = repositoriesTo.repositories[0]
                Log.out("start update...")
                repository.update()
                Log.out("start commit...")
                repository.commit()
                Log.out("complete commit. ")
                Log.out("start upload..." + " [from " + repository.outDir().path + " to " + repository.url + "]")
                repository.upload()
                Log.out("complete doUpload.")
            }
        }
    }
}
