package org.yanning.gradle.vcs_lib.task

import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction
import org.yanning.gradle.vcs_lib.core.BaseTask
import org.yanning.gradle.vcs_lib.core.Conf
import org.yanning.gradle.vcs_lib.extension.Repository
import org.yanning.gradle.vcs_lib.utils.Log

import javax.inject.Inject

open class TestTask : BaseTask() {
    @TaskAction
    fun doAction() {
        logger.error("*************************************")
        logger.error("****** please input testName: ")
        val input= readLine()
        println("----------------------------------------:$input")
    }

}
