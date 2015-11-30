package com.supc.spider.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:spcai@wisorg.com">spcai</a>
 * @version V1.0, 15/11/13
 */
public class FileUtils {

    public static final String CHARACTER_START = "*";
    public static final String CHARACTER_DOT = ".";

    /**
     * 根据匹配规则查找文件
     *
     * @param path 查找根目录
     * @param prefix 匹配前缀
     * @param suffix 匹配后缀
     * @param fileList 匹配的文件
     * @return 返回匹配的文件列表
     */
    public static List<File> findFiles(String path, String prefix, String[] suffix, List<File> fileList) {

        if (suffix.length < 1) suffix = getDefaultExt();
        File basePath = new File(path);
        if (basePath.exists() && basePath.isDirectory()) {
            String[] files = basePath.list();
            for (int i = 0; i < files.length; i++) {
                File readFile = new File(path + "/" + files[i]);
                if (!readFile.isDirectory()) {
                    for (String s : suffix) {
                        if (matchFileName(prefix + CHARACTER_START + s, readFile.getName())) {
                            fileList.add(readFile.getAbsoluteFile());
                        }
                    }
                } else {
                    if (files[i].charAt(0) != CHARACTER_DOT.charAt(0))
                        findFiles(path + "/" + files[i], prefix, suffix, fileList);
                }
            }
        }
        return fileList;
    }

    /**
     * 通配符匹配
     *
     * @param pattern 通配符模式
     * @param str     待匹配的字符串
     * @return 匹配成功则返回true，否则返回false
     */
    private static boolean matchFileName(String pattern, String str) {
        int patternLength = pattern.length();
        int strLength = str.length();
        int strIndex = 0;
        char ch;
        for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
            ch = pattern.charAt(patternIndex);
            if (ch == '*') {
                //通配符星号*表示可以匹配任意多个字符
                while (strIndex < strLength) {
                    if (matchFileName(pattern.substring(patternIndex + 1),
                            str.substring(strIndex))) {
                        return true;
                    }
                    strIndex++;
                }
            } else if (ch == '?') {
                //通配符问号?表示匹配任意一个字符
                strIndex++;
                if (strIndex > strLength) {
                    //表示str中已经没有字符匹配?了。
                    return false;
                }
            } else {
                if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {
                    return false;
                }
                strIndex++;
            }
        }
        return (strIndex == strLength);
    }

    public static String getExt(String fileName) {
        if (fileName != null) {
            int index = fileName.lastIndexOf('.');
            if (index > -1) {
                return fileName.substring(index + 1).toLowerCase();
            }
        }
        return "";
    }

    public static String getExtWithDot(String fileName) {
        if (fileName != null) {
            int index = fileName.lastIndexOf('.');
            if (index > -1) {
                return fileName.substring(index).toLowerCase();
            }
        }
        return "";
    }

    public static String[] getDefaultExt() {
        return new String[]{".xml", ".properties"};
    }
}
