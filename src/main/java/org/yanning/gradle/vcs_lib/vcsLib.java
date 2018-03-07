package org.yanning.gradle.vcs_lib;

import org.gradle.api.*;
import org.yanning.gradle.vcs_lib.extension.Config;
import org.yanning.gradle.vcs_lib.task.*;

public class vcsLib implements Plugin<Project> {
    public static final String TASK_vcsLibInit = "vcsLibsInit";
    public static final String TASK_vcsLib = "vcsLibs";
    public static final String TASK_vcsLibsDemo = "vcsLibsDemo";
    public static final String TASK_vcsLibsBuild = "vcsLibsBuild";
    public static final String TASK_vcsLibsUpdate = "vcsLibsUpdate";
    public static final String TASK_vcsLibsUpload = "vcsLibsUpload";

    public static void main(String... strings) {
        System.out.print("ok");
    }

    @Override
    public void apply(Project target) {
//        target.getExtensions().add(EXT_vcsLib, config = new Config());
        target.getTasksByName("assemble", false).iterator().next().doLast(task ->
                System.err.println("success....................."));
        target.getTasks().create(TASK_vcsLib, MAIN.class, main -> {
            main.setUrl("svn//127.0.0.1/demo");

        });
        target.getTasks().create(TASK_vcsLibsDemo, Demo.class);
        target.getTasks().create(TASK_vcsLibsBuild, Build.class);
        target.getTasks().create(TASK_vcsLibsUpdate, Update.class);
        target.getTasks().create(TASK_vcsLibsUpload, Upload.class);
    }
}