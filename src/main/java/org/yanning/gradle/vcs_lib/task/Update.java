//package org.yanning.gradle.vcs_lib.task;
//
//import org.gradle.api.Action;
//import org.gradle.api.DefaultTask;
//import org.gradle.api.tasks.TaskAction;
//import org.yanning.gradle.vcs_lib.extension.App;
//import org.yanning.gradle.vcs_lib.utils.Log;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 从远程库更新
// */
//public class Update extends DefaultTask {
//    private App app;
//
//    public void bindApp(App app) {
//        this.app = app;
//        app.controller.bindUpdate(this);
//    }
//
//    private Action<List<File>> outDirAction;
//
//    public void bindOutDirAction(Action<List<File>> outDirAction) {
//        this.outDirAction = outDirAction;
//    }
//
//    @TaskAction
//    public void doUpdate() {
//        if (app != null) {
//            List<File> dirs=new ArrayList<>();
//            app.getRepositoriesFrom().getRepositories().forEach(repository -> {
//                Log.out("start update..." + " [from " + repository.getUrl() + " to " + repository.outDir().getPath() + "]");
//                repository.update();
//                Log.out("complete update.");
//                dirs.add(repository.outDir());
//            });
//
//            if (outDirAction != null) {
//                outDirAction.execute(dirs);
//            }
//        }
//    }
//}
