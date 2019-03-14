package org.yanning.gradle.vcs_lib

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.yanning.gradle.vcs_lib.core.LibrarySuffix
import org.yanning.gradle.vcs_lib.extension.MavenBuild
import org.yanning.gradle.vcs_lib.task.GUI

class vcsLib_jar : Plugin<Project> {
    override fun apply(target: Project) {
        MavenBuild(target,LibrarySuffix.JAR).build()
//        val appConfig= AppConfig(target)
//        val conf=appConfig.getConf()
//        val uploadConf=appConfig.getUploadConf()
//        target.tasks.create(TaskConf.vcsLibUpload.name) {
//            //读取配置文件，链接到当前编译过程
//
//            //执行上传到vcs目录的命令
//
//
//
//            //上传成功，开始同步vcs仓库
//            val repo= RepositoryBuilder().setVcsLibConf(conf!!).build() ?: return@create
//            Log.out("start update...")
//            repo.update()
//            Log.out("start commit...")
//            repo.commit()
//            Log.out("complete commit. ")
//            Log.out("start upload..." + " [from " + repo.outDir()+ " to " + conf.vcsUri+ "]")
//            repo.upload()
//            Log.out("complete doUpload.")
//        }

    }
}