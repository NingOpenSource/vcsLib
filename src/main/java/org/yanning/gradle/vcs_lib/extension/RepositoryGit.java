package org.yanning.gradle.vcs_lib.extension;

import org.gradle.api.logging.Logging;

public class RepositoryGit extends Repository {
    public boolean isUseSSH = false;
    private String username;
    private String password;

    public void password(String password) {
        this.password = password;
    }

    public void username(String username) {
        this.username = username;
    }


    @Override
    public void update() {
        if (getUrl() == null) return;

        Logging.getLogger("vcsLibs").info("update", "from " + getUrl() + " to " + outDir().getPath());
        System.out.println("vcsLibs:update --> from " + getUrl() + " to " + outDir().getPath());
    }

    @Override
    public void commit() {
        if (getUrl() == null) return;

    }

    @Override
    public void upload() {
        if (getUrl() == null) return;

    }

    @Override
    public VcsType vcsType() {
        return VcsType.git;
    }
}
