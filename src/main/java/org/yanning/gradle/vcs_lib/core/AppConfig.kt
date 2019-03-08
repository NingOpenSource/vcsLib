package org.yanning.gradle.vcs_lib.core

import java.io.File

object AppConfig {
    fun getDefaultVCSLibPath(): File {
        return File(System.getProperty("user.home"), ".vcsLib");
    }
}