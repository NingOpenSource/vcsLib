package org.yanning.gradle.vcs_lib.extension;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private File vcsLibsHome = new File(System.getProperty("user.home"), ".vcsLib") {
        {
            setVcsLibsHome(this);
        }
    };
    public static final String KEY_VCS_LIBS_HOME = "VCS_LIBS_HOME";

    public File getVcsLibsHome() {
        return vcsLibsHome;
    }

    public void setVcsLibsHome(File vcsLibsHome) {
        this.vcsLibsHome = vcsLibsHome;
        System.getProperties().put(KEY_VCS_LIBS_HOME, vcsLibsHome.getAbsolutePath());
    }
}
