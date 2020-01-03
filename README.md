# vcsLib（v2.0.0-alpha-003）



v2.0版本测试中，改进方向
1. 配置不再放在`build.gradle`中，新建配置文件`vcsLib.properties`（完成）
1. 提供一个GUI版本的配置工具（正在进行...）
1. vsc仓库的更新操作不再跟随`gradle build`操作（防止SVN目录锁住）（完成）
1. 待续...





[![Download](https://api.bintray.com/packages/ningopensource/maven/vcsLib/images/download.svg) ](https://bintray.com/ningopensource/maven/vcsLib)
[![](https://jitpack.io/v/NingOpenSource/vcsLib.svg)](https://jitpack.io/#NingOpenSource/vcsLib)


用于gradle的vcs库管理，目前支持git以及SVN当作maven仓库

https://www.jianshu.com/p/944bb00355e2

https://github.com/zacker330/jshint-gradle

maven官方同样也在开发类似的插件：[http://maven.apache.org/wagon/wagon-providers/wagon-scm/](http://maven.apache.org/wagon/wagon-providers/wagon-scm/)

### How to use?

1. config step 1>>`$rootDir/build.gradle`:
    ```gradle
    buildscript {
        repositories {
            jcenter()  //required
        }
        dependencies {
            classpath "org.yanning.gradle:vcsLib:+"  //lastest version
        }
    }
    ```
    or
    ```gradle
    buildscript {
        repositories {
            jcenter()  //required
            maven { url 'https://jitpack.io' }  //required
        }
        dependencies {
            classpath 'com.github.NingOpenSource:vcsLib:+'  //lastest version
        }
    }
    ```        

1. config step 2>>`$projectDir/build.gradle`:
    
    ```gradle
    apply plugin: 'vcsLib'
    ```
1. config file 'vcsLib.properties'
    ```properties
    #update on 2019-03-18 15:10:49
    ## Demo:
    ## vcsLib_repoUri:https\://github.com/demo/demo.git
    ## vcsLib_repoType:svn/git
    ## vcsLib_repoUsername:Nullable
    ## vcsLib_repoPassword:Nullable
    ## vcsLib_isAutoUpdateRepoOnBuild:true/false (default is false)
    ## vcsLib_isUseUploadMaven:true/false (default is false)
    ## if(!vcsLib_isUseUploadMaven) return
    ## vcsLib_mavenUploadType:jar/aar
    ## vcsLib_mavenGroupId:org.demo
    ## vcsLib_mavenArtifactId:demo123
    ## vcsLib_mavenVersionName:1.2.3
    #Mon Mar 18 15:10:49 CST 2019
    
    #vcsLibs home dir
    vcsLibHome=C\:\\Users\\Administrator\\.vcsLib
    
    repoUri=https\://gitee.com/VarietyShop/vcsLibDemo.git
    
    #svn/git
    repoType=git
    
    #nullable
    repoUsername=123456@demo.com
    
    #nullable
    repoPassword=123456
    
    #true/false,default is false
    isAutoUpdateRepoOnBuild=false
    
    #true/false,default is false
    isUseUploadMaven=true
    
    #jar/aar,default is jar
    mavenUploadType=aar
    
    mavenGroupId=org.demo
    
    mavenArtifactId=demo123
    
    mavenVersionName=1.2.3
    
    ```
 
        
1. publish java or android library to vcs repository
    
    CMD：
    ```gradle    
            gradle :[module_name]:vcsLibUpload
    ```
    IDE：
        
    ![](./pic/20180319110507.png)
        
1. dependencies
    ```gradle        
    dependencies {
        compile 'com.github.NingOpenSource:LogFormat:0.0.3'
    }
    ```

### upload to jcenter:

CMD
```gradle    
gradle clean build install generatePomFileForMavenPublication bintrayUpload -PbintrayUser=[username] -PbintrayKey=[userkey] -PdryRun=false
```        
