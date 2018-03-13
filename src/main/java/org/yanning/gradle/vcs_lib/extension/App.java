package org.yanning.gradle.vcs_lib.extension;

import jodd.io.FileUtil;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.yanning.gradle.vcs_lib.task.Controller;
import org.yanning.gradle.vcs_lib.utils.Log;
import org.yanning.gradle.vcs_lib.vcsLib;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 执行的主要操作
 */
public class App {
    public App(Project project) {
        this.project = project;
        setVcsLibsHome(new File(System.getProperty("user.home"), ".vcsLib"));
    }

    public Controller controller = new Controller();

    private File vcsLibsHome;
    public static final String KEY_VCS_LIBS_HOME = "VCS_LIBS_HOME";
    public static final String KEY_VCS_LIBS_ARTIFACT_ID = "VCS_LIBS_ARTIFACT_ID";
    public static final String KEY_VCS_LIBS_GROUP_ID = "VCS_LIBS_GROUP_ID";
    public static final String KEY_VCS_LIBS_VERSION = "VCS_LIBS_VERSION";

    public File getVcsLibsHome() {
        return vcsLibsHome;
    }

    public void setVcsLibsHome(File vcsLibsHome) {
        this.vcsLibsHome = vcsLibsHome;
//        project.getExtensions().add(KEY_VCS_LIBS_HOME, vcsLibsHome);
        System.getProperties().put(KEY_VCS_LIBS_HOME, vcsLibsHome.getAbsolutePath());
    }

    private Project project;

    public Project getProject() {
        return project;
    }

    private RepositoriesFrom repositoriesFrom = new RepositoriesFrom();
    private RepositoriesTo repositoriesTo = new RepositoriesTo();


    /**
     * 更新源（可以添加多个仓库源）
     *
     * @param repositoriesAction 下载源
     */
    public void from(Action<RepositoriesFrom> repositoriesAction) {
        repositoriesAction.execute(repositoriesFrom);//执行vcs仓库的增加
        repositoriesFrom.getRepositories().forEach(repository -> {//更新每一个仓库的地址
            repository.update();
        });
        autoConfigFrom();
    }

    /**
     * 上传源（一般只有一个，多个则会同时提交到多个源中）
     *
     * @param repositoriesAction 上传源，只允许设置一个
     */
    public void to(Action<RepositoriesTo> repositoriesAction) {
        repositoriesAction.execute(repositoriesTo);
//        System.getProperties().put(KEY_VCS_LIBS_GROUP_ID, repositoriesTo.getGroupId());
//        System.getProperties().put(KEY_VCS_LIBS_ARTIFACT_ID, repositoriesTo.getGroupId());
//        System.getProperties().put(KEY_VCS_LIBS_VERSION, repositoriesTo.getVersion());
        autoConfigTo();
    }

    /**
     * @param libsHome 用户目录
     */
    public void libsHome(File libsHome) {
        setVcsLibsHome(libsHome);
    }

    /**
     * @param libsHome 用户目录
     */
    public void libsHome(String libsHome) {
        libsHome(new File(libsHome));
        autoConfigTo();
    }

    public RepositoriesFrom getRepositoriesFrom() {
        return repositoriesFrom;
    }

    public RepositoriesTo getRepositoriesTo() {
        return repositoriesTo;
    }

    private void autoConfigFrom() {
        final String fileName = "vcsLibUpdate.gradle";
        final String tag = "maven { url \"$lib_home\" }\n";
        if (vcsLibsHome != null) {
            StringBuffer strings = new StringBuffer();
            repositoriesFrom.getRepositories().forEach(repository -> {
//                if (System.getProperty("os.name").startsWith("win")){
//                    strings.append(tag.replace("$lib_home", repository.outDir().getPath()));
//                }else {
                    strings.append(tag.replace("$lib_home", repository.outDir().toURI().toString()));
//                }
            });
            File fileScript = new File(project.getBuildDir(), fileName);
            try {
                    FileUtil.writeString(
                            fileScript,
                            FileUtil.readUTFString(vcsLib.class.getResourceAsStream("/" + fileName))
                                    .replace("$repositories", strings.toString())
                    );
            } catch (IOException e) {
                e.printStackTrace();
            }
            project.apply(objectConfigurationAction -> {
                objectConfigurationAction.from(fileScript);
            });
//                    app.getProject().getTasks().findByName("uploadArchives").doLast("",task -> {
//                       Log.out("uploadArchives after...");
//                    });
        }
    }

    private void autoConfigTo() {
        if (repositoriesTo.getGroupId() != null && repositoriesTo.getArtifactId() != null && repositoriesTo.getVersion() != null
                && vcsLibsHome != null) {
            if (repositoriesTo.getGroupId() == null) {
                Log.err("not set groupId");
                return;
            }
            Repository repository = repositoriesTo.getRepositories().get(0);
            {
                File fileScript = new File(project.getBuildDir(), "vcsLibUpload.gradle");
                try {
                    FileUtil.writeString(
                            fileScript,
                            FileUtil.readUTFString(vcsLib.class.getResourceAsStream("/vcsLibUpload.gradle"))
                                    .replace("$" + App.KEY_VCS_LIBS_GROUP_ID, repositoriesTo.getGroupId())
//                                        .replace("$" + App.KEY_VCS_LIBS_ARTIFACT_ID, repositoriesTo.getArtifactId())
                                    .replace("$" + App.KEY_VCS_LIBS_VERSION, repositoriesTo.getVersion())
                                    .replace("$" + App.KEY_VCS_LIBS_HOME, repository.outDir().toURI().toString())
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
                project.apply(objectConfigurationAction -> {
                    objectConfigurationAction.from(fileScript);
                });
                project.getTasks().findByName("vcsLibsUpload").dependsOn(project.getTasks().findByName("uploadArchives").doFirst(task -> {
                    Log.err("uploadArchives ------------------> vcsLibsUpload");
                }).getPath());
//                    app.getProject().getTasks().findByName("uploadArchives").doLast("",task -> {
//                       Log.out("uploadArchives after...");
//                    });
            }
        }
    }
}
