package org.yanning.gradle.vcs_lib.extension

import jodd.io.FileUtil
import org.gradle.internal.impldep.org.eclipse.jgit.api.CloneCommand
import org.gradle.internal.impldep.org.eclipse.jgit.api.Git
import org.gradle.internal.impldep.org.eclipse.jgit.diff.DiffEntry
import org.gradle.internal.impldep.org.eclipse.jgit.lib.AnyObjectId
import org.gradle.internal.impldep.org.eclipse.jgit.transport.CredentialsProvider
import org.gradle.internal.impldep.org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.gradle.api.logging.Logging
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

    private var git: Git = if (hasCheckout()) {
        Git.open(outDir)
    } else {
        Git.cloneRepository()
                .setCredentialsProvider(getCredentialsProvider())
                .setCloneAllBranches(false)
                .setCloneSubmodules(false)
                .setDirectory(outDir)
                .setProgressMonitor(newProgressMonitor())
//                .setCallback(object : CloneCommand.Callback {
//                    override fun initializedSubmodules(submodules: Collection<String>) {
//                        submodules.forEach {
//                            log("initializedSubmodules: $it")
//                        }
//                    }
//
//                    override fun cloningSubmodule(path: String) {
//                        log("cloningSubmodule: $path")
//                    }
//
//                    override fun checkingOut(commit: AnyObjectId, path: String) {
//                        log("checkingOut: ${commit.name} -> $path")
//                    }
//                })
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
            log("update: end, error=${e.localizedMessage}")
        }
//        Logging.getLogger("vcsLibs").info("update", "from " + conf.uri + " to " + outDir.path)
    }

    private fun commit() {
        val diffEntries = git.diff()
                //                        .setPathFilter(PathFilterGroup.createFromStrings(outDir().getPath()))
                .setShowNameAndStatusOnly(true).call()
        if (diffEntries == null || diffEntries.size == 0) {
            //                    throw new RuntimeException("提交的文件内容都没有被修改，不能提交");
            return
        }
        //被修改过的文件
        val updateFiles = ArrayList<String>()
        var changeType: DiffEntry.ChangeType
        for (entry in diffEntries) {
            changeType = entry.changeType
            when (changeType) {
                DiffEntry.ChangeType.ADD, DiffEntry.ChangeType.COPY, DiffEntry.ChangeType.RENAME, DiffEntry.ChangeType.MODIFY -> updateFiles.add(entry.newPath)
                DiffEntry.ChangeType.DELETE -> updateFiles.add(entry.oldPath)
            }
        }
        //将文件提交到git仓库中，并返回本次提交的版本号
        //1、将工作区的内容更新到暂存区
        val addCmd = git.add()
        for (file in updateFiles) {
            addCmd.addFilepattern(file)
        }
        addCmd.call()
        git.add().addFilepattern("*").call()
        git.commit().setMessage("upload new version:" + SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())).call()
    }

    override fun upload() {
        git.push().setProgressMonitor(newProgressMonitor()).setCredentialsProvider(getCredentialsProvider()).setForce(false).call()
    }

//    override fun vcsType(): VcsType =VcsType.git

}