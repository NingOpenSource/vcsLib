package org.yanning.gradle.vcs_lib.extension;

public enum VcsType {
    svn(url -> {
        if (url != null && url.startsWith("svn:")) {
            return true;
        }
        return false;
    }), git(url -> {
        if (url != null && url.endsWith(".git")) {
            return true;
        }
        return false;
    });

    private interface Checker {
        boolean isThis(String url);

    }


    private Checker checker;

    VcsType(Checker checker) {
        this.checker = checker;
    }

    public boolean isThis(String url) {
        return checker.isThis(url);
    }
}
