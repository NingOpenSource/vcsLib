package org.yanning.gradle.vcs_lib.extension

import org.yanning.gradle.vcs_lib.core.RepoConfig
import java.io.File
import java.io.UnsupportedEncodingException

abstract class Repo internal constructor(internal val conf: RepoConfig) {
    val outDir: File=RepoConfig.outDir(conf.uri)

    abstract fun hasCheckout(): Boolean

    abstract fun update()

    abstract fun upload()

//    abstract fun vcsType(): VcsType

}
