package org.yanning.gradle.vcs_lib.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class GUI extends DefaultTask {

    @TaskAction
    public void launchGui(){
        new Thread(){
            @Override
            public void run() {
                super.run();
//                App.launch(CustomApplication.class);
            }
        }.start();
    }
}
