//package org.yanning.gradle.vcs_lib.core
//
//import jodd.util.PropertiesUtil
//import org.yanning.gradle.vcs_lib.utils.Log
//import java.io.File
//import java.io.FileWriter
//import java.text.SimpleDateFormat
//import java.util.*
//
//
//class Conf(
//        private val file: File) {
//
//
//
//    private val props = Properties()
//
//    init {
//
//        if (!file.exists()) file.createNewFile()
//        PropertiesUtil.loadFromFile(props, file)
//        Log.out("props:$props")
//    }
//
//    fun setConf(confKey: ConfKey, value: String?): Conf {
//        if (value.isNullOrEmpty()) {
//            props.setProperty(confKey.name, confKey.defaultValue)
//        } else {
//            props.setProperty(confKey.name, value)
//        }
//        return this
//    }
//
//    fun getConf(confKey: ConfKey): String {
//        return getConf(confKey, "")
//    }
//
//    fun getConf(confKey: ConfKey, defaultValue: String): String {
//        var value = props.getProperty(confKey.name) ?: defaultValue
//        if (confKey.defaultValue == value) value = defaultValue
//        Log.out("getConf:$confKey")
//        return value
//    }
//
//    fun apply() {
//        val fileWriter = FileWriter(file)
//        props.store(fileWriter, "update on " + SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()))
//        fileWriter.close()
//    }
//
//    fun resetBaseConf(): Conf {
//        setConf(ConfKey.uri, null)
//        setConf(ConfKey.user, null)
//        setConf(ConfKey.passwd, null)
//        setConf(ConfKey.isAutoUpdateOnBuild, null)
//        return this
//    }
//
//    fun resetMavenConf(): Conf {
//        setConf(ConfKey.mavenId, null)
//        return this
//    }
//}
//
///**
// *
// */
//enum class ConfKey(val defaultValue: String) {
//    /**
//     *
//     */
////    VCSLIB_HOME("~/.vcsLib"),
//    //################################################################################
//    /**
//     *仓库地址
//     */
//    uri("[Nonull] please input your vcs uri"),
//    /**
//     * 仓库登录用户名，没有则不填
//     */
//    user("[Nonull] please input your vcs username"),
//    /**
//     *仓库登录密码，没有则不填
//     */
//    passwd("[Nonull] please input your vcs password"),
//    /**
//     * 是否在编译时自动更新仓库（true/false,default is false）（PS:可能会增加编译时长）
//     */
//    isAutoUpdateOnBuild("false"),
//    //################################################################################
//    /**
//     * group:name:version
//     */
//    mavenId("[Nullable] please input 'group:name:version' like 'org.demo:demoLib:0.0.1'");
//
//    /**
//     *
//     */
//    fun displyName(): String {
//        return "vcsLib_$name"
//    }
//}