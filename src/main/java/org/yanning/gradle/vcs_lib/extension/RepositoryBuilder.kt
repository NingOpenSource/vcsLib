package org.yanning.gradle.vcs_lib.extension

import org.yanning.gradle.vcs_lib.core.AppConfig
import org.yanning.gradle.vcs_lib.core.Conf
import org.yanning.gradle.vcs_lib.core.ConfKey
import org.yanning.gradle.vcs_lib.utils.Log

class RepositoryBuilder(
        private val conf: Conf) {

    fun build(): Repository? {
        if (conf.getConf(ConfKey.repoUri).isEmpty()) {
            Log.err("Please config '${ConfKey.repoUri.name}' in ${AppConfig.confFileName}")
            return null
        }
        val repository: Repository
        if (conf.getConf(ConfKey.repoType).isNotEmpty()) {
            if (VcsType.git.name.equals(conf.getConf(ConfKey.repoType))){
                repository = RepositoryGit(conf)
            }else if (VcsType.svn.name.equals(conf.getConf(ConfKey.repoType))){
                repository = RepositorySvn(conf)
            }else{
                repository = RepositorySvn(conf)
            }
        } else if (VcsType.git.isThis(conf.getConf(ConfKey.repoUri))) {
            repository = RepositoryGit(conf)
        } else if (VcsType.svn.isThis(conf.getConf(ConfKey.repoUri))) {
            repository = RepositorySvn(conf)
        } else {
            repository = RepositorySvn(conf)
        }
        return repository
    }

}
