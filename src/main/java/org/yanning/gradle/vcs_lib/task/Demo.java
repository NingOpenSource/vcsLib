package org.yanning.gradle.vcs_lib.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class Demo extends DefaultTask {
    /**
     *
     */
    @TaskAction
    public void test(){

        System.out.println("test");
    }
}
