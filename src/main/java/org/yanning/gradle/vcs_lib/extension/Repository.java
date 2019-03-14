package org.yanning.gradle.vcs_lib.extension;

import org.yanning.gradle.vcs_lib.core.Conf;
import org.yanning.gradle.vcs_lib.core.ConfKey;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public abstract class Repository {
    private Conf conf;
    private File outDir;

    Repository(Conf conf) {
        this.conf = conf;
    }

    Conf getConf() {
        return conf;
    }

    public File outDir() {
        if (outDir == null) {
//            File rootDir = FileUtils.createDir(new File(System.getProperties().getProperty(App.KEY_VCS_LIBS_HOME)));
//            try {
//                outDir = FileUtils.createDir(new File(rootDir,
//                        URLEncoder.encode(url, "utf-8")));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
            try {
                outDir = new File(new File(conf.getConf(ConfKey.vcsLibHome)),
                        URLEncoder.encode(conf.getConf(ConfKey.repoUri), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return outDir;
    }

    public abstract boolean hasCheckout();

    public abstract void update();

    public abstract void commit();

    public abstract void upload();

    public abstract VcsType vcsType();

}
