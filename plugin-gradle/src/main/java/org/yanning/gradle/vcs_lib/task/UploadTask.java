package org.yanning.gradle.vcs_lib.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.yanning.gradle.vcs_lib.core.Conf;
import org.yanning.gradle.vcs_lib.core.ConfKey;
import org.yanning.gradle.vcs_lib.extension.Repository;
import org.yanning.gradle.vcs_lib.utils.Log;

import javax.inject.Inject;

public class UploadTask extends DefaultTask {
    private final Repository repo;
    private final Conf conf;

    @Inject
    public UploadTask(Repository repo, Conf conf) {
        this.repo = repo;
        this.conf = conf;
    }
    @TaskAction
    public void doAction(){
        //上传成功，开始同步vcs仓库
        Log.out("start update...");
        repo.update();
        Log.out("start commit...");
        repo.commit();
        Log.out("complete commit. ");
        Log.out("start upload..." + " [from " + repo.outDir() + " to " + conf.getConf(ConfKey.repoUri) + "]");
        repo.upload();
        Log.out("complete doUpload.");
    }
}
