package org.yanning.gradle.vcs_lib.core

import org.gradle.api.Project
import org.gradle.internal.impldep.com.google.gson.Gson
import org.gradle.internal.impldep.com.google.gson.GsonBuilder
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class AppConfig(private val target: Project) {
    companion object {
        private const val confFileName = "vcsLib.conf"
        private const val uploadConfFileName = "vcsLibUpload.conf"
    }

    private var confFile: File?
    private var uploadConfFile: File? = null
    var vcsLibHome = getDefaultVCSLibHome()

    init {
        if (isRootProject()) {
            confFile = File(target.projectDir, confFileName)
        } else {
            confFile = File(target.projectDir, confFileName)
            if (!confFile?.exists()!!) {
                confFile = File(target.rootDir, uploadConfFileName)
            }
            uploadConfFile = File(target.projectDir, uploadConfFileName)
            if (!uploadConfFile?.exists()!!) {
                setUploadConf(VCSLibUploadConf("", "", ""))
            }
        }
        if (!confFile?.exists()!!) {
            setConf(VCSLibConf(vcsLibHome.toURI().toString(), "", "", ""))
        }
    }

    fun getConf(): VCSLibConf? {
        if (confFile == null) return null
        return Gson().fromJson<VCSLibConf>(FileReader(confFile), VCSLibConf::class.java)
    }

    fun getUploadConf(): VCSLibUploadConf? {
        if (uploadConfFile == null) return null
        return Gson().fromJson<VCSLibUploadConf>(FileReader(uploadConfFile), VCSLibUploadConf::class.java)
    }

    fun setConf(conf: VCSLibConf) {
        GsonBuilder().setPrettyPrinting().create().toJson(conf, FileWriter(confFile))
        if (conf.vcsLibHome.isEmpty()) return
        vcsLibHome = File(conf.vcsLibHome)
    }

    fun setUploadConf(conf: VCSLibUploadConf) {
        GsonBuilder().setPrettyPrinting().create().toJson(conf, FileWriter(uploadConfFile))
    }

    private fun isRootProject(): Boolean {
        return target.projectDir.absoluteFile == target.rootDir.absoluteFile
    }

    private fun getDefaultVCSLibHome(): File {
        return File(System.getProperty("user.home"), ".vcsLib");
    }
}