package org.yanning.gradle.vcs_lib.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.yanning.gradle.vcs_lib.extension.App;
import org.yanning.gradle.vcs_lib.utils.Log;

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
        app.controller.bindUpload(this);
    }

    @TaskAction
    public void doUpload() {
        if (app != null) {
            app.getRepositoriesTo().getRepositories().forEach(repository -> {
                Log.out("start commit...");
                repository.commit();
                Log.out("complete commit. ");
                Log.out("start upload..." + " [from " + repository.outDir().getPath() + " to " + repository.getUrl() + "]");
                repository.upload();
                Log.out("complete doUpload.");
            });
        }
    }
}
