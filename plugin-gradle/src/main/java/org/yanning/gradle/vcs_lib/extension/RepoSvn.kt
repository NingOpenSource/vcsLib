package org.yanning.gradle.vcs_lib.extension

import jodd.io.FileUtil
import org.tmatesoft.svn.core.SVNDepth
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.SVNURL
import org.tmatesoft.svn.core.wc.SVNClientManager
import org.tmatesoft.svn.core.wc.SVNRevision
import org.tmatesoft.svn.core.wc.SVNWCUtil
import org.yanning.gradle.vcs_lib.core.Conf
import org.yanning.gradle.vcs_lib.core.ConfKey
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class RepoSvn(conf: Conf) : Repo(conf) {
    private val options = SVNWCUtil.createDefaultOptions(true)


    override fun hasCheckout(): Boolean = FileUtil.isExistingFolder(File(outDir(), ".svn"))

    private fun commit() {
        val svnClientManager = SVNClientManager.newInstance(options,conf.getConf(ConfKey.repoUsername),conf.getConf(ConfKey.repoPassword))
        checkVersionDirectory(svnClientManager, outDir())
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


    override fun update() {
        val svnClientManager = SVNClientManager.newInstance(options, conf.getConf(ConfKey.repoUsername),
                conf.getConf(ConfKey.repoPassword))
        val svnurl = SVNURL.parseURIEncoded(conf.getConf(ConfKey.repoUri))
        if (!outDir().exists()) {//不存在的目录需要进行检出
            val svnUpdateClient = svnClientManager.updateClient
            svnUpdateClient.doCheckout(svnurl, outDir(), SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY, false)
        } else {
            val svnUpdateClient = svnClientManager.updateClient
            svnUpdateClient.doUpdate(outDir(), SVNRevision.HEAD, SVNDepth.INFINITY, false, true)
        }
    }

    override fun upload() {
        update()
        val svnClientManager = SVNClientManager.newInstance(options, conf.getConf(ConfKey.repoUsername), conf.getConf(ConfKey.repoPassword))
        val svnCommitClient = svnClientManager.commitClient

        svnCommitClient.doCommit(
                arrayOf(outDir()),
                true,
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), null, null, false, false, SVNDepth.INFINITY)
    }

    override fun vcsType(): VcsType =VcsType.svn
}