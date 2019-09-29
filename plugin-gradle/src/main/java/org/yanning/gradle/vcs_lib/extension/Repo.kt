package org.yanning.gradle.vcs_lib.extension

import org.yanning.gradle.vcs_lib.core.AppConfig
import org.yanning.gradle.vcs_lib.core.Conf
import org.yanning.gradle.vcs_lib.core.ConfKey

import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

abstract class Repo internal constructor(internal val conf: Conf) {
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
                outDir = File(File(conf.getConf(ConfKey.vcsLibHome, AppConfig.vcsLibHome.absolutePath)),
                        URLEncoder.encode(conf.getConf(ConfKey.repoUri), "utf-8"))
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
