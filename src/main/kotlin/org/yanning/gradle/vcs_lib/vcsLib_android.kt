package org.yanning.gradle.vcs_lib

import jodd.io.FileUtil
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.yanning.gradle.vcs_lib.core.PlugType
import org.yanning.gradle.vcs_lib.extension.App
import org.yanning.gradle.vcs_lib.task.Build
import org.yanning.gradle.vcs_lib.task.Update
import org.yanning.gradle.vcs_lib.task.Upload
import org.yanning.gradle.vcs_lib.utils.Log
import java.io.File

class vcsLib_android : Plugin<Project> {

    override fun apply(target: Project?) {
        Base.apply(target,PlugType.android)
    }

}