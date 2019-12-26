package org.yanning.gradle.vcs_lib.extension

import org.yanning.gradle.vcs_lib.core.RepoConfig
import java.io.File
import java.io.UnsupportedEncodingException

abstract class Repo internal constructor(internal val conf: RepoConfig) {
    private lateinit var outDir: File

    fun outDir(): File {
        if (outDir == null) {
            //            File rootDir = FileUtils.createDir(new File(System.getProperties().getProperty(App.KEY_VCS_LIBS_HOME)));
            //            try {
            //                outDir = FileUtils.createDir(new File(rootDir,
            //                        URLEncoder.encode(url, "utf-8")));
            //            } catch (UnsupportedEncodingException e) {
            //                e.printStackTrace();
            //            }
            try {
                outDir = RepoConfig.outDir(conf.uri)
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

        }
        return outDir
    }

    abstract fun hasCheckout(): Boolean

    abstract fun update()

    abstract fun upload()

    abstract fun vcsType(): VcsType

}
