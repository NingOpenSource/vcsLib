package org.yanning.gradle.vcs_lib

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.javadoc.Javadoc

class vcsLibMavenPlugin implements Plugin<Project> {
    class vcsLibMaven {
        String mavenId = ''
        /**
         * 仓库地址
         */
        String uri = ''
        String user = ''
        String passwd = ''
    }

    @Override
    void apply(Project target) {
        def ext = target.extensions.create("vcsLibMaven", vcsLibMaven)
        if (ext==null){
            println("please config :\r\nvcsLibMaven {\r\n  mavenId=?\r\n  uri=?\r\n  user=?\r\n  passwd=?\r\n}")
        }else if(ext.mavenId.isEmpty()||ext.uri.isEmpty()){
            println("please config :\r\nvcsLibMaven {\r\n  mavenId=${ext.mavenId}\r\n  uri=${ext.uri}\r\n  user=${ext.user}\r\n  passwd=${ext.passwd}\r\n}")
        }
        setLocalMavenDeployer(target, ext.mavenId, RepoConfig.outDir(ext.uri).getAbsolutePath())
    }


    void setLocalMavenDeployer(Project target, String mavenId, String outputDir) {
        target.apply {
            String[] mavenIdInfo = mavenId.split(":")
            if (mavenIdInfo.length != 3 || mavenIdInfo[0].length() == 0 || mavenIdInfo[1].length() == 0 || mavenIdInfo[2].length() == 0) {
                println("mavenId:$mavenId is wrong!")
                return
            }
            String mGroup = mavenIdInfo[0]
            String mArtifactId = mavenIdInfo[1]
            String mVersion = mavenIdInfo[2]
            println("localMavenDeployerConfig -> outputDir:${new File(outputDir).absolutePath}")
            if (!plugins.hasPlugin('maven'))
                apply plugin: 'maven'
            group mGroup
            version mVersion
            if (plugins.hasPlugin("kotlin-android")) {
                if (!plugins.hasPlugin('org.jetbrains.dokka')) {
                    /**
                     buildscript {repositories {google()
                     jcenter()}dependencies {classpath "org.jetbrains.dokka:dokka-gradle-plugin:0.9.18"}}*/
                    apply plugin: 'org.jetbrains.dokka'
                }
                if (tasks.findByName('generateSourcesJar') == null)
                    task generateSourcesJar(type: Jar) {
                        group = 'jar'
                        from android.sourceSets.main.java.srcDirs
                        archiveClassifier = 'sources'
                    }

                if (tasks.findByName('javadoc') == null)
                    task javadoc(type: Javadoc) {
                        source = android.sourceSets.main.java.srcDirs
                        classpath += project.files(android.getBootClasspath().join(File.pathSeparator))
                    }

                if (tasks.findByName('dokkaJavadoc') == null)
                    task dokkaJavadoc(type: dokka.getClass()) {
                        outputFormat = "javadoc"
                        outputDirectory = javadoc.destinationDir
                    }

                if (tasks.findByName('generateJavadoc') == null)
                    task generateJavadoc(type: Jar, dependsOn: dokkaJavadoc) {
                        group = 'jar'
                        archiveClassifier = 'javadoc'
                        from javadoc.destinationDir
                    }

                artifacts {
                    archives generateJavadoc
                    archives generateSourcesJar
                }

            } else {
                if (plugins.hasPlugin("com.android.library")) {//android
                    if (tasks.findByName('sourcesJar') == null)
                        task sourcesJar(type: Jar) {
                            from android.sourceSets.main.java.srcDirs
                            archiveClassifier = 'sources'
                        }
                    if (tasks.findByName('javadoc') == null)
                        task javadoc(type: Javadoc) {
                            if (JavaVersion.current().isJava9Compatible()) {
                                options.addBooleanOption('html5', true)
                            }
                            options.encoding = "UTF-8"
                            source = android.sourceSets.main.java.srcDirs
                            classpath += project.files(android.getBootClasspath().join(File.pathSeparator))
                        }
                } else if (plugins.hasPlugin("java-library")) {//java
                    if (tasks.findByName('sourcesJar') == null)
                        task sourcesJar(type: Jar) {
                            from sourceSets.main.java.srcDirs
                            archiveClassifier = 'sources'
                        }
                    if (tasks.findByName('javadoc') == null)
                        task javadoc(type: Javadoc) {
                            if (JavaVersion.current().isJava9Compatible()) {
                                options.addBooleanOption('html5', true)
                            }
                            options.encoding = "UTF-8"
                            source = sourceSets.main.java.srcDirs
                            classpath += project.files(getBootClasspath().join(File.pathSeparator))
                        }
                }
                if (tasks.findByName('javadocJar') == null)
                    task javadocJar(type: Jar, dependsOn: javadoc) {
                        archiveClassifier = 'javadoc'
                        from javadoc.destinationDir
                    }

                artifacts {
                    archives javadocJar
                    archives sourcesJar
                }

                tasks.withType(Javadoc) {
                    options.addStringOption('Xdoclint:none', '-quiet')
                    options.addStringOption('encoding', 'UTF-8')
                    options.addStringOption('charSet', 'UTF-8')
                }
            }
            uploadArchives {
                repositories {
                    mavenDeployer {
                        repository(url: new File(outputDir).toURI())
                        pom.version = mVersion
                        pom.artifactId = mArtifactId
                    }
                }
            }
        }
    }

}
