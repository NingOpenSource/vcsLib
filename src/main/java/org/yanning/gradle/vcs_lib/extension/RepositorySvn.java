package org.yanning.gradle.vcs_lib.extension;

import org.gradle.api.logging.Logging;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RepositorySvn extends Repository {
    private String username;
    private String password;
    private DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);


    public void password(String password) {
        this.password = password;
    }

    public void username(String username) {
        this.username = username;
    }

    @Override
    public void update() {
        if (getUrl() == null) return;
        try {
            SVNClientManager svnClientManager = SVNClientManager.newInstance(options, username, password);
            SVNURL svnurl = SVNURL.parseURIEncoded(getUrl());

            if (!outDir().exists()) {//不存在的目录需要进行检出
                SVNUpdateClient svnUpdateClient = svnClientManager.getUpdateClient();
                svnUpdateClient.doCheckout(svnurl, outDir(), SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY, false);
            } else {
                SVNUpdateClient svnUpdateClient = svnClientManager.getUpdateClient();
                svnUpdateClient.doUpdate(outDir(), SVNRevision.HEAD, SVNDepth.INFINITY, false, true);
            }
        } catch (SVNException e) {
            e.printStackTrace();
        }
//        Logging.getLogger("vcsLibs").info("update", "from " + getUrl() + " to " + outDir().getPath());
//        System.out.println("vcsLibs:update --> from " + getUrl() + " to " + outDir().getPath());
    }

    @Override
    public void commit() {
        if (getUrl() == null) return;
        SVNClientManager svnClientManager = SVNClientManager.newInstance(options, username, password);
        checkVersionDirectory(svnClientManager, outDir());
    }

    /**
     * 递归检查不在版本控制的文件，并add到svn
     *
     * @param clientManager
     */
    private void checkVersionDirectory(SVNClientManager clientManager, File dir) {
        if (!SVNWCUtil.isVersionedDirectory(dir)) {
            try {
                clientManager.getWCClient().doAdd(new File[]{dir}, true,
                        false, false, SVNDepth.INFINITY, false, false,
                        true);
            } catch (SVNException e) {
                e.printStackTrace();
            }
        }
        if (dir.isDirectory()) {
            for (File subDir : dir.listFiles()) {
                if (subDir.isDirectory() && subDir.getName().equals(".svn")) {
                    continue;
                }
                checkVersionDirectory(clientManager, subDir);
            }
        }
    }

    @Override
    public void upload() {
        if (getUrl() == null) return;
        try {
            update();
            SVNClientManager svnClientManager = SVNClientManager.newInstance(options, username, password);
            SVNCommitClient svnCommitClient = svnClientManager.getCommitClient();

            svnCommitClient.doCommit(
                    new File[]{outDir()},
                    true,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                    null, null, false, false, SVNDepth.INFINITY);
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }

    @Override
    public VcsType vcsType() {
        return VcsType.svn;
    }
}
