package org.yanning.gradle.vcs_lib.extension;

public class RepositoriesTo extends RepositoriesFrom {
    private String groupId;
    private String artifactId;
    private String version = "0.0.1";

    public void groupId(String groupId) {
        this.groupId = groupId;
    }

    public void artifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public void version(String version) {
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }
}
