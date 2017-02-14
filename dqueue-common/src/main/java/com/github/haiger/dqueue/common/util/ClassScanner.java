package com.github.haiger.dqueue.common.util;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author haiger
 * @since 2017年2月14日 下午10:37:04
 */
public class ClassScanner {
    private String packageName;
    private Set<Class<?>> allClass;

    public ClassScanner(String packageName) {
        this.packageName = packageName;
        allClass = new HashSet<Class<?>>();
    }

    public Set<Class<?>> scanAnnotation(Class<?> annotationClass) throws Throwable {
        scan(annotationClass);
        return allClass;
    }

    public Set<Class<?>> scan() throws Throwable {
        scan(null);
        return allClass;
    }

    public void scan(Class<?> annotationClass) throws Throwable {
        String relPath = packageName.replace('.', '/');

        URL resource = ClassLoader.getSystemClassLoader().getResource(relPath);
        if (resource == null) {
            resource = Thread.currentThread().getContextClassLoader().getResource(relPath);
        }

        if (resource == null) {
            throw new RuntimeException("Unexpected problem: No resource for " + relPath);
        }

        if (resource.toString().startsWith("jar:")) {
            processJarfile(resource, packageName, annotationClass);
        } else {
            processDirectory(new File(resource.getPath()), packageName, annotationClass);
        }
    }

    private void processDirectory(File directory, String pkgname, Class<?> annotationClass) throws Throwable {
        String[] files = directory.list();
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i];
            String className = null;
            if (fileName.endsWith(".class")) {
                className = pkgname + '.' + fileName.substring(0, fileName.length() - 6);
            }
            if (className != null) {
                feed(className, annotationClass);
            }
            File subdir = new File(directory, fileName);
            if (subdir.isDirectory()) {
                processDirectory(subdir, pkgname + '.' + fileName, annotationClass);
            }
        }
    }

    private void processJarfile(URL resource, String pkgname, Class<?> annotationClass) throws Throwable {
        String relPath = pkgname.replace('.', '/');
        String resPath = resource.getPath();
        String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(jarPath);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                String className = null;
                if (entryName.endsWith(".class") && entryName.startsWith(relPath)
                        && entryName.length() > (relPath.length() + "/".length())) {
                    className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
                }
                if (className != null) {
                    feed(className, annotationClass);
                }
            }
        } finally {
            if (jarFile != null)
                jarFile.close();
        }
    }

    private void feed(String className, Class<?> annotationClass) throws Throwable {
        Class<?> clazz = Class.forName(className);
        if (annotationClass != null) {
            Annotation[] annotations = clazz.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(annotationClass)) {
                    allClass.add(clazz);
                }
            }
        } else {
            allClass.add(clazz);
        }
    }
}
