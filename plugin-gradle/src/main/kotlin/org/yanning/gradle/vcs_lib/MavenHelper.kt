//package org.yanning.gradle.vcs_lib
//
//import org.gradle.api.Project
//import org.gradle.jvm.tasks.Jar
//import java.io.File
//
//class MavenHelper(val target: Project) {
//
//
//    fun setLocalMavenDeployer(mavenId: String, outputDir: String) {
//        val mavenIdInfo = mavenId.split(":")
//        if (mavenIdInfo.size != 3 || mavenIdInfo[0].isEmpty() || mavenIdInfo[1].isEmpty() || mavenIdInfo[2].isEmpty()) {
//            println("mavenId:$mavenId is wrong!")
//            return
//        }
//        val mGroup = mavenIdInfo[0]
//        val mArtifactId = mavenIdInfo[1]
//        val mVersion = mavenIdInfo[2]
//        println("localMavenDeployerConfig -> outputDir:${File(outputDir).absolutePath}")
//        if (target.plugins.hasPlugin("maven")) {
//            target.pluginManager.apply("maven")
//        }
//        target.group = mGroup
//        target.version = mVersion
//        if (target.plugins.hasPlugin("kotlin-android")){
//            if (!target.plugins.hasPlugin("org.jetbrains.dokka")) {
//                target.pluginManager.apply("org.jetbrains.dokka")
//            }
//            if (target.tasks.findByName("generateSourcesJar") == null)
//                target.tasks.create("generateSourcesJar",Jar::class.java).apply{
//                    group="jar"
//target.extensions.findByName("android")
//                }
//                task generateSourcesJar(type: Jar) {
//                group = 'jar'
//                from android.sourceSets.main.java.srcDirs
//                        archiveClassifier = 'sources'
//            }
//        }
//    }
//
//}