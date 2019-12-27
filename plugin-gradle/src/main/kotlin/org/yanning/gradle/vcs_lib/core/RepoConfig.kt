package org.yanning.gradle.vcs_lib.core

import org.gradle.internal.impldep.com.google.gson.Gson
import org.gradle.internal.impldep.com.google.gson.GsonBuilder
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class RepoConfig {
    var uri = ""
    var user = ""
    var passwd = ""

    companion object {
        @JvmStatic
        fun outDir(uri: String): File {
            return File(AppConfig.vcsLibHome,
                    URLEncoder.encode(uri, "utf-8"))
        }

        fun loadConfs(file: File): HashMap<String, RepoConfig> {
            if (file.exists()) {
                val result = hashMapOf<String, RepoConfig>()
                Gson().fromJson(file.readText(), Array<RepoConfig>::class.java)
                        .filter {
                            it.uri.isNotEmpty()
                        }.forEach {
                            result.put(it.uri, it)
                        }
                return result
            } else {
                file.createNewFile()
                val confs = arrayOf<RepoConfig>().plus(RepoConfig())
                file.writeText(GsonBuilder().setPrettyPrinting().create().toJson(confs))
                return hashMapOf()
            }
        }
    }
}