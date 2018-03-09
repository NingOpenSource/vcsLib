package org.yanning.gradle.vcs_lib.utils;

public class Log {
    private static final String TAG = "-------vcsLib-------: ";

    public static void err(String message) {
        System.err.println(TAG + message);
    }

    public static void out(String message) {
        System.out.println(TAG + message);
    }
}
