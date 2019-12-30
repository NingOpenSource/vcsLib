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
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class RepoSvn(conf: RepoConfig) : Repo(conf) {
    private fun log(text: String) {
        println("svn(${conf.uri}) -> $text")
    }
    private fun err(text: String) {
        System.err.println("svn(${conf.uri}) -> $text")
    }
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
            } catch (e: Exception) {
                e.printStackTrace()
                err("check: error=${e.localizedMessage}")
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
            log("update: start")
            val svnurl = SVNURL.parseURIEncoded(conf.uri)
            if (!outDir.exists()) {//不存在的目录需要进行检出
                val svnUpdateClient = newClientManager().updateClient
                svnUpdateClient.doCheckout(svnurl, outDir, SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY, false)
            } else {
                val svnUpdateClient = newClientManager().updateClient
                svnUpdateClient.doUpdate(outDir, SVNRevision.HEAD, SVNDepth.INFINITY, false, true)
            }
            log("update: success")
        } catch (e: Exception) {
            err("update: error=${e.localizedMessage}")
        }
    }

    override fun upload() {
        update()
        try {
            log("upload: start")
            val svnCommitClient = newClientManager().commitClient
            svnCommitClient.doCommit(
                    arrayOf(outDir),
                    true,
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), null, null, false, false, SVNDepth.INFINITY)
            log("upload: success")
        } catch (e: Exception) {
            err("upload: error=${e.localizedMessage}")
        }
    }

//    override fun vcsType(): VcsType =VcsType.svn
}