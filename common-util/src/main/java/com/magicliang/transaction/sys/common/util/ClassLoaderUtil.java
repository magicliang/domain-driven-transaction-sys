package com.magicliang.transaction.sys.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 类加载器工具
 *
 * @author magicliang
 * <p>
 * date: 2022-01-25 13:40
 */
@Slf4j
public class ClassLoaderUtil {

    private static final String CLASS_POSTFIX = ".class";
    private static final char PACKAGE_SYMBOL = '.';
    private static final char DIR_SYMBOL = '/';

    public static Class loadClass(ClassLoader classLoader, String className) throws ClassNotFoundException {
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        return ClassUtils.getClass(classLoader, className);
    }

    public static Class loadClass(String className) throws ClassNotFoundException {
        return loadClass(null, className);
    }

    public static ClassLoader getCurrentClassLoader(ClassLoader classLoader) {
        if (classLoader != null) {
            return classLoader;
        }
        ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
        if (currentLoader != null) {
            return currentLoader;
        }
        return ClassLoaderUtil.class.getClassLoader();
    }

    /**
     * 加载package下的class
     *
     * @param packageName
     * @return
     */
    public static void loadClasses(String packageName) {
        if (StringUtils.isBlank(packageName) || !packageName.contains(".")) {
            return;
        }
        log.info("Load classes in package {}", packageName);
        try {
            boolean recursive = true;
            // 获取包的名字 并进行替换
            String packageDirName = packageName.replace(PACKAGE_SYMBOL, DIR_SYMBOL);
            Enumeration<URL> dirs;
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);

            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive);
                } else if ("jar".equals(protocol)) {
                    JarFile jar;
                    try {
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if (name.charAt(0) == DIR_SYMBOL) {
                                name = name.substring(1);
                            }
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf(DIR_SYMBOL);
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace(DIR_SYMBOL, PACKAGE_SYMBOL);
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    if (name.endsWith(CLASS_POSTFIX) && !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = packageName + PACKAGE_SYMBOL + name.substring(packageName.length() + 1, name.length() - CLASS_POSTFIX.length());
                                        try {
                                            Class.forName(className);
                                        } catch (Throwable e) {
                                            log.info("Class load failed, {}", e.toString());
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Throwable e) {
                        log.info("Class load failed, {}", e.toString());
                    }
                }
            }
        } catch (Throwable e) {
            log.info("Classes load failed, {}", e.toString());
        }
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     */
    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(CLASS_POSTFIX));
            }
        });
        for (File file : dirfiles) {
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive);
            } else {
                String className = packageName + PACKAGE_SYMBOL + file.getName().substring(0, file.getName().length() - CLASS_POSTFIX.length());
                try {
                    Class.forName(className);
                } catch (Throwable e) {
                    log.info("Class load failed, {}", e.toString());
                }
            }
        }
    }
}
