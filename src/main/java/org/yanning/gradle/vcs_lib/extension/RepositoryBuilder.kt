package org.yanning.gradle.vcs_lib.extension

import org.yanning.gradle.vcs_lib.core.VCSLibConf

class RepositoryBuilder {

    private var vcsLibConf: VCSLibConf? = null

    fun setVcsLibConf(vcsLibConf: VCSLibConf):RepositoryBuilder {
        this.vcsLibConf = vcsLibConf
        return this;
    }

    fun build(): Repository? {
        if (vcsLibConf == null) return null
        if (vcsLibConf!!.vcsUri.isEmpty()) return null
        val repository: Repository
        if (VcsType.git.isThis(vcsLibConf!!.vcsUri)) {
            repository = RepositoryGit(vcsLibConf)
        } else if (VcsType.svn.isThis(vcsLibConf!!.vcsUri)) {
            repository = RepositorySvn(vcsLibConf)
        } else {
            repository = RepositoryGit(vcsLibConf)
        }
        return repository
    }

}
