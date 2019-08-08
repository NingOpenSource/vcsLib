package org.yanning.gradle.vcs_lib.core.ext

import jodd.io.FileUtil
import org.eclipse.jgit.api.CloneCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.AnyObjectId
import org.yanning.gradle.vcs_lib.core.ConfKey
import java.io.File

class Repo_git(dir: String, uri: String) {
    private val dirFile = File(dir)

    fun hasCreated(): Boolean = FileUtil.isExistingFolder(File(dirFile, ".git"))

    private fun clone() {
    }

    fun pull() {
        if (hasCreated()) {


        } else {
            clone()
        }
    }

    fun push() {

    }

    fun commit() {

    }
}