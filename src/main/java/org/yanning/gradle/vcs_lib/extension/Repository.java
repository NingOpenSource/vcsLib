package org.yanning.gradle.vcs_lib.extension;

import org.yanning.gradle.vcs_lib.core.VCSLibConf;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public abstract class Repository {
    private VCSLibConf conf;
    private File outDir;

    public Repository(VCSLibConf conf) {
        this.conf = conf;
    }

    public VCSLibConf getConf() {
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
                outDir=new File(conf.getVcsLibHome(),
                        URLEncoder.encode(conf.getVcsUri(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return outDir;
    }


    public abstract void update();

    public abstract void commit();

    public abstract void upload();

    public abstract VcsType vcsType();

}
