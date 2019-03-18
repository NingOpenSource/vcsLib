package org.yanning.gradle.vcs_lib.core

import jodd.io.StreamUtil
import jodd.util.PropertiesUtil
import org.yanning.gradle.vcs_lib.extension.VcsType
import org.yanning.gradle.vcs_lib.utils.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*


class Conf(
        private val file: File) {
    private val props = OrderProperties()

    init {

        if (!file.exists()) file.createNewFile()
        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(file)
            props.load(fis)
        } finally {
            StreamUtil.close(fis)
        }
        Log.out("props:$props")
    }

    fun setConf(confKey: ConfKey, value: String): Conf {
        props.setProperty(confKey.name, value)
        return this
    }

    fun getConf(confKey: ConfKey): String {
        Log.out("getConf:$confKey")
        return props.getProperty(confKey.name) ?: return ""
    }

    fun apply() {
        val fileWriter = FileWriter(file)
        props.orderStore(fileWriter, "update on " + SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                + "\n## Demo:"
                + "\n## ${ConfKey.repoUri.displyName()}:https\\://github.com/demo/demo.git"
                + "\n## ${ConfKey.repoType.displyName()}:${VcsType.svn.name}/${VcsType.git.name}"
                + "\n## ${ConfKey.repoUsername.displyName()}:Nullable"
                + "\n## ${ConfKey.repoPassword.displyName()}:Nullable"
                + "\n## ${ConfKey.isAutoUpdateRepoOnBuild.displyName()}:true/false (default is false)"
                + "\n## ${ConfKey.isUseUploadMaven.displyName()}:true/false (default is false)"
                + "\n## if(!${ConfKey.isUseUploadMaven.displyName()}) return"
                + "\n## ${ConfKey.mavenUploadType.displyName()}:${LibrarySuffix.JAR.name.toLowerCase()}/${LibrarySuffix.AAR.name.toLowerCase()}"
                + "\n## ${ConfKey.mavenGroupId.displyName()}:org.demo"
                + "\n## ${ConfKey.mavenArtifactId.displyName()}:demo123"
                + "\n## ${ConfKey.mavenVersionName.displyName()}:1.2.3")
        fileWriter.close()
    }

    fun resetBaseConf(): Conf {
        props.setPropertyWithComment(ConfKey.vcsLibHome.name, AppConfig.vcsLibHome.absolutePath, "vcsLibs home dir")
        props.setPropertyWithComment(ConfKey.repoUri.name, "", "")
        props.setPropertyWithComment(ConfKey.repoType.name, "", "svn/git")
        props.setPropertyWithComment(ConfKey.repoUsername.name, "", "nullable")
        props.setPropertyWithComment(ConfKey.repoPassword.name, "", "nullable")
        props.setPropertyWithComment(ConfKey.isAutoUpdateRepoOnBuild.name,false.toString(),"true/false,default is false")
        return this
    }

    fun resetMavenConf(): Conf {
        props.setPropertyWithComment(ConfKey.isUseUploadMaven.name, false.toString(), "true/false,default is false")
        props.setPropertyWithComment(ConfKey.mavenUploadType.name, "jar", "jar/aar,default is jar")
        props.setPropertyWithComment(ConfKey.mavenGroupId.name, "", "")
        props.setPropertyWithComment(ConfKey.mavenArtifactId.name, "", "")
        props.setPropertyWithComment(ConfKey.mavenVersionName.name, "", "")
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
     *仓库地址
     */
    repoUri,
    /**
     * 仓库类型(svn/git)
     */
    repoType,
    /**
     * 仓库登录用户名，没有则不填
     */
    repoUsername,
    /**
     *仓库登录密码，没有则不填
     */
    repoPassword,
    /**
     * 是否在编译时自动更新仓库（true/false,default is false）（PS:可能会增加编译时长）
     */
    isAutoUpdateRepoOnBuild,
    //################################################################################
    /**
     * 是否使用上传功能(true/false)
     */
    isUseUploadMaven,
    /**
     * 仓库上传类型(jar/aar)
     */
    mavenUploadType,
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