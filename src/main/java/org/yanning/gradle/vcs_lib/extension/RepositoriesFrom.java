package org.yanning.gradle.vcs_lib.extension;


import org.gradle.api.Action;
import org.yanning.gradle.vcs_lib.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class RepositoriesFrom {
    private List<Repository> repositories = new ArrayList<>();

    public void vcs(String repository) {
        for (VcsType type : VcsType.values()) {
            if (type.isThis(repository)) {
                switch (type) {
                    case git: {
                        git(repositoryGit -> {
                            repositoryGit.url(repository);
                        });
                    }
                    break;
                    case svn: {
                        svn(repositorySvn -> {
                            repositorySvn.url(repository);
                        });
                    }
                    break;
                    default: {

                    }
                }
                continue;
            }
        }
    }

    public void vcs(String repository,String username,String password) {
        for (VcsType type : VcsType.values()) {
            if (type.isThis(repository)) {
                switch (type) {
                    case git: {
                        git(repositoryGit -> {
                            repositoryGit.url(repository);
                            repositoryGit.username(username);
                            repositoryGit.password(password);
                        });
                    }
                    break;
                    case svn: {
                        svn(repositorySvn -> {
                            repositorySvn.url(repository);
                            repositorySvn.username(username);
                            repositorySvn.password(password);
                        });
                    }
                    break;
                    default: {

                    }
                }
                continue;
            }
        }
    }
    public void vcs(Repository repository) {
        Log.out("add url " + repository.getUrl());
        repositories.add(repository);
    }

    public void git(Action<RepositoryGit> repositoryGitAction) {
        RepositoryGit repository = new RepositoryGit();
        repositoryGitAction.execute(repository);
        vcs(repository);
    }

    public void svn(Action<RepositorySvn> repositorySvnAction) {
        RepositorySvn repository = new RepositorySvn();
        repositorySvnAction.execute(repository);
        vcs(repository);
    }

    public List<Repository> getRepositories() {
        return repositories;
    }


}
