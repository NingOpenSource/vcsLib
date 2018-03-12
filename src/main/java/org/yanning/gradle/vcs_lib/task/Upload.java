package org.yanning.gradle.vcs_lib.task;

import groovy.lang.Closure;
import jodd.io.FileUtil;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskAction;
import org.gradle.internal.impldep.org.apache.maven.project.MavenProject;
import org.yanning.gradle.vcs_lib.extension.App;
import org.yanning.gradle.vcs_lib.extension.RepositoriesTo;
import org.yanning.gradle.vcs_lib.extension.Repository;
import org.yanning.gradle.vcs_lib.utils.Log;
import org.yanning.gradle.vcs_lib.vcsLib;

import java.io.File;
import java.io.IOException;

/**
 * 1,上传到本地仓库
 * <p>
 * 2,从远程仓库更新到本地仓库
 * <p>
 * 3，本地仓库提交到远程仓库
 * <p>
 * 4，完成lib的上传
 */
public class Upload extends DefaultTask {
    private App app;

    public void bindApp(App app) {
        this.app = app;
        app.controller.bindUpload(this);
    }

    @TaskAction
    public void doUpload() {
        if (app != null) {


//            org.gradle.api.tasks.Upload
            if (app.getRepositoriesTo().getRepositories().size() > 0) {
                RepositoriesTo repositoriesTo = app.getRepositoriesTo();
                if (repositoriesTo.getGroupId() == null) {
                    Log.err("not set groupId");
                    return;
                }
                Repository repository = repositoriesTo.getRepositories().get(0);
//                {
//
//                    File fileScript = new File(app.getProject().getBuildDir(), "vcsLibUpload.gradle");
//                    try {
//                        FileUtil.writeString(
//                                fileScript,
//                                FileUtil.readUTFString(vcsLib.class.getResourceAsStream("/vcsLibUpload.gradle"))
//                                        .replace("$" + App.KEY_VCS_LIBS_GROUP_ID, repositoriesTo.getGroupId())
////                                        .replace("$" + App.KEY_VCS_LIBS_ARTIFACT_ID, repositoriesTo.getArtifactId())
//                                        .replace("$" + App.KEY_VCS_LIBS_VERSION, repositoriesTo.getVersion())
//                                        .replace("$" + App.KEY_VCS_LIBS_HOME, repository.outDir().toURI().toString())
//                        );
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    app.getProject().apply(objectConfigurationAction -> {
//                        objectConfigurationAction.from(fileScript);
//                    });
////                    app.getProject().getTasks().findByName("uploadArchives").doLast("",task -> {
////                       Log.out("uploadArchives after...");
////                    });
//                }
                Log.out("start commit...");
                repository.commit();
                Log.out("complete commit. ");
                Log.out("start upload..." + " [from " + repository.outDir().getPath() + " to " + repository.getUrl() + "]");
                repository.upload();
                Log.out("complete doUpload.");
            }
        }
    }
}
