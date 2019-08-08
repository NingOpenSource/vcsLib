package org.yanning.gradle.vcs_lib.utils;

import java.io.File;

public class FileUtils {
    public static File createDir(File dir) {
        if (dir.isFile()) dir.delete();
        if (!dir.exists()) dir.mkdirs();
        if (!dir.exists()) dir.mkdir();
        return dir;
    }
}
