package org.yanning.gradle.vcs_lib.extension

enum class VcsType private constructor(val isThis: (url: String) -> Boolean) {
    svn({ url ->
        url != null && url.startsWith("svn:")
    }),
    git({ url ->
        url != null && url.endsWith(".git")
    });
}
