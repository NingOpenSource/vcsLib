package org.yanning.gradle.vcs_lib.extension;

import org.gradle.api.Action;
import org.gradle.api.Project;

import java.io.File;
import java.util.List;

/**
 * 执行的主要操作
 */
public class App extends Config {
    private Project project;

    private Repositories repositoriesFrom = new Repositories();
    private Repositories repositoriesTo = new Repositories();

    public App(Project project) {
        this.project = project;
    }

    /**
     * 更新源（可以添加多个仓库源）
     *
     * @param repositoriesAction
     */
    public void from(Action<Repositories> repositoriesAction) {
        repositoriesAction.execute(repositoriesFrom);//执行vcs仓库的增加
        repositoriesFrom.getRepositories().forEach(repository -> {//更新每一个仓库的地址
            repository.update();
        });
    }

    /**
     * 上传源（一般只有一个，多个则会同时提交到多个源中）
     *
     * @param repositoriesAction
     */
    public void to(Action<Repositories> repositoriesAction) {
        repositoriesAction.execute(repositoriesTo);
    }

    /**
     * @param libsHome
     */
    public void libsHome(File libsHome) {
        setVcsLibsHome(libsHome);
    }

    /**
     * @param libsHome
     */
    public void libsHome(String libsHome) {
        libsHome(new File(libsHome));
    }

    public Repositories getRepositoriesFrom() {
        return repositoriesFrom;
    }

    public Repositories getRepositoriesTo() {
        return repositoriesTo;
    }
}
