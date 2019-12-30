package org.yanning.gradle.vcs_lib.extension

import jodd.io.FileUtil
import org.gradle.internal.impldep.org.eclipse.jgit.api.Git
import org.gradle.internal.impldep.org.eclipse.jgit.transport.CredentialsProvider
import org.gradle.internal.impldep.org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.gradle.internal.impldep.org.eclipse.jgit.lib.BatchingProgressMonitor
import org.gradle.internal.impldep.org.eclipse.jgit.lib.ProgressMonitor
import org.yanning.gradle.vcs_lib.core.RepoConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class RepoGit(conf: RepoConfig) : Repo(conf) {
    private fun log(text: String) {
        println("git(${conf.uri}) -> $text")
    }
    private fun err(text: String) {
        System.err.println("git(${conf.uri}) -> $text")
    }

    private var git: Git = if (hasCheckout()) {
        Git.open(outDir)
    } else {
        Git.cloneRepository()
                .setCredentialsProvider(getCredentialsProvider())
                .setCloneAllBranches(false)
                .setCloneSubmodules(false)
                .setDirectory(outDir)
                .setProgressMonitor(newProgressMonitor())
                .setURI(conf.uri)
                .call()
    }

    private fun getCredentialsProvider(): CredentialsProvider {
        return UsernamePasswordCredentialsProvider(conf.user, conf.passwd)
    }

    private fun newProgressMonitor(): ProgressMonitor = object : BatchingProgressMonitor() {

        override fun onUpdate(taskName: String?, workCurr: Int) {
            onUpdate(taskName, workCurr, workCurr, 100)
        }

        override fun onUpdate(taskName: String?, workCurr: Int, workTotal: Int, percentDone: Int) {
            log("$taskName: $percentDone%")
        }

        override fun onEndTask(taskName: String?, workCurr: Int) {
            onEndTask(taskName, workCurr, workCurr, 100)
        }

        override fun onEndTask(taskName: String?, workCurr: Int, workTotal: Int, percentDone: Int) {
            log("$taskName: $percentDone%")
        }
    }

    private fun isGitRepository(): Boolean {
        return FileUtil.isExistingFolder(File(outDir, ".git"))
    }

    override fun hasCheckout(): Boolean {
        return FileUtil.isExistingFolder(File(outDir, ".git"))
    }

    override fun update() {
        try {
            log("update: start")
            git.pull().setProgressMonitor(newProgressMonitor()).setCredentialsProvider(getCredentialsProvider()).call()
            log("update: end")
        } catch (e: Exception) {
            err("update: error=${e.localizedMessage}")
        }
//        Logging.getLogger("vcsLibs").info("update", "from " + conf.uri + " to " + outDir.path)
    }

    private fun commit() {
        try {
            log("commit: start")
            val addCMD = git.add()
            git.status().call().also { status ->
                status.untracked.forEach {
                    log("status: untracked=$it")
                    addCMD.addFilepattern(it)
                }
                status.untrackedFolders.forEach {
                    log("status: untrackedFolders=$it")
                    addCMD.addFilepattern(it)
                }
            }
            addCMD.addFilepattern("*").call()
            git.commit().setAll(true).setMessage("upload new version:" + SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())).call()
            log("commit: end")
        } catch (e: Exception) {
            err("commit: error=${e.localizedMessage}")
        }
    }

    override fun upload() {
        update()
        commit()
        try {
            log("upload: start")
            git.push().setProgressMonitor(newProgressMonitor()).setCredentialsProvider(getCredentialsProvider()).setForce(false).call()
            log("upload: end")
        } catch (e: Exception) {
            err("upload: error=${e.localizedMessage}")
        }
    }
//    override fun vcsType(): VcsType =VcsType.git

}