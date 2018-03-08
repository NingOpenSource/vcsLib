package org.yanning.gradle.vcs_lib.extension;

import org.gradle.api.logging.Logging;
import org.yanning.gradle.vcs_lib.utils.FileUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RepositorySvn extends Repository {
    private String username;
    private String password;

    public void password(String password) {
        this.password = password;
    }

    public void username(String username) {
        this.username = username;
    }

    @Override
    void update() {
if (getUrl()==null)return;
        Logging.getLogger("vcsLibs").info("update", "from " + getUrl() + " to " + outDir().getPath());
        System.out.println("vcsLibs:update --> from " + getUrl() + " to " + outDir().getPath());
    }

    @Override
    void commit() {
        if (getUrl()==null)return;

    }

    @Override
    void upload() {
        if (getUrl()==null)return;

    }

    @Override
    VcsType vcsType() {
        return VcsType.svn;
    }
}
