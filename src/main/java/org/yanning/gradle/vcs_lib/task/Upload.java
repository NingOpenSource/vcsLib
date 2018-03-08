package org.yanning.gradle.vcs_lib.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.yanning.gradle.vcs_lib.extension.App;

/**
 * 1,上传到本地仓库
 * <p>
 * 2,从远程仓库更新到本地仓库
 * <p>
 * 3，本地仓库提交到远程仓库
 * <p>
 * 4，完成lib的上传
 */
public class Upload extends DefaultTask {
    private App app;

    public void bindApp(App app) {
        this.app = app;
    }

    @TaskAction
    public void build() {
        if (app != null) {
            System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>" + app.getVcsLibsHome().getAbsolutePath());
            app.getRepositoriesTo().getRepositories().forEach(repository -> {
                System.err.println(">>>>>>>>>" + repository.getUrl());
            });
        }
    }
}
