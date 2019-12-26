package org.yanning.gradle.vcs_lib.core

import org.gradle.internal.impldep.com.google.gson.Gson
import org.gradle.internal.impldep.com.google.gson.GsonBuilder
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class RepoConfig {
    val uri = ""
    val user = ""
    val passwd = ""

    companion object {
        @JvmStatic
        fun outDir(uri: String): File {
            return File(AppConfig.vcsLibHome,
                    URLEncoder.encode(uri, "utf-8"))
        }

        fun loadConfs(file: File): Array<RepoConfig> {
            if (file.exists()) {
                return Gson().fromJson(file.readText(), Array<RepoConfig>::class.java)
                        .filter {
                            it.uri.isNotEmpty()
                        }.toTypedArray()
            } else {
                file.createNewFile()
                val confs = arrayOf<RepoConfig>()
                file.writeText(GsonBuilder().setPrettyPrinting().create().toJson(confs))
                return confs
            }
        }
    }
}