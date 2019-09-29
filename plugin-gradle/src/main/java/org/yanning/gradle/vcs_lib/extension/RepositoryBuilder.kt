package org.yanning.gradle.vcs_lib.extension

import org.yanning.gradle.vcs_lib.core.AppConfig
import org.yanning.gradle.vcs_lib.core.Conf
import org.yanning.gradle.vcs_lib.core.ConfKey
import org.yanning.gradle.vcs_lib.utils.Log

class RepositoryBuilder(
        private val conf: Conf) {

    fun build(): Repo? {
        if (conf.getConf(ConfKey.repoUri).isEmpty()) {
            Log.err("Please config '${ConfKey.repoUri.name}' in ${AppConfig.confFileName}")
            return null
        }
        val repo: Repo
        if (conf.getConf(ConfKey.repoType).isNotEmpty()) {
            if (VcsType.git.name.equals(conf.getConf(ConfKey.repoType))){
                repo = RepoGit(conf)
            }else if (VcsType.svn.name.equals(conf.getConf(ConfKey.repoType))){
                repo = RepoSvn(conf)
            }else{
                repo = RepoSvn(conf)
            }
        } else if (VcsType.git.isThis(conf.getConf(ConfKey.repoUri))) {
            repo = RepoGit(conf)
        } else if (VcsType.svn.isThis(conf.getConf(ConfKey.repoUri))) {
            repo = RepoSvn(conf)
        } else {
            repo = RepoSvn(conf)
        }
        return repo
    }

}
