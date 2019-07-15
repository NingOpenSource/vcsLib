package org.yanning.gradle.vcs_lib.extension;

import jodd.io.FileUtil;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.gradle.api.logging.Logging;
import org.yanning.gradle.vcs_lib.core.Conf;
import org.yanning.gradle.vcs_lib.core.ConfKey;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class RepositoryGit extends Repository {
    public boolean isUseSSH = false;

    public RepositoryGit(Conf conf) {
        super(conf);
    }

    private boolean isGitRepository() {
        if (getConf() == null) return false;
        return FileUtil.isExistingFolder(new File(outDir(), ".git"));
    }

    private CredentialsProvider getCredentialsProvider() {
        return new UsernamePasswordCredentialsProvider(getConf().getConf(ConfKey.repoUsername), getConf().getConf(ConfKey.repoPassword));
    }

    private Git mGit;

    private interface GitCall {
        void onCall(Git git);
    }

    @Override
    public boolean hasCheckout() {
        return isGitRepository();
    }

    private void loadGit(GitCall gitCall) {
        if (mGit != null) {
            gitCall.onCall(mGit);
            return;
        }
        if (!isGitRepository()) {
            try {
                mGit = Git.cloneRepository()
                        .setCredentialsProvider(getCredentialsProvider())
                        .setCloneAllBranches(false)
                        .setCloneSubmodules(false)
                        .setDirectory(outDir())
                        .setCallback(new CloneCommand.Callback() {
                            @Override
                            public void initializedSubmodules(Collection<String> submodules) {

                            }

                            @Override
                            public void cloningSubmodule(String path) {

                            }

                            @Override
                            public void checkingOut(AnyObjectId commit, String path) {

                            }
                        })
                        .setURI(getConf().getConf(ConfKey.repoUri))
                        .call();
                gitCall.onCall(mGit);
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        } else {
            try {
                mGit = Git.open(outDir());
                gitCall.onCall(mGit);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update() {
        if (!isGitRepository()) {
            loadGit(git -> {
                try {
                    git.pull().setTransportConfigCallback(transport -> {

                    }).setCredentialsProvider(getCredentialsProvider()).call();
                } catch (GitAPIException e) {
                    e.printStackTrace();
                }
            });
        }
        Logging.getLogger("vcsLibs").info("update", "from " + getConf().getConf(ConfKey.repoUri) + " to " + outDir().getPath());
        System.out.println("vcsLibs:update --> from " + getConf().getConf(ConfKey.repoUri) + " to " + outDir().getPath());
    }

    @Override
    public void commit() {
        loadGit(git -> {
            try {
                List<DiffEntry> diffEntries = git.diff()
//                        .setPathFilter(PathFilterGroup.createFromStrings(outDir().getPath()))
                        .setShowNameAndStatusOnly(true).call();
                if (diffEntries == null || diffEntries.size() == 0) {
//                    throw new RuntimeException("提交的文件内容都没有被修改，不能提交");
                    return;
                }
                //被修改过的文件
                List<String> updateFiles = new ArrayList<String>();
                DiffEntry.ChangeType changeType;
                for (DiffEntry entry : diffEntries) {
                    changeType = entry.getChangeType();
                    switch (changeType) {
                        case ADD:
                        case COPY:
                        case RENAME:
                        case MODIFY:
                            updateFiles.add(entry.getNewPath());
                            break;
                        case DELETE:
                            updateFiles.add(entry.getOldPath());
                            break;
                    }
                }
                //将文件提交到git仓库中，并返回本次提交的版本号
                //1、将工作区的内容更新到暂存区
                AddCommand addCmd = git.add();
                for (String file : updateFiles) {
                    addCmd.addFilepattern(file);
                }
                addCmd.call();
                git.add().addFilepattern("*").call();
                git.commit().setMessage("upload new version:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).call();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void upload() {
        loadGit(git -> {
            try {
                git.push().setTransportConfigCallback(transport -> {
                }).setCredentialsProvider(getCredentialsProvider()).setForce(false).call();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public VcsType vcsType() {
        return VcsType.git;
    }
}
