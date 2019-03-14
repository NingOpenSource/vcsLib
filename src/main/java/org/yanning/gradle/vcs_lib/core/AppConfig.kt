package org.yanning.gradle.vcs_lib.core

import org.gradle.api.Project
import java.io.File

class AppConfig(private val target: Project) {
    companion object {
        const val confFileName = "vcsLib.properties"

        val vcsLibHome = getDefaultVCSLibHome()

        private fun getDefaultVCSLibHome(): File {
            return File(System.getProperty("user.home"), ".vcsLib");
        }
    }

    val conf: Conf
    init {
        if (isRootProject()) {
            conf = Conf(File(target.rootDir, confFileName))
            conf.setConf(ConfKey.vcsLibHome, vcsLibHome.absolutePath)
            if (conf.getConf(ConfKey.repoUri).isEmpty()) {
                conf.resetRepoConf().apply()
            }
        } else {
            conf = Conf(File(target.projectDir, confFileName))
            conf.setConf(ConfKey.vcsLibHome, vcsLibHome.absolutePath)
            if (conf.getConf(ConfKey.repoUri).isEmpty()) {
                conf.resetRepoConf()
            }
            if (conf.getConf(ConfKey.mavenGroupId).isEmpty()) {
                conf.resetMavenConf()
            }
            conf.apply()
        }
    }

    fun isRootProject(): Boolean {
        return target.projectDir.absoluteFile.absolutePath == target.rootDir.absoluteFile.absolutePath
    }

}