package org.yanning.gradle.vcs_lib.core

import org.gradle.api.DefaultTask

abstract class BaseTask : DefaultTask() {
    init {
        group="vcsLib"
    }
}