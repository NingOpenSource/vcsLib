package org.yanning.gradle.vcs_lib.extension;

import jodd.io.FileUtil;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.transport.ChainingCredentialsProvider;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.filter.PathFilterGroup;
import org.gradle.api.logging.Logging;
import org.gradle.internal.impldep.org.bouncycastle.crypto.params.DSAParameterGenerationParameters;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.yanning.gradle.vcs_lib.utils.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class RepositoryGit extends Repository {
    public boolean isUseSSH = false;
    private String username;
    private String password;

    public void password(String password) {
        this.password = password;
    }

    public void username(String username) {
        this.username = username;
    }

    private boolean isGitRepository() {
        if (getUrl() == null) return false;
        return FileUtil.isExistingFolder(new File(outDir(), ".git"));
    }

    private CredentialsProvider getCredentialsProvider() {
        return new UsernamePasswordCredentialsProvider(username, password);
    }

    private Git mGit;

    private interface GitCall {
        void onCall(Git git);
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
                        .setURI(getUrl())
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
        if (getUrl() == null) return;
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
        Logging.getLogger("vcsLibs").info("update", "from " + getUrl() + " to " + outDir().getPath());
        System.out.println("vcsLibs:update --> from " + getUrl() + " to " + outDir().getPath());
    }

    @Override
    public void commit() {
        if (getUrl() == null) return;
        loadGit(git -> {
            try {
                List<DiffEntry> diffEntries = git.diff()
                        .setPathFilter(PathFilterGroup.createFromStrings("/"))
                        .setShowNameAndStatusOnly(true).call();
                if (diffEntries == null || diffEntries.size() == 0) {
                    throw new Exception("提交的文件内容都没有被修改，不能提交");
                }
                //被修改过的文件
                List<String> updateFiles = new ArrayList<String>();
                ChangeType changeType;
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
                git.commit().setMessage("upload new version:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).call();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void upload() {
        if (getUrl() == null) return;
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
