package org.yanning.gradle.vcs_lib.core

import org.yanning.lib.LogFormat.LogFormat
import java.io.File
import java.net.URI

class VCS {

    fun update(repoPath: String, repoUri: String,
               onLog: (message: String) -> Unit? = { message -> println(message) },
               onSuccess: () -> Unit,
               onError: (error: ERROR) -> Unit) {
        checkPathAndUri(repoPath, repoUri,onError) {type->
            if (type==RepoType.git){

            }else{

            }
        }
    }


    fun upload(repoPath: String, repoUri: String,
               onLog: (message: String) -> Unit? = { message -> println(message) },
               onSuccess: () -> Unit,
               onError: (error: ERROR) -> Unit) {
        checkPathAndUri(repoPath, repoUri,onError) {

        }
    }


    private fun checkPathAndUri(path: String, uri: String, onError: (error: ERROR) -> Unit, next: (type:RepoType) -> Unit) {
        val path_file = File(path)
        if (!path_file.exists()) {
            onError(ERROR.repo_path_invalid)
            return
        }
        val dirType = RepoType.values().find { type -> type.isDir(path_file) }
        if (dirType == null) {
            onError(ERROR.repo_path_invalid)
            return
        }
        val uriType = RepoType.values().find { type -> type.isUri(uri) }
        if (uriType == null) {
            onError(ERROR.repo_uri_invalid)
            return
        }
        if (uriType != dirType) {
            onError(ERROR.repo_uri_invalid)
            return
        }
        next()
    }


    private fun logI(message: String) {
        LogFormat.newLog().tag("vcs:info").addLog(message).out(LogFormat.Type.info)
    }

    private fun logE(message: String) {
        LogFormat.newLog().tag("vcs:error").addLog(message).out(LogFormat.Type.error)
    }


    enum class ERROR {
        ok, error_unknow, repo_path_invalid, repo_uri_invalid, repo_uri_is_not_match_of_path, repo_auth_fail, repo_connect_fail
    }
}

internal class VCS_git {

}

internal class VCS_svn {

}