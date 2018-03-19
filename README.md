# vcsLib

[![Download](https://api.bintray.com/packages/ningopensource/maven/vcsLib/images/download.svg) ](https://bintray.com/ningopensource/maven/vcsLib)
[![](https://jitpack.io/v/NingOpenSource/vcsLib.svg)](https://jitpack.io/#NingOpenSource/vcsLib)


用于gradle的vcs库管理

https://www.jianshu.com/p/944bb00355e2

https://github.com/zacker330/jshint-gradle
### How to use?

1. config step 1>>`$rootDir/build.gradle`:
    
        buildscript {
            repositories {
                jcenter()  //required
            }
            dependencies {
                classpath "org.yanning.gradle:vcsLib:+"  //lastest version
            }
        }
        
    **or**
    
        buildscript {
            repositories {
                jcenter()  //required
                maven { url 'https://jitpack.io' }  //required
            }
            dependencies {
                classpath 'com.github.NingOpenSource:vcsLib:+'  //lastest version
            }
        }
        

2. config step 2>>`$projectDir/build.gradle`:
    
    Android:
        
        apply plugin: 'vcsLib_android'
        
    Java:
    
        apply plugin: 'vcsLib'
    
    Then:
    
        vcsLib{
            from{   // add a vcs repository
                vcs "svn://192.168.0.1/android/.vcsLibs", "username", "password" //config svn or git repository
            }
            to{     //config maven, publish java or android library to vcs repository
                groupId "com.github.NingOpenSource"
                artifactId 'LogFormat'
                version "0.0.3"
                vcs "svn://192.168.0.1/android/.vcsLibs", "username", "password" //config svn or git repository
            }
        }
        
3. publish java or android library to vcs repository
    
    **CMD**：
        
        gradle :[module_name]:vcsLibUpload
    
    **IDE**：
    
    ![](./pic/20180314162631.png)
        
4. dependencies
        
        dependencies {
            compile 'com.github.NingOpenSource:LogFormat:0.0.3'
        }


### upload to jcenter:

**CMD**
    
        gradle clean build install generatePomFileForMavenPublication bintrayUpload -PbintrayUser=[username] -PbintrayKey=[userkey] -PdryRun=false
