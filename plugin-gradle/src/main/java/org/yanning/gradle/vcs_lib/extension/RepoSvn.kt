package org.yanning.gradle.vcs_lib.extension

import jodd.io.FileUtil
import org.tmatesoft.svn.core.SVNDepth
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.SVNURL
import org.tmatesoft.svn.core.wc.SVNClientManager
import org.tmatesoft.svn.core.wc.SVNRevision
import org.tmatesoft.svn.core.wc.SVNWCUtil
import org.yanning.gradle.vcs_lib.core.RepoConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class RepoSvn(conf: RepoConfig) : Repo(conf) {
    private val options = SVNWCUtil.createDefaultOptions(true)


    override fun hasCheckout(): Boolean = FileUtil.isExistingFolder(File(outDir, ".svn"))

    private fun commit() {
        checkVersionDirectory(newClientManager(), outDir)
    }

    /**
     * 递归检查不在版本控制的文件，并add到svn
     */
    private fun checkVersionDirectory(clientManager: SVNClientManager, dir: File) {
        if (!SVNWCUtil.isVersionedDirectory(dir)) {
            try {
                clientManager.wcClient.doAdd(arrayOf(dir), true,
                        false, false, SVNDepth.INFINITY, false, false,
                        true)
            } catch (e: SVNException) {
                e.printStackTrace()
            }

        }
        if (dir.isDirectory) {
            for (subDir in dir.listFiles()!!) {
                if (subDir.isDirectory && subDir.name == ".svn") {
                    continue
                }
                checkVersionDirectory(clientManager, subDir)
            }
        }
    }

    private fun newClientManager(): SVNClientManager = SVNClientManager.newInstance(options, conf.user, conf.passwd)

    override fun update() {
        try {
            println("start svn update：${conf.uri}")
            val svnurl = SVNURL.parseURIEncoded(conf.uri)
            if (!outDir.exists()) {//不存在的目录需要进行检出
                val svnUpdateClient = newClientManager().updateClient
                svnUpdateClient.doCheckout(svnurl, outDir, SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY, false)
            } else {
                val svnUpdateClient = newClientManager().updateClient
                svnUpdateClient.doUpdate(outDir, SVNRevision.HEAD, SVNDepth.INFINITY, false, true)
            }
            println("end svn update success: ${conf.uri}")
        } catch (e: SVNException) {
            println("end svn update error: ${conf.uri} , message:${e.errorMessage}")
        }

    }

    override fun upload() {
        update()
        try {
            println("start svn upload：${conf.uri}")
            val svnCommitClient = newClientManager().commitClient
            svnCommitClient.doCommit(
                    arrayOf(outDir),
                    true,
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), null, null, false, false, SVNDepth.INFINITY)
            println("end svn upload success: ${conf.uri}")
        } catch (e: SVNException) {
            println("end svn upload error: ${conf.uri} , message:${e.errorMessage}")
        }
    }

//    override fun vcsType(): VcsType =VcsType.svn
}