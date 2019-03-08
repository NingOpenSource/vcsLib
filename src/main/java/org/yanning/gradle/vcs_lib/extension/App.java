package org.yanning.gradle.vcs_lib.extension;

import jodd.io.FileUtil;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.yanning.gradle.vcs_lib.core.AppConfig;
import org.yanning.gradle.vcs_lib.core.LibrarySuffix;
import org.yanning.gradle.vcs_lib.task.Controller;
import org.yanning.gradle.vcs_lib.utils.Log;
import org.yanning.gradle.vcs_lib.vcsLib;

import java.io.File;
import java.io.IOException;

/**
 * 执行的主要操作
 */
public class App {
    private LibrarySuffix libSuffix;

    public App(Project project, LibrarySuffix libSuffix) {
        this.project = project;
        this.libSuffix = libSuffix;
        setVcsLibHome(AppConfig.INSTANCE.getDefaultVCSLibPath());
    }

    public Controller controller = new Controller();

    private File vcsLibHome;
    public static final String KEY_VCS_LIB_HOME = "VCS_LIB_HOME";
    public static final String KEY_VCS_LIB_ARTIFACT_ID = "VCS_LIB_ARTIFACT_ID";
    public static final String KEY_VCS_LIB_GROUP_ID = "VCS_LIB_GROUP_ID";
    public static final String KEY_VCS_LIB_VERSION = "VCS_LIB_VERSION";

    public File getVcsLibHome() {
        return vcsLibHome;
    }

    public void setVcsLibHome(File vcsLibHome) {
        this.vcsLibHome = vcsLibHome;
//        project.getExtensions().add(KEY_VCS_LIB_HOME, vcsLibHome);
        System.getProperties().put(KEY_VCS_LIB_HOME, vcsLibHome.getAbsolutePath());
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
//        System.getProperties().put(KEY_VCS_LIB_GROUP_ID, repositoriesTo.getGroupId());
//        System.getProperties().put(KEY_VCS_LIB_ARTIFACT_ID, repositoriesTo.getGroupId());
//        System.getProperties().put(KEY_VCS_LIB_VERSION, repositoriesTo.getVersion());
        autoConfigTo();
    }

    /**
     * @param libHome 用户目录
     */
    public void libHome(File libHome) {
        setVcsLibHome(libHome);
    }

    /**
     * @param libHome 用户目录
     */
    public void libHome(String libHome) {
        libHome(new File(libHome));
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
        if (vcsLibHome != null) {
            StringBuffer strings = new StringBuffer();
            repositoriesFrom.getRepositories().forEach(repository -> {
//                if (System.getProperty("os.name").startsWith("win")){
//                    strings.append(tag.replace("$lib_home", repository.outDir().getPath()));
//                }else {
                strings.append(tag.replace("$lib_home", repository.outDir().toURI().toString()));
//                }
            });
            File fileScript = new File(project.getProjectDir(), fileName);
            try {
                FileUtil.writeString(
                        fileScript,
                        FileUtil.readUTFString(vcsLib.class.getResourceAsStream("/" + fileName))
                                .replace("$repositories", strings.toString())
                );
                project.apply(objectConfigurationAction -> {
                    objectConfigurationAction.from(fileScript);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
//                    app.getProject().getTasks().findByName("uploadArchives").doLast("",task -> {
//                       Log.out("uploadArchives after...");
//                    });
        }
    }

    private void autoConfigTo() {
        if (repositoriesTo.getGroupId() != null && repositoriesTo.getArtifactId() != null && repositoriesTo.getVersion() != null
                && vcsLibHome != null) {
            if (repositoriesTo.getGroupId() == null) {
                Log.err("not set groupId");
                return;
            }
            Repository repository = repositoriesTo.getRepositories().get(0);
            String fileScriptName = "vcsLibUpload.gradle";
            if (libSuffix == libSuffix.AAR) {
                fileScriptName = "vcsLibUpload_android.gradle";
            }
            {
                File fileScript = new File(project.getProjectDir(), fileScriptName);
                try {
                    FileUtil.writeString(
                            fileScript,
                            FileUtil.readUTFString(vcsLib.class.getResourceAsStream("/" + fileScriptName))
                                    .replace("$" + App.KEY_VCS_LIB_GROUP_ID, repositoriesTo.getGroupId())
//                                        .replace("$" + App.KEY_VCS_LIB_ARTIFACT_ID, repositoriesTo.getArtifactId())
                                    .replace("$" + App.KEY_VCS_LIB_VERSION, repositoriesTo.getVersion())
                                    .replace("$" + App.KEY_VCS_LIB_HOME, repository.outDir().toURI().toString())
                    );
                    project.apply(objectConfigurationAction -> {
                        objectConfigurationAction.from(fileScript);
                    });
                    project.getTasks().findByName("vcsLibUpload").dependsOn(project.getTasks().findByName("uploadArchives").doFirst(task -> {
                        Log.err("uploadArchives ------------------> vcsLibUpload");
                    }).getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                    app.getProject().getTasks().findByName("uploadArchives").doLast("",task -> {
//                       Log.out("uploadArchives after...");
//                    });
            }
        }
    }
}
