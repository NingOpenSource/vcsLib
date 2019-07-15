package org.yanning.gradle.vcs_lib.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.yanning.gradle.vcs_lib.core.Conf;
import org.yanning.gradle.vcs_lib.extension.Repository;
import org.yanning.gradle.vcs_lib.utils.Log;

import javax.inject.Inject;

public class UpdateTask extends DefaultTask {
    private final Repository repo;
    private final Conf conf;

    @Inject
    public UpdateTask(Repository repo, Conf conf) {
        this.repo = repo;
        this.conf = conf;
    }

    @TaskAction
    public void doAction() {
        if (repo.hasCheckout()) {
            Log.out("repo has updated,you can update repo forced use to 'gradle vcsLibUpdate'!");
        } else {
            Log.out("repo update start...");
            repo.update();
            Log.out("repo update end...");
        }
    }
}
