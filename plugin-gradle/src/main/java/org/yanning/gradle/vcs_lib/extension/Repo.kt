package org.yanning.gradle.vcs_lib.extension

import org.yanning.gradle.vcs_lib.core.RepoConfig
import java.io.File
import java.io.UnsupportedEncodingException

abstract class Repo internal constructor(internal val conf: RepoConfig) {
    val outDir: File = RepoConfig.outDir(conf.uri)
    val updateTagFile = File(outDir.parent, ".${outDir.name}")
    /**
     * 在12小时以内不再进行重复的更新操作
     */
    var limitTime = 1000 * 60 * 60 * 12

    abstract fun hasCheckout(): Boolean

    abstract fun update()

    abstract fun upload()

//    abstract fun vcsType(): VcsType
    /**
     * 可防止重复更新操作，加快编译速度
     */
    fun updateWithLimit() {
        if (!updateTagFile.exists()) {
            updateTagFile.createNewFile()
            update()
        } else if (System.currentTimeMillis() - updateTagFile.lastModified() > limitTime) {
            updateTagFile.setLastModified(System.currentTimeMillis())//更新最后修改的时间
            update()
        } else {
            println("Repo(${conf.uri}) has updated!(limit time is 12h, you can update force by 'gradle vcsLibUpdate')")
        }
    }
}
