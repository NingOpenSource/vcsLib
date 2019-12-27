//package org.yanning.gradle.vcs_lib.extension
//
//import com.sun.jndi.toolkit.url.Uri
//import jodd.io.FileUtil
//import org.gradle.api.Project
//import org.yanning.gradle.vcs_lib.TaskConf
//import org.yanning.gradle.vcs_lib.core.AppConfig
//import org.yanning.gradle.vcs_lib.core.ConfKey
//import org.yanning.gradle.vcs_lib.core.LibrarySuffix
//import org.yanning.gradle.vcs_lib.task.GUI
//import org.yanning.gradle.vcs_lib.utils.Log
//import java.io.File
//import java.io.IOException
//import java.net.URI
//
//class MavenBuild(
//        private val target: Project,
//        private val libSuffix: LibrarySuffix) {
//
//    fun build() {
//        try {
//            val appConfig = AppConfig(target)
//            if (appConfig.isRootProject()) return
//            val conf = appConfig.conf
//            val repo = RepositoryBuilder(conf).build() ?: return
//            val fileScriptName: String
//            if (libSuffix === LibrarySuffix.AAR) {
//                fileScriptName = "vcsLibUpload_aar.gradle"
//            } else if (libSuffix === LibrarySuffix.JAR) {
//                fileScriptName = "vcsLibUpload_jar.gradle"
//            } else {
//                fileScriptName = "vcsLibUpload_jar.gradle"
//            }
////            val fileScript = File(target.projectDir, fileScriptName)
//            target.extensions.add(ConfKey.vcsLibHome.displyName(), File(conf.getConf(ConfKey.vcsLibHome,AppConfig.vcsLibHome.absolutePath)).toURI().toString())
//            target.extensions.add(ConfKey.mavenGroupId.displyName(), conf.getConf(ConfKey.mavenGroupId))
//            target.extensions.add(ConfKey.mavenArtifactId.displyName(), conf.getConf(ConfKey.mavenArtifactId))
//            target.extensions.add(ConfKey.mavenVersionName.displyName(), conf.getConf(ConfKey.mavenVersionName))
//            val fileScript = File(target.buildDir, fileScriptName)
//            if (!fileScript.exists()) {
//                try {
//                    FileUtil.writeString(
//                            fileScript,
//                            FileUtil.readUTFString(javaClass.getResourceAsStream("/$fileScriptName"))
//                    )
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//            Log.err("apply from ${fileScript.absolutePath}->")
//            target.apply { objectConfigurationAction -> objectConfigurationAction.from(fileScript) }
//            target.tasks.create(TaskConf.vcsLibUpload.name) {
//                //读取配置文件，链接到当前编译过程
////
////
////            try {
////                FileUtil.writeString(
////                        fileScript,
////                        FileUtil.readUTFString(javaClass.getResourceAsStream("/$fileScriptName"))
////                                .replace("\$VCS_LIB_GROUP_ID", conf.getConf(ConfKey.mavenGroupId), true)
////                                .replace("\$VCS_LIB_ARTIFACT_ID", conf.getConf(ConfKey.mavenArtifactId), true)
////                                .replace("\$VCS_LIB_VERSION", conf.getConf(ConfKey.mavenVersionName), true)
////                                .replace("\$VCS_LIB_HOME", repo.outDir().toURI().toString())
////                )
////                target.apply { objectConfigurationAction -> objectConfigurationAction.from(fileScript) }
////            } catch (e: IOException) {
////                e.printStackTrace()
////            }
//                //上传成功，开始同步vcs仓库
//                Log.out("start update...")
//                repo.update()
//                Log.out("start upload..." + " [from " + repo.outDir() + " to " + conf.getConf(ConfKey.repoUri) + "]")
//                repo.upload()
//                Log.out("complete doUpload.")
//            }
//                    .dependsOn(target.tasks.findByName(TaskConf.vcsLibUpdate.name))
//                    //执行上传到vcs目录的命令
//                    .dependsOn(target.tasks.findByName("uploadArchives"))
//
//
//        } catch (e: Exception) {
//            Log.err(e.localizedMessage)
//            e.printStackTrace()
//        }
//    }
//}
