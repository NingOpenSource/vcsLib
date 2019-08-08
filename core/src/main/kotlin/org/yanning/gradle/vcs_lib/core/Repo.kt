package org.yanning.gradle.vcs_lib.core

import java.io.File

enum class RepoType(val isDir:(dir: File)->Boolean,
                    val isUri:(uri: String)->Boolean) {
    git(isDir = {dir->File(dir,".git").exists()},
            isUri = {uri-> uri.isNotEmpty() &&(uri.startsWith("git@")||uri.endsWith(".git"))}),
    svn(isDir = {dir->File(dir,".git").exists()},
            isUri = {uri-> uri.isNotEmpty() &&uri.startsWith("svn://")})

}