package org.yanning.gradle.vcs_lib

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.yanning.gradle.vcs_lib.core.PlugType

class vcsLib_android : Plugin<Project> {

    override fun apply(target: Project?) {
        BaseLibUpload.apply(target,PlugType.android)
    }

}