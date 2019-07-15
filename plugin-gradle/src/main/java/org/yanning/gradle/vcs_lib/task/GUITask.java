package org.yanning.gradle.vcs_lib.task;

import javafx.application.Application;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import org.gradle.internal.impldep.org.mozilla.javascript.tools.debugger.Dim;
import org.gradle.internal.impldep.org.mozilla.javascript.tools.debugger.SwingGui;
import org.yanning.gradle.vcs_lib.gui.ConsoleView;
import org.yanning.gradle.vcs_lib.gui.CustomApplication;
import org.yanning.gradle.vcs_lib.utils.Log;
import tornadofx.App;

import javax.inject.Inject;
import javax.swing.*;

public class GUITask extends DefaultTask {
    private final Project target;

    @Inject
    public GUITask(Project target) {
        this.target = target;
    }

    @TaskAction
    public void launchGui() {
//        target.javaexec(javaExecSpec -> {
//            Log.out("try to start gui application[" + ConsoleView.class.getCanonicalName() + "]...");
////            String jarPath=getClass().getResource("").getFile();
//
//            try {
//                Log.out("config classPath[" + getClass().getResource("").getFile()+"]");
//                javaExecSpec.classpath(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
//                javaExecSpec.setMain(ConsoleView.class.getCanonicalName());
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.err(e.getLocalizedMessage());
//            }
//            Log.out("start gui application success!");
//        });
        new Thread(){
            @Override
            public void run() {
                super.run();
//                new SwingGui(new Dim(),"test").setVisible(true);
//                SwingUtilities.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        App.launch(CustomApplication.class);
//                    }
//                });
                App.launch(CustomApplication.class);
            }
        }.start();

    }
}
