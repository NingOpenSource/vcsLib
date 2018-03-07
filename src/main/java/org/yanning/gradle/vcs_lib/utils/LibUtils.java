package org.yanning.gradle.vcs_lib.utils;

import org.yanning.gradle.vcs_lib.URL;

import java.util.List;

public abstract class LibUtils {
    private URL url;

    public LibUtils(URL url) {
        this.url = url;
    }


    /**
     * 是否有新版本
     *
     * @return
     */
    abstract boolean hasNewVersion();

    /**
     * 远程仓库中已经有的库
     *
     * @return
     */
    abstract List<URL> list();

    /**
     * 本地已经有的库
     *
     * @return
     */
    abstract List<URL> localList();


    /**
     * 上传库
     */
    abstract void upload();

    /**
     * 下载库
     */
    abstract void download();

}
