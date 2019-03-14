package org.yanning.gradle.vcs_lib

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.yanning.gradle.vcs_lib.core.LibrarySuffix
import org.yanning.gradle.vcs_lib.extension.MavenBuild

class vcsLib_aar : Plugin<Project> {
    override fun apply(target: Project) {
        MavenBuild(target, LibrarySuffix.AAR).build()
    }
}