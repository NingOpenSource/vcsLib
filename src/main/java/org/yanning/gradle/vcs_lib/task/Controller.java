package org.yanning.gradle.vcs_lib.task;

import org.yanning.gradle.vcs_lib.task.Build;
import org.yanning.gradle.vcs_lib.task.Update;
import org.yanning.gradle.vcs_lib.task.Upload;

public class Controller {
    private Build build;
    private Update update;
    private Upload upload;

    protected void bindBuild(Build build) {
        this.build = build;
    }

    protected void bindUpdate(Update update) {
        this.update = update;
    }

    protected void bindUpload(Upload upload) {
        this.upload = upload;
    }

    public void doBuild() {
        if (build != null) {
            build.doBuild();
        }
    }

    public void doUpload() {
        if (upload != null) {
            upload.doUpload();
        }
    }

    public void doUpdate() {
        if (update != null) {
            update.doUpdate();
        }
    }
}
