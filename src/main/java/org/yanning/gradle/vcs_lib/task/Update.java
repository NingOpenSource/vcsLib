package org.yanning.gradle.vcs_lib.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.yanning.gradle.vcs_lib.extension.App;

/**
 * 从远程库更新
 */
public class Update extends DefaultTask {
    private App app;

    public void bindApp(App app) {
        this.app = app;
    }
    @TaskAction
    public void build() {
        if (app != null) {
            System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>" + app.getVcsLibsHome().getAbsolutePath());
            app.getRepositoriesFrom().getRepositories().forEach(repository -> {
                System.err.println(">>>>>>>>>" + repository.getUrl());
            });
        }
    }
}
