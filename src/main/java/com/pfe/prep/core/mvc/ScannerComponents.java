package com.pfe.prep.core.mvc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ScannerComponents
 */
public class ScannerComponents extends ClassLoader {

    public Set<Class<?>> components(String packageName, String dir) {
        File directory = new File(dir);

        return Stream.of(
            directory.listFiles((dir1, name) -> name.endsWith(".class"))
        )
            .map(file -> {
                String className =
                    packageName + "." + file.getName().replace(".class", "");
                try {
                    return loadClass(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            })
            .filter(clazz -> clazz != null)
            .collect(Collectors.toSet());
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] b = loadClassFromFile(name);
        return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassFromFile(String fileName) {
        InputStream inputStream = getClass()
            .getClassLoader()
            .getResourceAsStream(
                fileName.replace('.', File.separatorChar) + ".class"
            );
        byte[] buffer;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int nextValue = 0;
        try {
            while ((nextValue = inputStream.read()) != -1) {
                byteStream.write(nextValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer = byteStream.toByteArray();
        return buffer;
    }
}
