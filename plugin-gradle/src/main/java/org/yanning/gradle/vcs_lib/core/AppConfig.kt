package org.yanning.gradle.vcs_lib.core

import org.gradle.api.Project
import java.io.File

class AppConfig(private val target: Project) {
    companion object {
        const val confFileName = "vcsLib.properties"

        val vcsLibHome = File(System.getProperty("user.home"), ".vcsLib")
    }

    val conf: Conf

    init {
        val file = if (isRootProject()) {
            File(target.rootDir, confFileName)
        } else {
            File(target.projectDir, confFileName)
        }
        val isNeedReset = !file.exists() || file.length() == 0.toLong()
        conf = Conf(file)
        if (isNeedReset) {
            if (isRootProject()) {
//                if (conf.getConf(ConfKey.repoUri).isEmpty()
//                        && vcsLibHome.absolutePath != conf.getConf(ConfKey.vcsLibHome)
//                        && conf.getConf(ConfKey.repoUsername).isEmpty()
//                        && conf.getConf(ConfKey.repoPassword).isEmpty()
//                        && conf.getConf(ConfKey.repoType).isEmpty())
                conf.resetBaseConf().apply()
            } else {
//                var b_0 = false
//                var b_1 = false
//                if (conf.getConf(ConfKey.repoUri).isEmpty()
//                        && vcsLibHome.absolutePath != conf.getConf(ConfKey.vcsLibHome)
//                        && conf.getConf(ConfKey.repoUsername).isEmpty()
//                        && conf.getConf(ConfKey.repoPassword).isEmpty()
//                        && conf.getConf(ConfKey.repoType).isEmpty()) {
//                    conf.resetBaseConf()
//                    b_0 = true
//                }
//                if (conf.getConf(ConfKey.mavenGroupId).isEmpty()
//                        && conf.getConf(ConfKey.mavenArtifactId).isEmpty()
//                        && conf.getConf(ConfKey.mavenVersionName).isEmpty()) {
//                    conf.resetMavenConf()
//                    b_1 = true
//                }
//                if (b_0 || b_1)
//                    conf.apply()
                conf.resetBaseConf().resetMavenConf().apply()
            }
        }
    }

    fun isRootProject(): Boolean {
        return target.projectDir.absoluteFile.absolutePath == target.rootDir.absoluteFile.absolutePath
    }

}