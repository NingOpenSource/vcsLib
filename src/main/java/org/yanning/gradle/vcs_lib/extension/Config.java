package org.yanning.gradle.vcs_lib.extension;

import org.yanning.gradle.vcs_lib.URL;

import java.util.List;

public class Config {
    private List<URL> libs;

    public List<URL> getLibs() {
        return libs;
    }

    public void setLibs(List<URL> libs) {
        this.libs = libs;
    }
}
