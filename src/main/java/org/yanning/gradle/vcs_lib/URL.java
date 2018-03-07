package org.yanning.gradle.vcs_lib;

import org.yanning.gradle.vcs_lib.utils.FileUtils;

import java.io.File;

/**
 * URL模型
 */
public class URL {
    public enum Type {
        svn(new Checker() {
            @Override
            public boolean isThis(String url) {
                if (url != null && url.startsWith("svn:")) {
                    return true;
                }
                return false;
            }
        }), git(new Checker() {
            @Override
            public boolean isThis(String url) {
                if (url != null && url.endsWith(".git")) {
                    return true;
                }
                return false;
            }
        });

        private interface Checker {
            boolean isThis(String url);
        }

        private Checker checker;

        Type(Checker checker) {
            this.checker = checker;
        }

        public boolean isThis(String url) {
            return checker.isThis(url);
        }
    }

    private String url;
    private Type url_type;
    private File VCS_LIB_HOME;

    private String groupId;
    private String artifactId;
    private String version;

    public URL(String url) {
        this.url = url;
    }

    private File outDir() {
        File vcsDir = FileUtils.createDir(new File(getVCS_LIB_HOME(), getUrl_type().toString()));
        File groupDir = FileUtils.createDir(new File(vcsDir, groupId));
        File artifactDir = FileUtils.createDir(new File(groupDir, artifactId));
        File versionDir = FileUtils.createDir(new File(artifactDir, version));
        return versionDir;
    }

    public File getVCS_LIB_HOME() {
        return VCS_LIB_HOME;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setVCS_LIB_HOME(File VCS_LIB_HOME) {
        this.VCS_LIB_HOME = VCS_LIB_HOME;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Type getUrl_type() {
        return url_type;
    }

    public void setUrl_type(Type url_type) {
        this.url_type = url_type;
    }
}
