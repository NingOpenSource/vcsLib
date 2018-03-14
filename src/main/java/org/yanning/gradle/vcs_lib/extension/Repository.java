package org.yanning.gradle.vcs_lib.extension;

import org.gradle.api.logging.Logging;
import org.yanning.gradle.vcs_lib.utils.FileUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.UUID;

public abstract class Repository {


    private String url;
    private File outDir;


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
                outDir=new File(new File(System.getProperties().getProperty(App.KEY_VCS_LIB_HOME)),
                        URLEncoder.encode(url, "utf-8"));
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

    public void url(String url){
        setUrl(url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }


}
