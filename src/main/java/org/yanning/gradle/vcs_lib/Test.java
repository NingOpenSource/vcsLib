package org.yanning.gradle.vcs_lib;

import org.yanning.gradle.vcs_lib.extension.App;
import org.yanning.gradle.vcs_lib.extension.RepositorySvn;

public class Test {
    public static void main(String... ss) {
        new App(null);
//        new RepositorySvn() {
//            {
//                username("yanning");
//                password("123456");
//                url("svn://61.160.193.26/android/vcsLibs");
//            }
//        }.update();
        new RepositorySvn() {
            {
                username("yanning");
                password("123456");
                url("svn://61.160.193.26/android/vcsLibs");
            }
        }.commit();
        new RepositorySvn() {
            {
                username("yanning");
                password("123456");
                url("svn://61.160.193.26/android/vcsLibs");
            }
        }.upload();
    }


}
