# vcsLib（v3.0.4）

一个帮助链接到存储在git和svn上的maven仓库的辅助脚本，简化使用

maven官方同样也在开发类似的插件：[http://maven.apache.org/wagon/wagon-providers/wagon-scm/](http://maven.apache.org/wagon/wagon-providers/wagon-scm/)

### 使用

1. 引入脚本:
    ```gradle
    //如果是android项目需要将脚本放在build.gradle的最后一行 
    apply from:"https://raw.githubusercontent.com/NingOpenSource/vcsLib/master/script/release/vcsLib_v3.0.4.gradle"
    //或者使用gitee
    apply from:"https://gitee.com/NingOpenSource/vcsLib/raw/master/script/release/vcsLib_v3.0.4.gradle"
    ```

1. 编写vcslib.json配置(此文件会自动生成):
    
    ```json
    [
        {
            "uri": "svn://127.61.137.210:1800/Android/.vcsLibs",
            "user": "demo",
            "passwd": "123456"
        }
    ]
    ```
