package org.yanning.gradle.vcs_lib.core

import jodd.util.PropertiesUtil
import org.yanning.gradle.vcs_lib.utils.Log
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*


class Conf(
        private val file: File) {
    private val props: Properties

    init {
        if (!file.exists()) file.createNewFile()
        props = PropertiesUtil.createFromFile(file)
        Log.out("props:"+props.toString())
    }

    fun setConf(confKey: ConfKey, value: String): Conf {
        props.setProperty(confKey.name, value)
        return this
    }

    fun getConf(confKey: ConfKey): String {
        Log.out("getConf:"+confKey.toString())
        return props.getProperty(confKey.name) ?: return ""
    }

    fun apply() {
        props.store(FileWriter(file), "update on " + SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()))
    }

    fun resetRepoConf(): Conf {
        setConf(ConfKey.repoUri, "")
        setConf(ConfKey.repoUsername, "")
        setConf(ConfKey.repoPassword, "")
        return this
    }

    fun resetMavenConf(): Conf {
        setConf(ConfKey.mavenGroupId, "")
        setConf(ConfKey.mavenArtifactId, "")
        setConf(ConfKey.mavenVersionName, "")
        return this
    }
}

/**
 *
 */
enum class ConfKey {
    /**
     *
     */
    vcsLibHome,
    //################################################################################
    /**
     *
     */
    repoUri,
    /**
     *
     */
    repoUsername,
    /**
     *
     */
    repoPassword,

    //################################################################################

    /**
     *
     */
    mavenGroupId,
    /**
     *
     */
    mavenArtifactId,
    /**
     *
     */
    mavenVersionName;

    /**
     *
     */
    fun displyName(): String {
        return "vcsLib_$name"
    }
}