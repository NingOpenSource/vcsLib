package org.yanning.gradle.vcs_lib.extension

import org.yanning.gradle.vcs_lib.core.AppConfig
import org.yanning.gradle.vcs_lib.core.RepoConfig
import org.yanning.gradle.vcs_lib.utils.Log

class RepoHelper(private val conf: RepoConfig) {
    private fun isSVN(uri: String?): Boolean {
        if (uri == null) return false
        if (uri.isNullOrEmpty()) return false
        if (uri.startsWith("svn://")) return true
        return false
    }


    private fun isGIT(uri: String?): Boolean {
        if (uri == null) return false
        if (uri.isNullOrEmpty()) return false
        if (uri.endsWith(".git") && (
                        uri.startsWith("https://")
                                || uri.startsWith("http://")
                                || uri.contains("@"))) return true
        return false
    }

    fun connectRepo(): Repo? {
        if (conf.uri.isEmpty()) {
            Log.err("Please config \"uri\":\"${conf.uri}\" in ${AppConfig.confFileName}")
            return null
        }
        return when {
            isSVN(conf.uri) -> {
                RepoSvn(conf)
            }
            isGIT(conf.uri) -> {
                RepoGit(conf)
            }
            else -> {
                null
            }
        }
    }

}
