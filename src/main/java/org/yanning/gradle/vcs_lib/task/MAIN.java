package org.yanning.gradle.vcs_lib.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.GradleBuild;
import org.gradle.api.tasks.TaskAction;

public class MAIN extends DefaultTask {
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @TaskAction
    void test() {
        System.err.println("url-->" + url);
    }
}
