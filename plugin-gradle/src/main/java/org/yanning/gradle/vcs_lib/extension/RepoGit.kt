package org.yanning.gradle.vcs_lib.extension

import jodd.io.FileUtil
import org.gradle.internal.impldep.org.eclipse.jgit.api.CloneCommand
import org.gradle.internal.impldep.org.eclipse.jgit.api.Git
import org.gradle.internal.impldep.org.eclipse.jgit.diff.DiffEntry
import org.gradle.internal.impldep.org.eclipse.jgit.lib.AnyObjectId
import org.gradle.internal.impldep.org.eclipse.jgit.transport.CredentialsProvider
import org.gradle.internal.impldep.org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.gradle.api.logging.Logging
import org.yanning.gradle.vcs_lib.core.Conf
import org.yanning.gradle.vcs_lib.core.ConfKey
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class RepoGit(conf: Conf) : Repo(conf) {
    private var git: Git = if (hasCheckout()){
        Git.open(outDir())
    }else{
        Git.cloneRepository()
                .setCredentialsProvider(getCredentialsProvider())
                .setCloneAllBranches(false)
                .setCloneSubmodules(false)
                .setDirectory(outDir())
                .setCallback(object : CloneCommand.Callback {
                    override fun initializedSubmodules(submodules: Collection<String>) {

                    }

                    override fun cloningSubmodule(path: String) {

                    }

                    override fun checkingOut(commit: AnyObjectId, path: String) {

                    }
                })
                .setURI(conf.getConf(ConfKey.repoUri))
                .call()
    }

    private fun getCredentialsProvider(): CredentialsProvider {
        return UsernamePasswordCredentialsProvider(conf.getConf(ConfKey.repoUsername), conf.getConf(ConfKey.repoPassword))
    }
    private fun isGitRepository(): Boolean {
        return FileUtil.isExistingFolder(File(outDir(), ".git"))
    }

    override fun hasCheckout(): Boolean {
        return FileUtil.isExistingFolder(File(outDir(), ".git"))
    }

    override fun update() {
        git.pull().setTransportConfigCallback { transport ->

        }.setCredentialsProvider(getCredentialsProvider()).call()
        Logging.getLogger("vcsLibs").info("update", "from " + conf.getConf(ConfKey.repoUri) + " to " + outDir().path)
        System.out.println("vcsLibs:update --> from " + conf.getConf(ConfKey.repoUri) + " to " + outDir().path)
    }

    private fun commit(){
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
        git.push().setTransportConfigCallback({ transport -> }).setCredentialsProvider(getCredentialsProvider()).setForce(false).call()
    }

    override fun vcsType(): VcsType =VcsType.git

}